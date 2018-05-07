package com.gzoltar.core.instr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.pass.IPass;
import com.gzoltar.core.instr.pass.InstrumentationPass;
import com.gzoltar.core.util.BinaryTypeDetector;
import com.gzoltar.core.util.Pack200Streams;
import com.gzoltar.core.util.SignatureRemover;
import javassist.ClassPool;
import javassist.CtClass;

/**
 * Several APIs to instrument Java class definitions for coverage tracing.
 */
public class Instrumenter {

  private final IPass[] passes;

  private final SignatureRemover signatureRemover;

  /**
   * 
   * @param agentConfigs
   */
  public Instrumenter(final AgentConfigs agentConfigs) {
    this.passes = new IPass[] {
        //new TestFilterPass(), // do not instrument test classes/cases
        new InstrumentationPass(agentConfigs)
    };
    this.signatureRemover = new SignatureRemover();
  }

  /**
   * Determines whether signatures should be removed from JAR files. This is typically necessary as
   * instrumentation modifies the class files and therefore invalidates existing JAR signatures.
   * Default is <code>true</code>.
   * 
   * @param flag <code>true</code> if signatures should be removed
   */
  public void setRemoveSignatures(final boolean flag) {
    this.signatureRemover.setActive(flag);
  }

  /**
   * 
   * @param classfileBuffer
   * @return
   * @throws Exception
   */
  public byte[] instrument(final byte[] classfileBuffer) throws Exception {
    return this.instrument(new ByteArrayInputStream(classfileBuffer));
  }

  /**
   * 
   * @param sourceStream
   * @return
   * @throws Exception
   */
  public byte[] instrument(final InputStream sourceStream) throws Exception {
    CtClass cc = ClassPool.getDefault().makeClassIfNew(sourceStream);
    return this.instrument(cc);
  }

  /**
   * 
   * @param cc
   * @return
   * @throws Exception
   */
  public byte[] instrument(final CtClass cc) throws Exception {
    for (IPass p : this.passes) {
      switch (p.transform(cc)) {
        case REJECT:
          cc.detach();
          return null;
        case ACCEPT:
        default:
          continue;
      }
    }

    byte[] bytecode = cc.toBytecode();
    cc.detach();

    return bytecode;
  }

  /**
   * Creates a instrumented version of the given resource depending on its type. Class files and the
   * content of archive files (.zip, .jar) are instrumented. All other files are copied without
   * modification.
   * 
   * @param input stream to contents from
   * @param output stream to write the instrumented version of the contents
   * @return number of instrumented classes
   * @throws Exception if reading data from the stream fails or a class cannot be instrumented
   */
  public int instrumentToFile(final InputStream input, final OutputStream output) throws Exception {
    final BinaryTypeDetector binaryType = new BinaryTypeDetector(input);
    switch (binaryType.getType()) {
      case BinaryTypeDetector.CLASSFILE:
        output.write(this.instrument(binaryType.getInputStream()));
        return 1;
      case BinaryTypeDetector.GZFILE:
        return this.instrumentGzip(binaryType.getInputStream(), output);
      case BinaryTypeDetector.PACK200FILE:
        return this.instrumentPack200(binaryType.getInputStream(), output);
      case BinaryTypeDetector.ZIPFILE:
        return this.instrumentZip(binaryType.getInputStream(), output);
      case BinaryTypeDetector.UNKNOWN:
      default:
        this.copy(binaryType.getInputStream(), output);
        return 0;
    }
  }

  private int instrumentGzip(final InputStream input, final OutputStream output) throws Exception {
    final GZIPInputStream gzipInputStream = new GZIPInputStream(input);
    final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(output);
    final int count = this.instrumentToFile(gzipInputStream, gzipOutputStream);
    gzipOutputStream.finish();
    return count;
  }

  private int instrumentPack200(final InputStream input, final OutputStream output)
      throws Exception {
    final InputStream unpackedInput = Pack200Streams.unpack(input);
    final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    final int count = this.instrumentToFile(unpackedInput, buffer);
    Pack200Streams.pack(buffer.toByteArray(), output);
    return count;
  }

  private int instrumentZip(final InputStream input, final OutputStream output) throws Exception {
    final ZipInputStream zipInputStream = new ZipInputStream(input);
    final ZipOutputStream zipOutputStream = new ZipOutputStream(output);
    ZipEntry entry;
    int count = 0;

    while ((entry = zipInputStream.getNextEntry()) != null) {
      final String entryName = entry.getName();
      if (this.signatureRemover.removeEntry(entryName)) {
        continue;
      }

      zipOutputStream.putNextEntry(new ZipEntry(entryName));
      if (!this.signatureRemover.filterEntry(entryName, zipInputStream, zipOutputStream)) {
        count += this.instrumentToFile(zipInputStream, zipOutputStream);
      }
      zipOutputStream.closeEntry();
    }
    zipOutputStream.finish();

    return count;
  }

  private void copy(final InputStream input, final OutputStream output) throws IOException {
    final byte[] buffer = new byte[1024];
    int len;
    while ((len = input.read(buffer)) != -1) {
      output.write(buffer, 0, len);
    }
  }
}

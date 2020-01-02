/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package com.gzoltar.core.instr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.jacoco.core.internal.ContentTypeDetector;
import org.jacoco.core.internal.Pack200Streams;
import org.jacoco.core.internal.instr.SignatureRemover;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.pass.IPass;
import com.gzoltar.core.instr.pass.CoveragePass;
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
        new CoveragePass(agentConfigs)
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
    final ContentTypeDetector detector = new ContentTypeDetector(input);
    switch (detector.getType()) {
      case ContentTypeDetector.CLASSFILE:
        output.write(this.instrument(detector.getInputStream()));
        return 1;
      case ContentTypeDetector.GZFILE:
        return this.instrumentGzip(detector.getInputStream(), output);
      case ContentTypeDetector.PACK200FILE:
        return this.instrumentPack200(detector.getInputStream(), output);
      case ContentTypeDetector.ZIPFILE:
        return this.instrumentZip(detector.getInputStream(), output);
      case ContentTypeDetector.UNKNOWN:
      default:
        this.copy(detector.getInputStream(), output);
        return 0;
    }
  }

  public int instrument(File source, File dest) throws Exception {
    dest.getParentFile().mkdirs();
    final InputStream input = new FileInputStream(source);
    try {
      final OutputStream output = new FileOutputStream(dest);
      try {
        return this.instrumentToFile(input, output);
      } finally {
        output.close();
      }
    } catch (Exception e) {
      throw e;
    } finally {
      input.close();
    }
  }

  public int instrumentRecursively(File source, File dest)
      throws Exception {
    int numInstrumentedClasses = 0;

    if (source.isDirectory()) {
      ClassPool.getDefault().appendClassPath(source.getAbsolutePath());
      for (final File child : source.listFiles()) {
        numInstrumentedClasses += this.instrumentRecursively(child, new File(dest, child.getName()));
      }
    } else {
      numInstrumentedClasses += this.instrument(source, dest);
    }

    return numInstrumentedClasses;
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

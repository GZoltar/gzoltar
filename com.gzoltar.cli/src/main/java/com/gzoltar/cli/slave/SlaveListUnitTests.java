package com.gzoltar.cli.slave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import com.gzoltar.cli.commands.ListUnitTests;
import com.gzoltar.cli.rmi.IMessage;
import com.gzoltar.cli.test.FindAtomicTests;
import com.gzoltar.cli.utils.ClassType;
import com.gzoltar.cli.utils.ClasspathHandler;
import com.gzoltar.cli.utils.SystemProperties;

public class SlaveListUnitTests {

  public static void main(String[] args) {
    assert args.length == 2;

    try {
      int port = Integer.parseInt(args[0]);

      Registry registry = LocateRegistry.getRegistry(port);
      IMessage message = (IMessage) registry.lookup(args[1]);

      ListUnitTests command = (ListUnitTests) message.getCommand();

      File outputDirectory = command.getOutputDirectory();
      if (!outputDirectory.exists()) {
        outputDirectory.mkdirs();
      }

      PrintWriter testsWriter = new PrintWriter(
          outputDirectory + SystemProperties.FILE_SEPARATOR + command.getOutputFile(), "UTF-8");

      ClasspathHandler classpathHandler =
          new ClasspathHandler(command.getIncludes(), command.getExcludes());

      System.out.println("* Reading all .class/.jar files");
      List<Pair<String, ClassType>> testClasses = classpathHandler.getTestClasses();

      System.out.println("* Finding all unit test cases");
      for (String testCase : FindAtomicTests.find(testClasses)) {
        testsWriter.println(testCase);
      }

      testsWriter.close();

    } catch (RemoteException | NotBoundException | FileNotFoundException
        | UnsupportedEncodingException e) {
      e.printStackTrace();
      System.exit(1);
    }

    System.out.println("* Finished");
    System.exit(0);
  }
}

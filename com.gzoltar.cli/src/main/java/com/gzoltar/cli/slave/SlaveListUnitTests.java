/**
 * Copyright (C) 2018 GZoltar contributors.
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

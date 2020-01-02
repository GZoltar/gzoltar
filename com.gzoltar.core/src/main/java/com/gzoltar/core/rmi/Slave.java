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
package com.gzoltar.core.rmi;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import com.gzoltar.core.util.SystemProperties;

public class Slave {

  /**
   * 
   * @param out
   * @param err
   * @param command
   * @param runner
   * @param classpath
   * @return
   */
  public static Response launch(final PrintStream out, final PrintStream err, final String runner,
      final String classpath, final int timeout) {

    String messageName = UUID.randomUUID().toString();

    RegistrySingleton.createSingleton();
    Response response = null;

    try {
      IMessage message = new Message();
      // TODO set parameters or properties to send

      RegistrySingleton.register(messageName, message);

      List<String> commandLineArgs = new ArrayList<String>();
      if (SystemProperties.OS_NAME.contains("windows") == true) {
        commandLineArgs.add(SystemProperties.JAVA_HOME + ".exe");
      } else {
        commandLineArgs.add(SystemProperties.JAVA_HOME);
      }

      assert commandLineArgs.size() != 0;

      commandLineArgs.add("-cp");
      commandLineArgs.add(classpath.replace(":", SystemProperties.PATH_SEPARATOR)
          + SystemProperties.PATH_SEPARATOR + SystemProperties.getClasspathString());

      commandLineArgs.add(runner);
      commandLineArgs.add(Integer.toString(RegistrySingleton.getPort()));
      commandLineArgs.add(messageName);

      ProcessBuilder pb = new ProcessBuilder(commandLineArgs);
      pb.redirectErrorStream(true);;
      final Process p = pb.start();

      InputStream is = p.getInputStream();
      BufferedInputStream isl = new BufferedInputStream(is);
      byte buffer[] = new byte[1024];
      int len = 0;

      Timer timer = new Timer();
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          p.destroy();
          err.println("A timeout has occured");
        }
      }, timeout * 1000);

      while ((len = isl.read(buffer)) != -1) {
        out.write(buffer, 0, len);
      }

      int i = p.waitFor();
      if (timer != null) {
        timer.cancel();
      }

      if (i == 0) {
        response = message.getResponse();
      }

    } catch (Exception e) {
      err.println(e);
    } finally {
      RegistrySingleton.unregister(messageName);
    }

    return response;
  }
}

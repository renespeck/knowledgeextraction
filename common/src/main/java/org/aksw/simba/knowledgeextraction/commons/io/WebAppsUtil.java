package org.aksw.simba.knowledgeextraction.commons.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import org.apache.log4j.Logger;

public class WebAppsUtil {

  public static Logger LOG = Logger.getLogger(WebAppsUtil.class);

  /**
   * Checks if the port is in use.
   *
   * @param port e.g.: 8080
   * @return true if port is free else false
   */
  public static synchronized boolean isPortAvailable(final int port) {

    ServerSocket ss = null;
    DatagramSocket ds = null;
    try {
      ss = new ServerSocket(port);
      ss.setReuseAddress(true);
      ds = new DatagramSocket(port);
      ds.setReuseAddress(true);
      return true;
    } catch (IOException | IllegalArgumentException e) {
      // logger.error("\n", e);
    } finally {
      if (ds != null) {
        ds.close();
      }

      if (ss != null) {
        try {
          ss.close();
        } catch (final IOException e) {
          // logger.error("\n", e);
        }
      }
    }
    return false;
  }


  /**
   * Gives the applications process id.
   *
   * @return applications process id
   */
  public static synchronized String getProcessId() {

    final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
    final int index = jvmName.indexOf('@');
    if (index < 1) {
      return null;
    }
    try {
      return Long.toString(Long.parseLong(jvmName.substring(0, index)));
    } catch (final NumberFormatException e) {
      return null;
    }
  }

  /**
   * Writes a system depended file to shut down the application with kill cmd and process id.
   *
   * @return true if the file was written
   */
  public static synchronized boolean writeShutDownFile(final String fileName) {

    // get process Id
    final String id = getProcessId();
    if (id == null) {
      return false;
    }

    String cmd = "";
    String fileExtension = "";

    cmd = "kill " + id + System.getProperty("line.separator") + "rm " + fileName + ".sh";
    fileExtension = "sh";
    LOG.info(fileName + "." + fileExtension);

    final File file = new File(fileName + "." + fileExtension);
    try {
      final BufferedWriter out = new BufferedWriter(new FileWriter(file));
      out.write(cmd);
      out.close();
    } catch (final Exception e) {
      LOG.error(e.getMessage());
    }
    file.setExecutable(true, false);
    file.deleteOnExit();
    return true;
  }

  public static synchronized boolean shutDown() {
    try {
      Runtime.getRuntime().exec("kill ".concat(getProcessId()));
    } catch (final IOException e) {
      LOG.error(e.getLocalizedMessage(), e);
      return false;
    }
    return true;
  }
}

package org.aksw.simba.knowledgeextraction.commons.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Gets FOX properties.
 *
 * @author rspeck
 *
 */
public class PropertiesLoader {

  public static final Logger LOG = LogManager.getLogger(PropertiesLoader.class);;

  protected static Properties properties = null;
  protected static String propertiesFile = "";

  /**
   * Loads a given file to use as properties.
   *
   * @param cfgFile properties file
   */
  public static boolean loadFile(final String cfgFile) {
    boolean loaded = false;
    LOG.info("Loads cfg ...");

    properties = new Properties();
    FileInputStream in = null;
    try {
      in = new FileInputStream(cfgFile);
    } catch (final FileNotFoundException e) {
      LOG.error("file: " + cfgFile + " not found!");
    }
    if (in != null) {
      try {
        properties.load(in);
        loaded = true;
      } catch (final IOException e) {
        LOG.error("Can't read `" + cfgFile + "` file.");
      }
      try {
        in.close();
      } catch (final Exception e) {
        LOG.error("Something went wrong.\n", e);
      }
    } else {
      LOG.error("Can't read `" + cfgFile + "` file.");
    }

    return loaded;
  }

  /**
   * Sets the file beeing used.
   *
   * @param file
   */
  public static void setPropertiesFile(final String file) {
    propertiesFile = file;
    loadFile(propertiesFile);
  }

  /**
   * Gets a property.
   *
   * @param key property key
   * @return property value
   */
  public static String get(final String key) {
    if (propertiesFile.isEmpty()) {
      throw new UnsupportedOperationException("Please set the properties file first!");
    }
    try {
      if (properties == null) {
        loadFile(propertiesFile);
      }

      return properties.getProperty(key).trim();
    } catch (final Exception e) {
      LOG.error(e.getLocalizedMessage(), e);
      LOG.info("given key is: " + key);
    }
    return null;
  }

  /**
   * Gets an object of the given class.
   *
   * @param classPath path to class
   * @return object of a class
   * @throws LoadingNotPossibleException
   */
  public synchronized static Object getClass(final String classPath) throws IOException {
    LOG.info("Loading class: " + classPath);

    Class<?> clazz = null;
    try {
      clazz = Class.forName(classPath.trim());
      final Constructor<?> constructor = clazz.getConstructor();
      return constructor.newInstance();

    } catch (final Exception e) {
      LOG.error(e.getLocalizedMessage(), e);
      throw new IOException("Could not load class: " + classPath);
    }
  }
}

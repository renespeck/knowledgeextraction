package org.aksw.simba.knowledgeextraction.commons.config;

import java.io.File;

import org.aksw.simba.knowledgeextraction.commons.io.FileUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CfgManager {

  public static String cfgFolder = "config";

  public static final Logger LOG = LogManager.getLogger(CfgManager.class);

  /**
   *
   * @param className
   * @return
   */
  public static XMLConfiguration getCfg(final String className) {

    final String file = cfgFolder + File.separator + className + ".xml";

    if (FileUtil.fileExists(file)) {

      try {
        return new XMLConfiguration(file);
      } catch (final ConfigurationException e) {
        LOG.error("Error while reading " + file, e);
      }
    } else {
      LOG.warn("Could not find " + file);
    }
    return null;
  }

  /**
   *
   * @param className
   * @return
   */
  public static XMLConfiguration getCfg(final Class<?> classs) {
    return CfgManager.getCfg(classs.getName());
  }
}

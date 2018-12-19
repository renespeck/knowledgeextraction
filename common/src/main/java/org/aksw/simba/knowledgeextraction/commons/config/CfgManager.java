package org.aksw.simba.knowledgeextraction.commons.config;

import java.io.File;

import org.aksw.simba.knowledgeextraction.commons.io.FileUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CfgManager {
  public static final Logger LOG = LogManager.getLogger(CfgManager.class);

  protected String cfgFolder = null;

  public CfgManager(final String cfgFolder) {
    this.cfgFolder = cfgFolder;
  }

  public XMLConfiguration getCfg(final String className) {
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

  public XMLConfiguration getCfg(final Class<?> classs) {
    return this.getCfg(classs.getName());
  }
}

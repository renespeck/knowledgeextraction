package org.aksw.simba.knowledgeextraction.commons.dbpedia;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DBpedia {

  protected final static Logger LOG = LogManager.getLogger(DBpedia.class);

  public static final String url = "http://dbpedia.org/sparql";

  public static final String graph = "http://dbpedia.org";

  public static final String ns_dbpedia_ontology = "http://dbpedia.org/ontology/";

  public static final int pagination = 1000;
  public static final int delay = 0;

  public static final String queryPrefix =
      ClassLoader.getSystemClassLoader().getResource("prefix.sparql").getPath();

  public static final String queryLabels =
      ClassLoader.getSystemClassLoader().getResource("labels.sparql").getPath();

  public static final String queryWikidata =
      ClassLoader.getSystemClassLoader().getResource("wikidatalink.sparql").getPath();

  public static String PREFIX;
  static {
    try {
      PREFIX = new String(Files.readAllBytes(Paths.get(queryPrefix)));
    } catch (final IOException e) {
      LOG.error(e.getLocalizedMessage(), e);
      PREFIX = "";
    }
  }
}

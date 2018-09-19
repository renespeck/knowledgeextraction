package org.aksw.simba.knowledgeextraction.commons.wikidata;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class WikidataLabelsTest {
  protected final static Logger LOG = LogManager.getLogger(WikidataLabelsTest.class);

  @Test
  public void test() {

    final String predicate = "http://dbpedia.org/ontology/birthPlace";
    final WikidataLabels wikidataLabels = new WikidataLabels();

    final List<String> lables = wikidataLabels.labels(predicate);

    Assert.assertTrue(lables != null);

    lables.forEach(LOG::info);
  }
}

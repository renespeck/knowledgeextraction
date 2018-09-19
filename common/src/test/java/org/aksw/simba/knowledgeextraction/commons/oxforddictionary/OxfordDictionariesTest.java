package org.aksw.simba.knowledgeextraction.commons.oxforddictionary;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

public class OxfordDictionariesTest {

  final static Logger LOG = LogManager.getLogger(OxfordDictionariesTest.class);

  @Test
  public void test() {
    final OxfordDictionaries ofd = new OxfordDictionaries();
    ofd.synonyms("spouse").forEach(LOG::info);
  }
}

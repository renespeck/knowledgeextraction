package org.aksw.simba.knowledgeextraction.commons.wordnik;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

public class WordnikRelatedWordsTest {
  final static Logger LOG = LogManager.getLogger(WordnikRelatedWordsTest.class);

  @Test
  public void test() {
    final WordnikRelatedWords ofd = new WordnikRelatedWords();
    ofd.synonyms("spouse").forEach(LOG::info);
  }

}

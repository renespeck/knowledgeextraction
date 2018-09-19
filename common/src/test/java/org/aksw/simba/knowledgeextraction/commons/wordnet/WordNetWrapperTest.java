package org.aksw.simba.knowledgeextraction.commons.wordnet;

import java.util.Collection;

import org.aksw.simba.knowledgeextraction.commons.nlp.PartOfSpeech;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class WordNetWrapperTest {
  protected static Logger LOG = LogManager.getLogger(WordNetWrapperTest.class);

  String path = "/home/rspeck/Data/WordNet-3.1";

  @Test
  public void test() {
    final WordNetWrapper wordNetWrapper = new WordNetWrapper(path);

    final Collection<String> synonyms = wordNetWrapper.//
        synonymsWordnet("ceo", PartOfSpeech.NOUN, 10);

    synonyms.forEach(LOG::info);

    Assert.assertTrue(synonyms.size() <= 10);
  }
}

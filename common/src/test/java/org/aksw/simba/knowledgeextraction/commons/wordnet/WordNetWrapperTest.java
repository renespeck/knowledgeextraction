package org.aksw.simba.knowledgeextraction.commons.wordnet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class WordNetWrapperTest {

  protected static Logger LOG = LogManager.getLogger(WordNetWrapperTest.class);

  // FIXME: test depends on external data.
  /**
   * <code>
    String path = "/home/rspeck/Data/WordNet-3.1";

    &#64;Test
    public void test() {
      final WordNetWrapper wordNetWrapper = new WordNetWrapper(path);

      final Collection<String> synonyms = wordNetWrapper.//
          synonymsWordnet("ceo", PartOfSpeech.NOUN, 10);

      synonyms.forEach(LOG::info);

      Assert.assertTrue(synonyms.size() <= 10);
    }</code>
   */
}

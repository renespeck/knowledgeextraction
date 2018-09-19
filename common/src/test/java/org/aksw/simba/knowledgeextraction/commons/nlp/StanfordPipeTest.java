package org.aksw.simba.knowledgeextraction.commons.nlp;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class StanfordPipeTest {
  public static final Logger LOG = LogManager.getLogger(StanfordPipeTest.class);

  @Test
  public void test() {

    // final StanfordPipe stanford = StanfordPipeExtended.getStanfordPipe();
    final StanfordPipe stanford = StanfordPipe.getStanfordPipe();

    try {
      Assert.assertTrue(stanford.getSentences().size() > 0);
    } catch (final Exception e) {
      LOG.error(e.getLocalizedMessage());
    }

    final String example = "Test sentence.";
    stanford.process(example);
    Assert.assertTrue(stanford.getSentences().size() > 0);
  }
}

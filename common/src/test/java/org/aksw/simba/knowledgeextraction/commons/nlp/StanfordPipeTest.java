package org.aksw.simba.knowledgeextraction.commons.nlp;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import edu.stanford.nlp.pipeline.Annotation;

public class StanfordPipeTest {

  public static final Logger LOG = LogManager.getLogger(StanfordPipeTest.class);

  @Test
  public void test() {

    final StanfordPipe stanford = new StanfordPipe();

    final String example = "Test sentence.";
    final Annotation annotation = stanford.process(example);

    Assert.assertTrue(stanford.getSentences(annotation).size() > 0);
  }
}

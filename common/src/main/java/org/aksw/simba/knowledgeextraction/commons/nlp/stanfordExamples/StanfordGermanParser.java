package org.aksw.simba.knowledgeextraction.commons.nlp.stanfordExamples;

import org.aksw.simba.knowledgeextraction.commons.nlp.StanfordPipeExtended;
import org.aksw.simba.knowledgeextraction.commons.nlp.StanfordProperties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import edu.stanford.nlp.ling.CoreLabel;

// TODO: move to tests.
public class StanfordGermanParser {

  public static final Logger LOG = LogManager.getLogger(StanfordGermanParser.class);

  public static void main(final String[] a) {

    final StanfordPipeExtended stan =
        StanfordPipeExtended.instance(StanfordProperties.getPropertiesDE());

    final String sentence = "Angela Merkel was born in Germany.";

    for (final CoreLabel label : stan.getLabels(sentence)) {
      LOG.info("NER: " + stan.getNE(label));
      LOG.info("POS: " + stan.getPOS(label));
    }

    LOG.info(stan.getSemanticGraph(sentence));
  }
}

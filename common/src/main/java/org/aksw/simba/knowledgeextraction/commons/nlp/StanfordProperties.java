package org.aksw.simba.knowledgeextraction.commons.nlp;

import java.util.Properties;

/**
 * TODO: Add more languages here.
 *
 *
 * @author rspeck
 *
 */
public class StanfordProperties {

  public static Properties getPropertiesDE() {
    final Properties properties = new Properties();
    properties.put("annotators", "tokenize, ssplit, pos, ner, parse, depparse");
    properties.put("tokenize.language", "de");
    properties.put("pos.model", "edu/stanford/nlp/models/pos-tagger/german/german-hgc.tagger");
    // properties.put("ner.model", "edu/stanford/nlp/models/ner/german.hgc_175m_600.crf.ser.gz");
    properties.put("ner.model",
        "edu/stanford/nlp/models/ner/german.conll.germeval2014.hgc_175m_600.crf.ser.gz");
    properties.put("ner.applyNumericClassifiers", "false");
    properties.put("ner.applyFineGrained", "false");
    properties.put("ner.useSUTime", "false");
    properties.put("parse.model", "edu/stanford/nlp/models/lexparser/germanFactored.ser.gz");
    properties.put("depparse.mode", "edu/stanford/nlp/models/parser/nndep/UD_German.gz");
    properties.put("depparse.language", "german");

    return properties;
  }
}

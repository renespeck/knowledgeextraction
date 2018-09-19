package org.aksw.simba.knowledgeextraction.commons.nlp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;

/**
 *
 * @author Ren&eacute; Speck <speck@informatik.uni-leipzig.de>
 *
 */
public class StanfordPipe {

  public static final Logger LOG = LogManager.getLogger(StanfordPipe.class);

  /** An instance of of {@link edu.stanford.nlp.pipeline.StanfordCoreNLP}. */
  protected StanfordCoreNLP pipeline = null;

  protected static StanfordPipe stanfordPipe = null;

  public static StanfordPipe instance() {
    if (stanfordPipe == null) {
      stanfordPipe = new StanfordPipe();
    }
    return stanfordPipe;
  }

  /**
   *
   * Constructor.
   *
   */
  public StanfordPipe() {
    this(0);
  }

  /**
   *
   * Constructor.
   *
   * @param numberOfThreads
   */
  public StanfordPipe(final int numberOfThreads) {

    final Properties props = new Properties();
    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
    // props.setProperty("tokenize.language", "en");
    // props.setProperty("ner.applyNumericClassifiers", "false");
    // props.setProperty("ner.useSUTime", "false");
    props.setProperty("ner.model",
        "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz");

    if (numberOfThreads > 0) {
      props.put("threads", String.valueOf(numberOfThreads));
    }

    pipeline = new StanfordCoreNLP(props);
  }

  /**
   * Splits text the sentences and stores in an empty map , keys are the sentence order.
   *
   * @param text
   * @return LinkedHashMap containing sentences
   */
  public Map<Integer, String> getSentenceIndex(final String text) {
    final Map<Integer, String> sentenceIndex = new LinkedHashMap<>();
    final List<CoreMap> list = getSentences(text);
    for (final CoreMap sentence : list) {
      final List<CoreLabel> labels = sentence.get(CoreAnnotations.TokensAnnotation.class);
      String originalSentence = Sentence.listToOriginalTextString(labels);
      if (list.indexOf(sentence) != (list.size() - 1)) {
        originalSentence = originalSentence.replaceAll("\\s+$", "");
      }
      sentenceIndex.put(sentenceIndex.size(), originalSentence);
    }
    return sentenceIndex;
  }

  public Annotation process(final String text) {
    Annotation document = null;
    if (pipeline == null) {
      LOG.error("Stanford pipeline null.");
    } else if ((text != null) && (text.trim().length() > 0)) {
      document = new Annotation(text);
      pipeline.annotate(document);
    } else {
      LOG.warn("Input is null or empty.");
    }
    return document;
  }

  public List<CoreMap> getSentences(final Annotation document) {
    if (document == null) {
      throw new UnsupportedOperationException(
          "No document set yet. Call process(text) method first to set the document.");
    }
    return document.get(SentencesAnnotation.class);
  }

  public List<CoreMap> getSentences(final String text) {
    final Annotation document = process(text);
    return getSentences(document);
  }

  public List<IndexedWord> getShortestUndirectedPathNodes(final SemanticGraph sg, final int source,
      final int target, final List<String> token) {

    final IndexedWord s = sg.getNodeByIndex(source);
    final IndexedWord t = sg.getNodeByIndex(target);

    // find the shortest path
    final List<IndexedWord> shortestPath = new ArrayList<>();

    List<IndexedWord> list = null;
    list = sg.getShortestUndirectedPathNodes(s, sg.getFirstRoot());
    if ((list != null) && !list.isEmpty()) {
      shortestPath.addAll(list);
    }
    shortestPath.remove(sg.getFirstRoot()); // will be added again in the next step
    shortestPath.remove(s);

    list = null;
    list = sg.getShortestUndirectedPathNodes(sg.getFirstRoot(), t);
    if ((list != null) && !list.isEmpty()) {
      shortestPath.addAll(list);
    }
    shortestPath.remove(t);

    if (shortestPath.isEmpty()) {
      LOG.warn(String.format("Shortest path is empty. Parameter are: %s , %s", sg, token));
    }

    return shortestPath;
  }

  public List<SemanticGraph> getSemanticGraph(final Annotation annotation) {
    final List<SemanticGraph> semanticGraphs = new ArrayList<>();
    for (final CoreMap sentence : getSentences(annotation)) {
      final SemanticGraph dependencies = sentence.get(BasicDependenciesAnnotation.class);
      semanticGraphs.add(dependencies);
    }
    return semanticGraphs;
  }

  public List<SemanticGraph> getSemanticGraph(final List<CoreMap> sentences) {
    final List<SemanticGraph> semanticGraphs = new ArrayList<>();
    for (final CoreMap sentence : sentences) {
      final SemanticGraph dependencies = sentence.get(BasicDependenciesAnnotation.class);
      semanticGraphs.add(dependencies);
    }
    return semanticGraphs;
  }

  public SemanticGraph getSemanticGraph(final String sentence) {
    final List<CoreMap> sentencesList = getSentences(sentence);
    if (sentencesList.size() > 1) {
      LOG.warn("Found more than one sentence! We you use the 1st one at the moment.");
    }
    if (sentencesList.size() < 1) {
      LOG.warn("Could not found a sentence!");
      return new SemanticGraph();
    }
    return getSemanticGraph(sentencesList).get(0);
  }

  /**
   * Returns the annotation for the 1st sentence in the given String.
   *
   * @param sentence
   * @return annotations
   */
  public List<CoreLabel> getLabels(final String sentence) {
    final List<CoreMap> coreMap = getSentences(sentence);
    if (coreMap.size() > 1) {
      LOG.warn("Found more than one sentence!");
    }
    return coreMap.get(0).get(TokensAnnotation.class);
  }

  public String getWord(final CoreLabel token) {
    return token.get(TextAnnotation.class);
  }

  public String getPOS(final CoreLabel token) {
    return token.get(PartOfSpeechAnnotation.class);
  }

  public String getNE(final CoreLabel token) {
    return token.get(NamedEntityTagAnnotation.class);
  }

  public IndexedWord getRoot(final String sentence) {
    return getRoot(getSemanticGraph(sentence));
  }

  public IndexedWord getRoot(final SemanticGraph s) {
    if ((s == null) || (s.getRoots().size() < 1)) {
      return null;
    }
    return s.getRoots().iterator().next();
  }

  public String getLemma(final CoreLabel token) {
    return token.get(LemmaAnnotation.class);
  }
}

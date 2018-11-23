package org.aksw.simba.knowledgeextraction.commons.nlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.util.Pair;

public class StanfordPipeExtended extends StanfordPipe {

  public static final String NO_NER = "O";
  protected static final Set<String> posProperNounTags = new HashSet<>();

  static {
    posProperNounTags.add(PartOfSpeech.NOUN_PROPER_SINGULAR.getTag());
    posProperNounTags.add(PartOfSpeech.NOUN_PROPER_PLURAL.getTag());
  }
  protected static StanfordPipeExtended stanfordPipe = null;

  /**
   * Creates a new instance if needed with default properties.
   *
   * @return a StanfordPipeExtended instance
   */
  public static StanfordPipeExtended instance() {
    if (stanfordPipe == null) {
      stanfordPipe = new StanfordPipeExtended();
    }
    return stanfordPipe;
  }

  /**
   * Creates a new instance with default properties.
   *
   * @return a StanfordPipeExtended instance
   */
  public static StanfordPipeExtended newInstance() {
    stanfordPipe = new StanfordPipeExtended();
    return stanfordPipe;
  }

  /**
   * Creates a new instance if needed with default properties.
   *
   * @return a StanfordPipeExtended instance
   */
  public static StanfordPipeExtended instance(final Properties properties) {
    if (stanfordPipe == null) {
      stanfordPipe = new StanfordPipeExtended(properties);
    }
    return stanfordPipe;
  }

  /**
   * Creates a new instance with default properties.
   *
   * @return a StanfordPipeExtended instance
   */
  public static StanfordPipeExtended newInstance(final Properties properties) {
    stanfordPipe = new StanfordPipeExtended(properties);
    return stanfordPipe;
  }

  public StanfordPipeExtended() {
    super();
  }

  public StanfordPipeExtended(final Properties properties) {
    super(properties);
  }

  /**
   * Checks if the roots POS tag is is a noun (NNP NNPS).
   *
   * @param sg SemanticGraph
   *
   * @return the root if its useful, otherwise null
   */
  public IndexedWord checkRoot(final SemanticGraph sg) {

    IndexedWord root = null;
    try {
      if (!sg.getRoots().isEmpty()) {
        root = sg.getFirstRoot();
      }
    } catch (final Exception e) {
      LOG.warn("No root found in the graph.");
    }

    if (root != null && posProperNounTags.contains(root.tag())) {
      root = null;

      LOG.warn(
          "1st of " + sg.getRoots().size() + " (" + root + ") root is one of " + posProperNounTags);
      for (final IndexedWord roots : sg.getRoots()) {
        if (!posProperNounTags.contains(roots.tag())) {
          LOG.info("Found a better one: " + roots);
          root = roots;
          break;
        }
      }
    }
    return root;
  }

  public List<IndexedWord> clean(final List<IndexedWord> shortestPath, final SemanticGraph sg,
      final int source, final int target) {

    final IndexedWord s = sg.getNodeByIndex(source);
    final IndexedWord t = sg.getNodeByIndex(target);

    List<IndexedWord> sp = new ArrayList<>();
    // index to node
    final Map<Integer, IndexedWord> shortestPathIndexMap = new HashMap<>();
    // adds the shortest path to map (index to word)
    shortestPath.forEach(w -> shortestPathIndexMap.put(w.index(), w));

    // removes proper nouns at the begin
    boolean isStartingProperNoun = true;
    for (final Integer i : new TreeSet<>(shortestPathIndexMap.keySet())) {
      final IndexedWord word = shortestPathIndexMap.get(i);
      if (!posProperNounTags.contains(word.tag())) {
        isStartingProperNoun = false;
      }
      if (!isStartingProperNoun) {
        // add the ordered shortest path
        sp.add(word);
      }
    }

    // removes proper nouns at the end
    for (final ListIterator<IndexedWord> iter = sp.listIterator(sp.size()); iter.hasPrevious();) {
      final IndexedWord word = iter.previous();
      if (!posProperNounTags.contains(word.tag())) {
        break;
      }
      iter.remove();
    }

    // removes entities known as surfaceforms from shortest path
    String prevNERType = null;
    for (final Iterator<IndexedWord> iter = sp.iterator(); iter.hasNext();) {
      final IndexedWord word = iter.next();

      if (word.index() == s.index() || word.index() == t.index()) {
        prevNERType = word.ner();
      }

      if (prevNERType != null) {
        if (prevNERType.equals(word.ner())) {
          iter.remove();
        } else {
          prevNERType = null;
        }
      }
    } // ebd for

    boolean onlyNounes = true;
    for (final IndexedWord w : shortestPath) {
      if (!posProperNounTags.contains(w.tag())) {
        onlyNounes = false;
        break;
      }
    }
    if (onlyNounes) {
      LOG.warn("Shortest path with proper nones only: " + shortestPath);
      sp = null;
    }
    return sp;
  }

  /**
   *
   * @param si
   * @param oi
   * @return new Pair<>(si + 1, oi + 1)
   */
  @Deprecated
  public Pair<Integer, Integer> tokenIndexToSGIndex(final int si, final int oi) {
    return new Pair<>(si + 1, oi + 1);
  }
}

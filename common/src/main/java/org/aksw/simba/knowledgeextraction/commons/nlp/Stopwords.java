package org.aksw.simba.knowledgeextraction.commons.nlp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Stopwords {

  protected final static Logger LOG = LogManager.getLogger(Stopwords.class);

  final List<String> stopwords = new ArrayList<>();

  /**
   *
   */
  public Stopwords() {
    final String file = "/stopwords/english.stop";
    final String path = getClass().getResource(file).getPath();
    try {
      stopwords.addAll(Files.readAllLines(Paths.get(path)));
    } catch (final IOException e) {
      LOG.error("Stopwords not used!!", e);
    }
  }

  /**
   * Splits elements in the given list and rRemoves stopwords form it.
   *
   * @param words
   * @return words without stopwords
   */
  public List<String> removeStopwords(final List<String> words) {

    final List<String> noStopwords = new ArrayList<>();

    words.forEach(word -> {
      final StringBuilder sb = new StringBuilder();
      for (final String s : word.split(" ")) {
        if (!stopwords.contains(s)) {
          sb.append(s).append(" ");
        }
      }
      if (sb.length() > 0) {
        noStopwords.add(sb.toString().trim());
      }
    });
    return noStopwords;
  }
}

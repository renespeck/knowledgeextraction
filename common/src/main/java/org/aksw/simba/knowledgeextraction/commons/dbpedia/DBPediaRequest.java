package org.aksw.simba.knowledgeextraction.commons.dbpedia;

import java.util.HashSet;
import java.util.Set;

import org.aksw.simba.knowledgeextraction.commons.io.SparqlExecution;
import org.json.JSONArray;

public class DBPediaRequest extends SparqlExecution {

  /**
   *
   * Constructor.
   *
   */
  public DBPediaRequest() {
    super(DBpedia.url, DBpedia.graph, DBpedia.pagination, DBpedia.delay);
  }

  /**
   *
   * Constructor.
   *
   * @param url
   * @param graph
   * @param pagination
   * @param delay
   */
  public DBPediaRequest(final String url, final String graph, final int pagination,
      final int delay) {
    super(url, graph, pagination, delay);
  }

  /**
   * Gets the labels to a given subject.
   */
  public Set<String> getLabels(final String uri) {

    final String q = DBpedia.PREFIX //
        + "select distinct ?label where {" //
        + "<" + uri + "> rdfs:label ?label." //
        + "FILTER (lang(?label) = 'en')" //
        + "} ";

    final JSONArray ja = execSelectToJSONArray(q);

    final Set<String> labels = new HashSet<>();
    for (int i = 0; i < ja.length(); i++) {
      labels.add(ja.getJSONObject(i).getJSONObject("label").getString("value"));
    }
    return labels;
  }
}

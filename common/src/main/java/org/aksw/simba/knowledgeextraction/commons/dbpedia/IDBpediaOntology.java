package org.aksw.simba.knowledgeextraction.commons.dbpedia;

import java.util.AbstractMap.SimpleEntry;
import java.util.Set;

/**
 *
 * @author Ren&eacute; Speck <speck@informatik.uni-leipzig.de>
 *
 */
public interface IDBpediaOntology {

  /**
   * Gets all subClasses and the subClasses of the subClasses for the given one.
   *
   * @param dbpediaClass
   * @return a set with all subClasses.
   */
  public Set<String> getSubClassesOf(String dbpediaClass);

  /**
   * Gets all superClasses and the superClasses of the superClasses for the given one.
   *
   *
   * @param dbpediaClass
   * @return a set with all superClasses.
   */
  public Set<String> getSuperClassesOf(final String dbpediaClass);

  /**
   * Gets the domain and range of the given property.
   *
   * @param dbpediaProperty
   * @return pair of domain and range, in case one is not set, it is interpreted that it holds for
   *         everything.
   */
  public SimpleEntry<Set<String>, Set<String>> getDomainRange(String dbpediaProperty);
}

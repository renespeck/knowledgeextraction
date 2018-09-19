package org.aksw.simba.knowledgeextraction.commons.dbpedia;

import java.util.AbstractMap.SimpleEntry;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class DBpediaOntologyTest {

  final IDBpediaOntology dbediaOntology = new DBpediaOntology();

  @Test
  public void testA() {

    final Set<String> subPerson =
        dbediaOntology.getSubClassesOf(DBpedia.ns_dbpedia_ontology.concat("Person"));

    Assert.assertNotNull(subPerson);
    Assert.assertFalse(subPerson.isEmpty());
    Assert.assertTrue(subPerson.contains(DBpedia.ns_dbpedia_ontology.concat("Humorist")));

    final Set<String> suberHumorist =
        dbediaOntology.getSuperClassesOf(DBpedia.ns_dbpedia_ontology.concat("Humorist"));

    Assert.assertNotNull(suberHumorist);
    Assert.assertFalse(subPerson.isEmpty());
    Assert.assertTrue(suberHumorist.contains(DBpedia.ns_dbpedia_ontology.concat("Person")));
  }

  @Test
  public void testB() {
    // birthPlace
    final SimpleEntry<Set<String>, Set<String>> pair =
        dbediaOntology.getDomainRange(DBpedia.ns_dbpedia_ontology.concat("birthPlace"));
    // Domain
    Assert.assertTrue(pair.getKey().contains(DBpedia.ns_dbpedia_ontology.concat("Person")));
    // Range
    Assert.assertTrue(pair.getValue().contains(DBpedia.ns_dbpedia_ontology.concat("Place")));
  }

  @Test
  public void testC() {
    // birthPlace
    final SimpleEntry<Set<String>, Set<String>> pair =
        dbediaOntology.getDomainRange(DBpedia.ns_dbpedia_ontology.concat("award"));
    // Domain
    Assert.assertTrue(pair.getKey().isEmpty());
    // Range
    Assert.assertTrue(pair.getValue().contains(DBpedia.ns_dbpedia_ontology.concat("Award")));
  }

}

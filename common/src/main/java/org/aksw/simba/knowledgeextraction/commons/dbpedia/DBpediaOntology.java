package org.aksw.simba.knowledgeextraction.commons.dbpedia;

import java.io.File;
import java.io.InputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDFS;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Reads DBpedia Ontology given in n-triples format.
 *
 * @author Ren&eacute; Speck <speck@informatik.uni-leipzig.de>
 *
 */
public class DBpediaOntology implements IDBpediaOntology {

  protected final static Logger LOG = LogManager.getLogger(DBpediaOntology.class);

  protected Model dbpediaOntology = null;

  /**
   * Loads file 'dbpedia_2016-10.nt' from resources.
   *
   */
  public DBpediaOntology() {
    final String file = "/dbpedia".concat(File.separator).concat("dbpedia_2016-10.nt");
    final InputStream is = getClass().getResourceAsStream(file);
    init(is);
  }

  /**
   *
   *
   * @param file path to 'dbpedia_2016-10.nt'
   */
  public DBpediaOntology(final String file) {
    init(getClass().getResourceAsStream(file));

  }

  private void init(final InputStream file) {
    dbpediaOntology = ModelFactory.createDefaultModel();
    dbpediaOntology.read(file, null, "N-TRIPLE");
  }

  @Override
  public Set<String> getSubClassesOf(final String dbpediaClass) {
    final Set<String> subClasses = ConcurrentHashMap.newKeySet();

    if (dbpediaClass.startsWith(DBpedia.ns_dbpedia_ontology)) {
      final StmtIterator iter = dbpediaOntology//
          .listStatements(null, RDFS.subClassOf, ResourceFactory.createResource(dbpediaClass));
      while (iter.hasNext()) {
        final Statement statement = iter.next();
        final String uri = statement.getSubject().getURI();
        if (uri.startsWith(DBpedia.ns_dbpedia_ontology)) {
          subClasses.add(uri);
        }
      }

      for (final String s : subClasses) {
        subClasses.addAll(getSubClassesOf(s));
      }
    } else {
      LOG.warn("The argument needs to start with ".concat(DBpedia.ns_dbpedia_ontology).concat("."));
    }
    return subClasses;
  }

  @Override
  public SimpleEntry<Set<String>, Set<String>> getDomainRange(final String dbpediaProperty) {

    final Set<String> domain = ConcurrentHashMap.newKeySet();
    final Set<String> range = ConcurrentHashMap.newKeySet();

    if (dbpediaProperty.startsWith(DBpedia.ns_dbpedia_ontology)) {
      dbpediaOntology
          .listObjectsOfProperty(ResourceFactory.createResource(dbpediaProperty), RDFS.domain)
          .toSet()//
          .stream().map(node -> node.asResource().getURI()).forEach(domain::add);
      dbpediaOntology
          .listObjectsOfProperty(ResourceFactory.createResource(dbpediaProperty), RDFS.range)
          .toSet()//
          .stream().map(node -> node.asResource().getURI()).forEach(range::add);

    }
    return new SimpleEntry<Set<String>, Set<String>>(domain, range);
  }

  @Override
  public Set<String> getSuperClassesOf(final String dbpediaClass) {
    final Set<String> superClasses = ConcurrentHashMap.newKeySet();
    if (dbpediaClass.startsWith(DBpedia.ns_dbpedia_ontology)) {
      final NodeIterator iter = dbpediaOntology//
          .listObjectsOfProperty(ResourceFactory.createResource(dbpediaClass), RDFS.subClassOf);
      while (iter.hasNext()) {
        final RDFNode node = iter.next();
        final String uri = node.asResource().getURI();
        if (uri.startsWith(DBpedia.ns_dbpedia_ontology)) {
          superClasses.add(uri);
        }
      }
      for (final String s : superClasses) {
        superClasses.addAll(getSuperClassesOf(s));
      }
    } else {
      LOG.warn("The argument needs to start with ".concat(DBpedia.ns_dbpedia_ontology)
          .concat(" but is " + dbpediaClass + "."));
    }
    return superClasses;
  }
}

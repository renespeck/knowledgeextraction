package org.aksw.simba.knowledgeextraction.commons.lang;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

public class CollectionUtil {
  /**
   * Example code: <code>
      List<String> stringList = Arrays.asList("1","2","3");
      List<Integer> integerList = convertList(stringList, s -> Integer.parseInt(s));
    </code>
   *
   * @param from
   * @param func
   * @return
   */
  public static <T, U> List<U> convertList(final List<T> from, final Function<T, U> func) {
    return from.stream().map(func).collect(Collectors.toList());
  }

  /**
   * example code:<code>
      String[] stringArr = {"1","2","3"};
      Double[] doubleArr = convertArray(stringArr, Double::parseDouble, Double[]::new);
   </code>
   *
   * @param from
   * @param func
   * @param generator
   * @return
   */
  public static <T, U> U[] convertArray(final T[] from, final Function<T, U> func,
      final IntFunction<U[]> generator) {
    return Arrays.stream(from).map(func).toArray(generator);
  }

  public static <T> Set<T> toSet(final T[] objects) {
    return new HashSet<>(Arrays.asList(objects));
  }

  public static <T> List<T> toList(final T[] objects) {
    return new ArrayList<>(Arrays.asList(objects));
  }

  /**
   * Calls toString and joining with a space.
   *
   * @param stream
   * @return string
   */
  public static String objectsToString(final Collection<Object> stream) {
    return stream.stream().map(Object::toString).collect(Collectors.joining(" "));
  }

  /**
   * Transforms all strings in the given list to lower case.
   *
   * @param list list with strings
   * @return list with lower case strings
   */
  public static List<String> toLowerCase(final List<String> list) {
    return list.parallelStream().map(String::toLowerCase).collect(Collectors.toList());
  }

  /**
   *
   * Adds normalized string (without accents) to the given set.
   *
   * @param set set with strings
   * @return set with normalized strings
   */
  public static Set<String> addNonAccent(final Set<String> set) {
    final Set<String> normalized = new HashSet<>();
    for (final String s : set) {
      String normal = Normalizer.normalize(s, Normalizer.Form.NFD);
      normal = normal.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
      if (!normal.equals(s)) {
        normalized.add(normal);
      }
    }
    set.addAll(normalized);
    return set;
  }

  /**
   * Converts a String in list format (["1st element",2nd element"]) to a list of Strings.
   *
   * @param listString Sting in list format
   * @return List of String
   *
   */
  public static List<String> toList(final String listString) {
    return Arrays.asList(toArray(listString));
  }

  /**
   * Converts a String in list format (["1st element",2nd element"]) to a Array of Strings.
   *
   * @param listString Sting in list format
   * @return Array of String
   *
   */
  public static String[] toArray(final String listString) {
    final String[] a =
        listString.trim().substring(1).substring(0, listString.length() - 2).split("\\s*,\\s*");

    final List<String> list = new ArrayList<>(Arrays.asList(a));
    list.removeAll(Collections.singleton(""));
    return list.toArray(new String[list.size()]);
  }

  /**
   * Converts a String in list format (["1st element",2nd element"]) to a String with space
   * separation.
   *
   * @param listString Sting in list format
   * @return String
   *
   */
  public static String arrayToString(final String listString) {
    return StringUtils.join(CollectionUtil.toArray(listString), " ").replaceAll(" +", " ");
  }

  public static String replaceLast(final String sentence, final String word,
      final String replacement) {
    final int i = sentence.lastIndexOf(word);
    return sentence.substring(0, i) + replacement + sentence.substring(i + word.length());
  }
}

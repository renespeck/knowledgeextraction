package org.aksw.simba.knowledgeextraction.commons.cache;

import java.util.ArrayList;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.LRUMap;

/**
 *
 * @author Ren&eacute; Speck <speck@informatik.uni-leipzig.de>
 *
 * @param <K>
 * @param <T>
 */
public class InMemoryCache<K, T> {

  private final long timeToLive;
  private final LRUMap<K, CachedObject> cache;

  protected class CachedObject {
    public long lastAccessed = System.currentTimeMillis();
    public T value;

    protected CachedObject(final T value) {
      this.value = value;
    }
  }

  /**
   *
   * Constructor.
   *
   * @param timeToLive in ms
   * @param timerInterval in ms
   */
  public InMemoryCache(final long timeToLive, final long timerInterval) {

    this.timeToLive = timeToLive;
    cache = new LRUMap<K, CachedObject>();

    if ((timeToLive > 0) && (timerInterval > 0)) {
      final Thread t = new Thread(() -> {
        while (true) {
          try {
            Thread.sleep(timerInterval);
          } catch (final InterruptedException ex) {
          }
          cleanup();
        }
      });
      t.setDaemon(true);
      t.start();
    }
  }

  /**
   *
   * Constructor.
   *
   * @param timeToLive in ms
   * @param timerInterval in ms
   * @param maxItems
   */
  public InMemoryCache(final long timeToLive, final long timerInterval, final int maxItems) {

    this.timeToLive = timeToLive;
    cache = new LRUMap<K, CachedObject>(maxItems);

    if ((timeToLive > 0) && (timerInterval > 0)) {
      final Thread t = new Thread(() -> {
        while (true) {
          try {
            Thread.sleep(timerInterval);
          } catch (final InterruptedException ex) {
          }
          cleanup();
        }
      });
      t.setDaemon(true);
      t.start();
    }
  }

  public void put(final K key, final T value) {
    synchronized (cache) {
      cache.put(key, new CachedObject(value));
    }
  }

  public T get(final K key) {
    synchronized (cache) {
      final CachedObject c = cache.get(key);

      if (c == null) {
        return null;
      } else {
        c.lastAccessed = System.currentTimeMillis();
        return c.value;
      }
    }
  }

  public void remove(final K key) {
    synchronized (cache) {
      cache.remove(key);
    }
  }

  public int size() {
    synchronized (cache) {
      return cache.size();
    }
  }

  public void cleanup() {
    final long now = System.currentTimeMillis();
    ArrayList<K> deleteKey = null;

    synchronized (cache) {
      final MapIterator<K, CachedObject> itr = cache.mapIterator();

      deleteKey = new ArrayList<K>((cache.size() / 2) + 1);
      K key = null;
      CachedObject c = null;

      while (itr.hasNext()) {
        key = itr.next();
        c = itr.getValue();

        if ((c != null) && (now > (timeToLive + c.lastAccessed))) {
          deleteKey.add(key);
        }
      }
    }

    for (final K key : deleteKey) {
      synchronized (cache) {
        cache.remove(key);
      }
      Thread.yield();
    }
  }
}

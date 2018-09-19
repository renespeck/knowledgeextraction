package org.aksw.simba.knowledgeextraction.commons.cache;

import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class InMemoryCacheTest {
  protected static final Logger LOG = LogManager.getLogger(InMemoryCacheTest.class);

  @Test
  public void test() {

    final long timeToLive = 2; //
    final long timerInterval = 1;//
    final int maxItems = 5;

    final InMemoryCache<Integer, String> cache;
    cache = new InMemoryCache<Integer, String>(timeToLive, timerInterval, maxItems);

    // "0"..."9" Sting to the cache
    Arrays.asList(0, 1, 2, 3, 4, 5, 7, 8, 9).forEach(w -> cache.put(w, String.valueOf(w)));

    Assert.assertTrue(null == cache.get(0));
    Assert.assertTrue(null == cache.get(3));
    Assert.assertTrue("4".equals(cache.get(4)));
    Assert.assertTrue("9".equals(cache.get(9)));

    final long lastAccessed = System.currentTimeMillis();

    while (System.currentTimeMillis() < (lastAccessed + (timeToLive * 1000))) {
      try {
        LOG.info("waiting...");
        Thread.sleep(timeToLive * 2 * 1000);
      } catch (final InterruptedException e) {
        LOG.error(e.getLocalizedMessage(), e);
      }
      LOG.info("Going on...");
    }
    Assert.assertTrue(null == (cache.get(8)));
    Assert.assertTrue(null == (cache.get(9)));
    LOG.info("Done...");
  }
}

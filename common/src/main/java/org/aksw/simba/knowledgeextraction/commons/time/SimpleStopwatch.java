package org.aksw.simba.knowledgeextraction.commons.time;

import java.util.concurrent.TimeUnit;

public class SimpleStopwatch {

  protected long time = 0l;

  public void start() {
    time = System.nanoTime();
  }

  public void stop() {
    time = System.nanoTime() - time;
  }

  public long getTimeInSec() {
    return getTime(TimeUnit.SECONDS);
  }

  public long getTime(final TimeUnit u) {
    return u.convert(time, TimeUnit.NANOSECONDS);
  }
}

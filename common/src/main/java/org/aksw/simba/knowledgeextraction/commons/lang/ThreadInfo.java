package org.aksw.simba.knowledgeextraction.commons.lang;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class ThreadInfo {

  /**
   * Returns an estimate of the number of active threads in the current thread's
   * {@linkplain java.lang.ThreadGroup thread group} and its subgroups. Recursively iterates over
   * all subgroups in the current thread's thread group.
   *
   * <p>
   * The value returned is only an estimate because the number of threads may change dynamically
   * while this method traverses internal data structures, and might be affected by the presence of
   * certain system threads. This method is intended primarily for debugging and monitoring
   * purposes.
   *
   * @return an estimate of the number of active threads in the current thread's thread group and in
   *         any other thread group that has the current thread's thread group as an ancestor
   */
  public static int activeCount() {
    return Thread.activeCount();
  }

  /**
   * Returns the current number of live threads including both daemon and non-daemon threads.
   *
   * @return the current number of live threads.
   */
  public static int getThreadCount() {
    final ThreadMXBean bean = ManagementFactory.getThreadMXBean();
    return bean.getThreadCount();
  }

  /**
   * Retruns the number of free threads.
   *
   * @return {@link #getThreadCount()} - {@link #activeCount()}
   */
  public static int getFreeCount() {
    return ThreadInfo.getThreadCount() - ThreadInfo.activeCount();
  }

  /**
   *
   * @param args
   */
  public static void main(final String[] args) {
    System.out.println("active count: " + ThreadInfo.activeCount());
    System.out.println("thread count: " + ThreadInfo.getThreadCount());
  }
}

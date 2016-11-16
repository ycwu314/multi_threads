package org.ycwu.misc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 3 threads to print a, b, c repeatedly
 * 
 * @author ycwu
 *
 */
public class PrintABCV3 {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		ReentrantLock lock = new ReentrantLock();
		Condition condition_c2a = lock.newCondition();
		Condition condition_a2b = lock.newCondition();
		Condition condition_b2c = lock.newCondition();

		ExecutorService es = Executors.newFixedThreadPool(3);
		es.execute(new PrintTask('a', lock, condition_c2a, condition_a2b));
		// make sure the startup order
		Thread.sleep(100L);
		es.execute(new PrintTask('b', lock, condition_a2b, condition_b2c));
		Thread.sleep(100L);
		es.execute(new PrintTask('c', lock, condition_b2c, condition_c2a));
		es.shutdown();
	}

	static class PrintTask implements Runnable {
		char c;
		ReentrantLock lock;
		Condition prev;
		Condition next;

		public PrintTask(char c, ReentrantLock lock, Condition prev, Condition next) {
			super();
			this.c = c;
			this.lock = lock;
			this.prev = prev;
			this.next = next;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int i = 10;
			while (i > 0) {
				lock.lock();
				try {
					System.out.println(c);
					i--;
					// wake up next thread waiting on the
					next.signalAll();

					// check exit condition, otherwise deadlock in the end
					if (i > 0) {
						try {
							prev.await();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} finally {
					lock.unlock();
				}
			}

		}
	}

}

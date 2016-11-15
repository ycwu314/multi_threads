package org.ycwu.misc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 3 threads to print a, b, c repeatedly
 * 
 * @author ycwu
 *
 */
public class PrintABCV2 {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		ExecutorService es = Executors.newFixedThreadPool(3);
		Object lockA = new Object();
		Object lockB = new Object();
		Object lockC = new Object();

		es.execute(new PrintTask('a', lockC, lockA));
		Thread.sleep(100L);
		es.execute(new PrintTask('b', lockA, lockB));
		Thread.sleep(100L);
		es.execute(new PrintTask('c', lockB, lockC));

		es.shutdown();
	}

	static class PrintTask implements Runnable {
		char c;
		Object prev;
		Object me;

		public PrintTask(char c, Object prev, Object me) {
			super();
			this.c = c;
			this.prev = prev;
			this.me = me;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int i = 10;
			while (i > 0) {
				synchronized (prev) {
					synchronized (me) {
						System.out.println(c);
						me.notifyAll();
					}
					i--;
					// check i>0 to avoid last round to wait
					// otherwise causing deadlock
					if (i > 0) {
						try {
							prev.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}

			}

		}
	}

}

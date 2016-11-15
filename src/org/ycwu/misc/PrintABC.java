package org.ycwu.misc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 3 threads to print a, b, c repeatedly
 * @author ycwu
 *
 */
public class PrintABC {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		int[] next = { 0 };
		ExecutorService es = Executors.newFixedThreadPool(3);
		es.execute(new PrintTask('a', next));
		es.execute(new PrintTask('b', next));
		es.execute(new PrintTask('c', next));
		es.shutdown();
	}

	static class PrintTask implements Runnable {

		private char me;
		private int[] next;

		public PrintTask(char me, int[] next) {
			super();
			this.me = me;
			this.next = next;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				synchronized (next) {
					if (('a' + next[0]) == me) {
						System.out.println(me);
						next[0] = (next[0] + 1) % 3;
						next.notifyAll();
					}

					try {
						next.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

		}

	}

}

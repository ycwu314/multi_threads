package org.ycwu.misc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 启动3个线程打印递增的数字, 线程1先打印1,2,3,4,5, 然后是线程2打印6,7,8,9,10, 然后是线程3打印11,12,13,14,15.
 * 接着再由线程1打印16,17,18,19,20....以此类推, 直到打印到75.
 * 
 * @author ycwu
 *
 */
public class PrintNumbers {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Object lockOne = new Object();
		Object lockTwo = new Object();
		Object lockThree = new Object();

		ExecutorService es = Executors.newFixedThreadPool(3);

		es.execute(new PrintTask(1, 75, lockThree, lockOne));
		// add sleep to ensure startup order of threads 
		Thread.sleep(100L);
		es.execute(new PrintTask(6, 75, lockOne, lockTwo));
		Thread.sleep(100L);
		es.execute(new PrintTask(11, 75, lockTwo, lockThree));
		es.shutdown();
	}

	static class PrintTask implements Runnable {

		int start;
		int stop;
		Object prev;
		Object me;

		public PrintTask(int start, int stop, Object prev, Object me) {
			super();
			this.start = start;
			this.stop = stop;
			this.prev = prev;
			this.me = me;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int i = start;
			while (i <= stop) {
				synchronized (prev) {
					synchronized (me) {
						for (int j = i; j < i + 5; j++) {
							System.out.println(Thread.currentThread().getName() + ":" + j);
						}
						me.notifyAll();
					}

					i += 15;
					// check exit condition
					if (i < stop)
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

package org.ycwu.misc;

/**
 * requirement: thread A prints 1, then thread B prints 2, then thread A prints
 * 3, ...<br>
 * until 99, 100
 * 
 * @author ycwu
 *
 */
public class PrintOddEvenV2 {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Object lock = new Object();
		new Thread(new PrintOddTask(1, lock)).start();
		Thread.sleep(500L);
		new Thread(new PrintEvenTask(2, lock)).start();
	}

	static class PrintOddTask implements Runnable {

		int i;
		Object lock;

		public PrintOddTask(int i, Object lock) {
			super();
			this.i = i;
			this.lock = lock;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (i < 100) {
				synchronized (lock) {
					System.out.println(i);
					lock.notify();

					i += 2;
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
	}

	static class PrintEvenTask implements Runnable {

		int i;
		Object lock;

		public PrintEvenTask(int i, Object lock) {
			super();
			this.i = i;
			this.lock = lock;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (i <= 100) {
				synchronized (lock) {
					System.out.println(i);
					lock.notify();

					i += 2;
					// after print 100, i is 102, no need to wait any more
					if (i <= 100) {
						try {
							lock.wait();
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

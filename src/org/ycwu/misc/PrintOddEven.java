package org.ycwu.misc;

/**
 * requirement: thread A prints 1, then thread B prints 2, then thread A prints
 * 3, ...<br>
 * until 99, 100
 * 
 * @author ycwu
 *
 */
public class PrintOddEven {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Object lock = new Object();
		new Thread(new PrintEvenTask(1, lock)).start();
		// make sure that the PrintEven thread starts before PrintOdd
		Thread.sleep(1000L);
		new Thread(new PrintOddTask(1, lock)).start();
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
					if (i % 2 == 1) {
						System.out.println(i);
						lock.notify();
					} else {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					i++;
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
					if (i % 2 == 0) {
						System.out.println(i);
						lock.notify();
					} else {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					i++;
				}
			}
		}
	}
}

package org.ycwu.misc;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ObjectWaitNotify {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService es = Executors.newFixedThreadPool(2);
		Account account = new Account();
		account.setBalance(200);
		es.execute(new WithdrawTask(account));
		es.execute(new DepositTask(account));
		es.shutdown();
	}

	static class WithdrawTask implements Runnable {

		private Account account;
		private Random rand = new Random();

		public WithdrawTask(Account account) {
			super();
			this.account = account;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				synchronized (account) {
					int withdraw = rand.nextInt(100);

					// check the condition in a loop, when wake up, the
					// condition might not be satisfied
					while (account.getBalance() < withdraw) {
						System.out.printf("[[balance=%d, withdraw=%d]]\n", account.getBalance(), withdraw);
						try {
							account.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					account.setBalance(account.getBalance() - withdraw);
					System.out.printf("withdraw=-%d,\tcurrent balance=%d\n", withdraw, account.getBalance());
				}
			}

		}

	}

	static class DepositTask implements Runnable {

		private Account account;
		private Random rand = new Random();

		public DepositTask(Account account) {
			super();
			this.account = account;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				synchronized (account) {
					int deposit = rand.nextInt(100);
					account.setBalance(account.getBalance() + deposit);
					System.out.printf("deposit=+%d,\tcurrent balance=%d\n", deposit, account.getBalance());
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					account.notify();
				}
			}
		}

	}

	static class Account {
		private int balance;

		public int getBalance() {
			return balance;
		}

		public void setBalance(int balance) {
			this.balance = balance;
		}

	}

}

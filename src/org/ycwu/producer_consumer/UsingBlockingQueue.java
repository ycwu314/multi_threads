package org.ycwu.producer_consumer;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsingBlockingQueue {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService es = Executors.newFixedThreadPool(4);
		BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
		es.execute(new Producer(queue));
		es.execute(new Consumer(queue));
		es.execute(new Consumer(queue));
		es.execute(new Consumer(queue));
		es.execute(new Consumer(queue));
		es.shutdown();
	}

	static class Producer implements Runnable {
		private BlockingQueue<Integer> queue;
		private Random rand = new Random();

		public Producer(BlockingQueue<Integer> queue) {
			super();
			this.queue = queue;
		}

		@Override
		public void run() {
			while (true) {
				try {
					Integer num = rand.nextInt(100);
					queue.put(num);
					System.out.println("put " + num);
					Thread.sleep(500L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	static class Consumer implements Runnable {
		private BlockingQueue<Integer> queue;

		public Consumer(BlockingQueue<Integer> queue) {
			super();
			this.queue = queue;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {
					Integer num = queue.take();
					System.out.println("take " + num);
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}

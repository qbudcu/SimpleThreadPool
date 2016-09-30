import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class MyThreadPool<Job extends Runnable> implements ThreadPool<Job> {
	private int maxPoolSize = 10;
	private int corePoolSize = 5;
	private int workerNum = 5;
	private BlockingQueue<Job> jobs = new LinkedBlockingQueue<Job>();
	private List<Worker> workers = new LinkedList<Worker>();
	
	public MyThreadPool() {
		initThreadPool(corePoolSize);
	}
	
	private void initThreadPool(int num){
		for(int i=0;i<num;i++){
			Worker worker = new Worker();
			workers.add(worker);
			Thread t = new Thread(worker);
			t.start();
		}
	}
	@Override
	public void excute(Job job) {
		try {
			jobs.put(job);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		for(Worker worker:workers)
			worker.shutdown();
	}

	@Override
	public void addWorkers(int num) {
		synchronized(workers){
			num = num+workerNum>maxPoolSize?maxPoolSize:num+corePoolSize;
			initThreadPool(num);
			workerNum+=num;
		}
	}

	@Override
	public void removeWorkers(int num) {
		synchronized(workers){
			if (num >= workerNum)
				throw new IllegalArgumentException("beyond workNum");
			Iterator it = workers.iterator();
			int count = num;
			while(it.hasNext()){
				if(num==0)
					break;
				Worker worker = (Worker) it.next();
				worker.shutdown();
				num--;
			}
			workerNum-=count;
		}
	}

	@Override
	public int getJobSize() {
		return jobs.size();
	}
	
	class Worker implements Runnable{
		private volatile boolean flag = true;
		@Override
		public void run() {
			while(flag){
				try {
					Job job = jobs.take();
					job.run();
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
				
			}
		}
		
		public void shutdown(){
			flag = false;
		}
	}
}

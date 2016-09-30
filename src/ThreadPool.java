
public interface ThreadPool<Job extends Runnable>{
	//ִ��һ��Job�����Job��Ҫʵ��Runnable
	void excute(Job job);
	//�ر��̳߳�
	void shutdown();
	//���ӹ������߳�
	void addWorkers(int num);
	//���ٹ������߳�
	void removeWorkers(int num);
	//�õ�����ִ�е��߳�����
	int getJobSize();
}

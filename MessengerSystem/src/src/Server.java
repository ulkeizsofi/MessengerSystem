
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
	
	private int noMessages = 0;
	private static final int maxMessages = 100;
	private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<Message>(maxMessages);
	ReadWriteLock lock = new ReentrantReadWriteLock();
//	private final BlockingQueue<Timer> timerQueue = new ArrayBlockingQueue<Timer>(maxMessages);
	private final ConcurrentMap<Integer, Client> mClients = new ConcurrentHashMap<Integer, Client>();
	 
    /**
     * Adds given client to the server's client list.
     */
    public synchronized void addClient(Client client){
    	
        mClients.put(client.getID(), client);
    }
 
    /**
     * Deletes given client from the server's client list
     * if the client is in the list.
     */
    public synchronized void deleteClient(int id)
    {
        mClients.remove(id);
    }
	
	public Server() {
		//Send to Client
		new Thread(
				new Runnable() {
					
					@Override
					public void run() {
						
						try {
							while (true) {
								
								Message message = getNextFromQueue();
								if (message instanceof MessageQueues)
									sendToClient((MessageQueues)message);
								else {
									if (message instanceof MessageTopics) {
										
										sendToClient((MessageTopics)message);
									}
								}
							}
						}
						catch (Exception e) {
							// TODO: handle exception
							System.out.println("ERROR"+ e);
						}
					}
				}).start();
	}
	
	public Boolean receiveMessage(Message msg) throws InterruptedException {
		int noActualMessages;
		lock.readLock().lock();
	    try {
	    	
	        noActualMessages = noMessages;
	        
	    } finally {
	        lock.readLock().unlock();
	    }
	    if (noActualMessages < maxMessages - 1) {
	    	addMessageToQueue(msg);
	    	lock.writeLock().lock();
	        try {
	            noMessages++;
	            synchronized(messageQueue) {
	            	messageQueue.notify();
	            }
	       
	        } finally {
	            lock.writeLock().unlock();
	        }
	        return true;
	    }
	    else {
	    	return false;
	    }
	}
	
	private void addMessageToQueue(Message message) throws InterruptedException {
		
		messageQueue.add(message);
		if (message instanceof MessageQueues) {
			return;
		}
		if (message instanceof MessageTopics) {
		
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						System.out.println("START");
						Thread.sleep(0);
						synchronized(messageQueue) {
							if (messageQueue.remove(message)) {
								System.out.println("Out of time, message deleted: " + message);
							}
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}).start();
		}
//		if (message instanceof MessageQueues) {
//			return;
//		}
//		if (message instanceof MessageTopics) {
//			Timer timer = new Timer();
//	
//		    timer.cancel(); //this will cancel the current task. if there is no active task, nothing happens
//		    timer = new Timer();
//	
//		    TimerTask action = new TimerTask() {
//		        public void run() {
//		            synchronized(messageQueue) {
//		            	if (messageQueue.remove(message)) {
//		            		System.out.println("DELETED: "+ message);
//		            	}
//		            }
//		        }
//	
//		    };
//
//
//		    timer.schedule(action, ((MessageTopics)message).getTime()); //this starts the task
//	    	timerQueue.put(timer);
//		}
	}
	
	public void sendToClient(MessageQueues message) {
		synchronized(messageQueue) {
			
	
			Client client = mClients.get(message.getID());	
			if (client == null){
	//									      return map.put(key, value);
			} else{
				client.receiveMessage(message);
			}
			
		}
	}
	

	public void sendToClient(MessageTopics message) throws InterruptedException {
		
		broadcast(message);
	}
	
	public Message getNextFromQueue() throws InterruptedException{
		while (messageQueue.size()==0) {
			
	         synchronized(messageQueue) {
	        	 messageQueue.wait();
	         }
		}
		
	    Message message = (Message) messageQueue.element();
	    messageQueue.remove(message);
	    return message;
	}
	
	public void broadcast(MessageTopics message) throws InterruptedException {
		synchronized (mClients) {
			for(Client client : mClients.values()) {
				client.receiveMessage(message);
			}
		}
	}
	
}
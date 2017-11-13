package src;


import java.util.Map;
import java.util.Vector;
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
	
	private final ConcurrentMap<AtomicInteger, Client> mClients = new ConcurrentHashMap<AtomicInteger, Client>();
	 
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
								MessageQueues message = (MessageQueues) getNextFromQueue();
								Client client = mClients.get(message.getID());
								sendToClient(client, message);
							}
						}
						catch (Exception e) {
							// TODO: handle exception
						}
					}
				}).start();
	}
	
	public Boolean receiveMessage(Message msg) {
		int noActualMessages;
		lock.readLock().lock();
	    try {
	        noActualMessages = noMessages;
	    } finally {
	        lock.readLock().unlock();
	    }
	    if (noActualMessages < maxMessages - 1) {
	    	messageQueue.add(msg);
	    	lock.writeLock().lock();
	        try {
	            noMessages++;
	        } finally {
	            lock.writeLock().unlock();
	        }
	        return true;
	    }
	    else {
	    	return false;
	    }
	}
	
	public void sendToClient(Client client, Message message) {
		client.receiveMessage(message);
	}
	
	public Message getNextFromQueue() throws InterruptedException{
		while (messageQueue.size()==0)
	           wait();
	    Message message = (Message) messageQueue.element();
	    messageQueue.remove(message);
	    return message;
	}
	
}
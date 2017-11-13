

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Client{
	
	
//	private volatile boolean stop = false;
    private final BlockingQueue<MessageQueues> messageQueue = new ArrayBlockingQueue<MessageQueues>(100);
    private AtomicInteger id;
    private final Server server;
    private String topic = null;
    ReadWriteLock lock = new ReentrantReadWriteLock();

    public Client(int id, Server server) {
    	this.id = new AtomicInteger(id);
//      this.messageQueue = messageQueue;
    	this.server = server;
    	this.server.addClient(this);
    }
    
    public Client(int id, Server server, String topic) {
    	this.id = new AtomicInteger(id);
//      this.messageQueue = messageQueue;
    	this.server = server;
    	this.topic = topic;
    	this.server.addClient(this);
    }
    
    public int getID() {
    	return id.intValue();
    }
    
    public void receiveMessage(MessageQueues message) {
    	System.out.println("Received message: " + message );
    }
    
    public void receiveMessage(MessageTopics message) throws InterruptedException {
    	
    	String actualTopic = null;
    	lock.readLock().lock();
	    try {
	    	if (topic != null) {
	    		actualTopic = new String(topic);
	    
	    	}
	        
	    } finally {
	        lock.readLock().unlock();
	    }
	    
	    if (actualTopic == null) {
	    	return;
	    }
	    if (actualTopic.equals(message.getTopic())) {
	    	System.out.println("Received message" + message);
	    }
    }
    
    public void sendToServer(Message message) throws InterruptedException {
    	
    	server.receiveMessage(message);
    }
    
	
	public MessageQueues getNextFromQueue() throws InterruptedException{
		while (messageQueue.size()==0)
			synchronized(messageQueue) {
				messageQueue.wait();
			}  
	    MessageQueues message = (MessageQueues) messageQueue.element();
	    messageQueue.remove(message);
	    return message;
	}
	
	public String toString() {
		return "client " + id + " " + topic;
	}
}
 


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;


public class Client{
	
	
//	private volatile boolean stop = false;
    private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<Message>(100);
    private AtomicInteger id;
    private final Server server;
    private String topic = null;
    ReadWriteLock lock = new ReentrantReadWriteLock();

    public Client(int id, Server server) {
    	this.id = new AtomicInteger(id);
//      this.messageQueue = messageQueue;
    	this.server = server;
    	this.server.addClient(this);
    	startClient();
    }
    

    public Client(int id, Server server, String topic) {
    	this.id = new AtomicInteger(id);
//      this.messageQueue = messageQueue;
    	this.server = server;
    	this.topic = topic;
    	this.server.addClient(this);
    	startClient();
    }
    
    private void startClient() {
    	new Thread(new Runnable() {

    		@Override
    		public void run() {

    			try {
    				while(true) {
    					Message message = messageQueue.take();
    					
    					if (message instanceof DirectMessage) {
    						processMessage((DirectMessage)message);

    					}
    					else {
    						if (message instanceof TopicMessage) {
    							processMessage((TopicMessage)message);
    						}
    					}
    				}

    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    	}).start();
    }
    
    
    public int getID() {
    	return id.intValue();
    }
    
    public void receiveMessage(Message message) throws InterruptedException {
    
    	messageQueue.put(message);

    }
    
    public void processMessage(DirectMessage message) {
    	System.out.println("Received message: " + message );
    }
    
    public void processMessage(TopicMessage message) throws InterruptedException {
    	
//    	String actualTopic = null;
//    	lock.readLock().lock();
//	    try {
//	    	if (topic != null) {
//	    		actualTopic = new String(topic);
//	    
//	    	}
	        
//	    } finally {
//	        lock.readLock().unlock();
//	    }
//	    
//	    if (actualTopic == null) {
//	    	return;
//	    }
//	    if (actualTopic.equals(message.getTopic())) {
//	    	System.out.println("Received message" + message);
//	    }
    	
    	if (topic != null) {
    		if (topic.equals(message.getTopic())) {
    			System.out.println("Received message: " + message);
    		}
    	}
    }
    
    public void sendToServer(Message message) throws InterruptedException {
    	
    	server.receiveMessage(message);
    }
    
	
	public String toString() {
		return "client " + id + " " + topic;
	}
}
 
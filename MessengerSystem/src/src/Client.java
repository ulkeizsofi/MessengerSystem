package src;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class Client implements Runnable {
	
	
	private volatile boolean stop = false;
    private final BlockingQueue<MessageQueues> messageQueue = new ArrayBlockingQueue<MessageQueues>(100);
    private AtomicInteger id;
    private final Server server;

    public Client(int id, Server server) {
    	this.id = new AtomicInteger(id);
//      this.messageQueue = messageQueue;
      this.server = server;
    }
    
    public AtomicInteger getID() {
    	return id;
    }
    
    public void receiveMessage(Message message) {
    	System.out.println("Received message: " + message );
    }
    
    public void sendToServer(Message message) {
    	server.receiveMessage(message);
    }
    
    
    
	@Override
	public void run() {
		server.addClient(this);
		while (true) {
	        try {
	        	Message message = getNextFromQueue();
	        	System.out.println("Received: "+ message);
	        } catch (InterruptedException ex) {
	          
	        }
	      }
		
	}
	
	public MessageQueues getNextFromQueue() throws InterruptedException{
		while (messageQueue.size()==0)
	           wait();
	    MessageQueues message = (MessageQueues) messageQueue.element();
	    messageQueue.remove(message);
	    return message;
	}
}
 
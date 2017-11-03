import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class Client implements Runnable {
	
	
	private volatile boolean stop = false;
    private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<Message>(100);
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
    
    public void sendToServer(Message message) {
    	server.receiveMessage(message);
    }
    
    public void receiveMessage(Message message) {
    	System.out.println("Received message: " + message);
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
	
	public Message getNextFromQueue() throws InterruptedException{
		while (messageQueue.size()==0)
	           wait();
	    Message message = (Message) messageQueue.element();
	    messageQueue.remove(message);
	    return message;
	}
}
 


import java.util.concurrent.atomic.AtomicInteger;

public final class MessageQueues extends Message{
	
    private final AtomicInteger id;
    // A message to a client.
    
	public MessageQueues(String msg,int id) {
		 super(msg);
		 this.id = new AtomicInteger(id);
	    }
	
	    public int getID() {
	    	return id.intValue();
	    }
	    
	    public String toString() {
	    	return super.toString() + " " + id;
	    }
}
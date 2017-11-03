import java.util.concurrent.atomic.AtomicInteger;

public class Message {
    final String msg;
    final AtomicInteger id;

    // A message to a client.
    public Message(String msg, int id) {
      this.msg = msg;
      this.id = new AtomicInteger(id);
    }
    
    public String getMessage() {
    	return msg;
    }
    
    public AtomicInteger getID() {
    	return id;
    }

    public String toString() {
      return id + ": " + msg;
    }
}

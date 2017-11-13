import java.util.concurrent.atomic.AtomicInteger;

abstract  class Message{
    // A message to a client.
    abstract public String getMessage() ;
    abstract public String toString() ;
    abstract public AtomicInteger getTime();  
	abstract public String getType();
}
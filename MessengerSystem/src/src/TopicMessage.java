

import java.util.concurrent.atomic.AtomicInteger;

public final class TopicMessage extends Message{


		  private final String topic;
		  private final AtomicInteger time;
		  
		  public TopicMessage(String msg,String topic , int time)
		  {
			  super(msg);
			  this.topic  = topic;
			  this.time =new AtomicInteger(time);
			  
		  }
			  
		  public int getTime()
		  {
			  return this.time.intValue();
		  }
		  
		  public String getTopic() {
			  return this.topic;
		  }

		  public String toString() {
		      return super.toString() + " " + time + " " + topic;
		  }
}
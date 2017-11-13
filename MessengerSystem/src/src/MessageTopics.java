package src;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageTopics extends Message{


		  final String msg , topic , type;
		  final AtomicInteger time;
		  
		  public MessageTopic(String msg,String topic , int time)
		  {
			  this.msg  = msg ;
			  this.topic  = topic;
			  this.time =new AtomicInteger(time);
			  this.type="Topic";
			  
		  }

		  public String getMessage() {
		    	return msg;
		    }
			  
		  public AtomicInteger getTime()
		  {
			  return this.time;
		  }
		  
		  public String getType()
		  {
			  return this.type;
		  }

		  public String toString() {
		      return msg;
		    }
}
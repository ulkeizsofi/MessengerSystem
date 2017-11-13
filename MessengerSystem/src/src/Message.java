

abstract  class Message{
	private final String msg;
//	private final 
    // A message to a client.
	
	public Message(String msg) {
		 this.msg = msg;
	}
	
    public String getMessage() {
    	return msg;
    }

    public String toString() {
      return msg;
    }
}
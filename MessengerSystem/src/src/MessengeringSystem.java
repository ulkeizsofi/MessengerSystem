public class MessengeringSystem {
	public static void main(String argv[]) {
		
		try {
			Server server = new Server();
			Client cl1 = new Client(1, server, "TOPIC");
			Client cl2 = new Client(2, server, "TOPIC");
			cl2.sendToServer(new TopicMessage("NOT_TO_DELETE", "TOPIC", 0));
			cl2.sendToServer(new TopicMessage("HELLO3", "TOPIC", 10));
			cl1.sendToServer(new DirectMessage("HELLO", 2));
			cl2.sendToServer(new DirectMessage("HELLO2", 1));
			cl2.sendToServer(new TopicMessage("TO DELETE2", "TOPIC", 20));
			
		}
		catch (Exception e) {
			System.err.println("SOME UNRESOLVED ERROR HAPPENED: " + e);
		}
	}
}
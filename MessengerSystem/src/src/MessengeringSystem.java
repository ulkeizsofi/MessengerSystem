public class MessengeringSystem {
	public static void main(String argv[]) {
		System.out.println("HERE");
		try {
			Server server = new Server();
			Client cl1 = new Client(1, server, "TOPIC");
			Client cl2 = new Client(2, server, "TOPIC");
			cl2.sendToServer(new MessageTopics("HELLO3", "TOPIC", 10));
			cl1.sendToServer(new MessageQueues("HELLO", 2));
			cl2.sendToServer(new MessageQueues("HELLO2", 1));
			
		}
		catch (Exception e) {
			System.err.println("SOME UNRESOLVED ERROR HAPPENED: " + e);
		}
	}
}
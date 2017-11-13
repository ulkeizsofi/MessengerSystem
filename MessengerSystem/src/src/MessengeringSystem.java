package src;

public class MessengeringSystem {
	public static void main(String argv[]) {
		System.out.println("HERE");
		Server server = new Server();
		Client cl1 = new Client(1, server);
		Client cl2 = new Client(2, server);
		cl1.sendToServer(new MessageQueues("HELLO", 2,10));
		server.sendToClient(cl2, new MessageQueues("HELLO", 2,10));
		cl2.sendToServer(new MessageQueues("HELLO2", 1,10));
		server.sendToClient(cl1, new MessageQueues("HELLO2", 1,10));
		server.sendToClient(cl1, new MessageQueues("HELLO2", 3,10));
	}
}
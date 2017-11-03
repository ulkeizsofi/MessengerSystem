
public class MessengeringSystem {
	public static void main(String argv[]) {
		System.out.println("HERE");
		Server server = new Server();
		Client cl1 = new Client(1, server);
		Client cl2 = new Client(2, server);
		cl1.sendToServer(new Message("HELLO", 2));
	}
}

package server;

public class Server {

	public static void main(String[] args) {
		MirotServer ms = new MirotServer(9000);
		ms.start();
	}

}

package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 群聊的服务器端
 * @author liyujie
 *
 */
public class MirotServer extends Thread {
	private ServerSocket socket;
	private boolean state = false; // 运行状态
	/**
	 * 
	 * @param port Welcome Socket 的端口号
	 */
	public MirotServer(int port) {
		try {
			socket = new ServerSocket(port, 5, InetAddress.getLocalHost());
			System.out.println("Server run at addr: " + socket.getInetAddress().toString());
			System.out.println("Server run at port: " + String.valueOf(port));
			state = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		System.out.println("Server started...");
		
		while (state) {
			System.out.println("looping");
			try {
				Socket clientSocket = socket.accept();
				String clientAddress = clientSocket.getInetAddress().getHostAddress();
				
				System.out.println("New client connected...");
				System.out.println("Address: " + clientAddress);
				
				ChatHelper session = new ChatHelper(clientSocket, clientAddress);
				ChatManager.getInstance().add(clientAddress, session);
				
				new Thread(session).start();
				System.out.println("Thread started...");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
}

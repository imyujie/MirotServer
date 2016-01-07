package server;

import java.util.HashMap;

public class ChatManager {
	
	private static ChatManager manager = new ChatManager();
	private HashMap<String, ChatHelper> container;
	
	public static ChatManager getInstance() {
		return manager;
	}
	
	private ChatManager() {
		container = new HashMap<>();
	}
	
	public void add(String addr, ChatHelper session) {
		container.put(addr, session);
	}
	
	public ChatHelper get(String addr) {
		return container.get(addr);
	}
	
	public HashMap<String, ChatHelper> getAll() {
		return container;
	}
}

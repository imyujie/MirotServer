package server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;



public class ChatHelper implements Runnable {
	private Socket socket;
	public String address;
	private InputStream in;
	private OutputStream out;
	private boolean state; 
	private Header header;
	
	public ChatHelper(Socket socket, String addr) {
		this.socket = socket;
		this.address = addr;
		state = true;
	}
	
	private void parseHeader(BufferedInputStream bis) {
		header = new Header();
        int b;
        byte[] bytes = new byte[1024 * 2];
        int count = 0;

        try {
			while ((b = bis.read()) != -1) {
			    if (b == 13) {// cr
			        int nextByte = bis.read();
			        if (nextByte == 10) { //lf

			            if (count != 0) { // header
			                String headerLine = new String(bytes, "UTF-8");
			                System.out.println(headerLine.trim());
			                handleHeaderLine(headerLine.trim());
			                bytes = new byte[1024 * 2];
			                count = 0;
			            } else { // body
			                break;
			            }

			        } else {
			            bytes[count++] = (byte) b;
			        }
			    } else {
			        bytes[count++] = (byte) b;
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void handleHeaderLine(String line) {
		header.addHeaderLine(line);
	}
	
	private void handle() throws IOException {
		BufferedInputStream bis = new BufferedInputStream(in);
		
		while (state) {
			parseHeader(bis);
			System.out.println("Parse header finished....");
			
			sendBroadcast(header.getHeader().getBytes("UTF-8"));
			
			if (header.getContentType().equals("text")) {

                ArrayList<Byte> bytesList;
                bytesList = new ArrayList<>();

                int bt;
                bt = bis.read();
                while (bt != 13 && bt != -1) {
                    bytesList.add(new Byte((byte)bt));
                    bt = bis.read();
                }


                byte[] textBytes = new byte[bytesList.size()];

                for (int i = 0, len = bytesList.size(); i < len; i++) {
                    textBytes[i] = bytesList.get(i);
                }

                sendBroadcast(textBytes);
                sendBroadcast(13);
                
                
			} else {
				int length;
				long len = 0;
				byte[] writeBytes = new byte[1024];
				while ((length = bis.read(writeBytes)) != -1) {
					sendBroadcast(writeBytes, 0, length);
					len += length;
					if (len == header.getFileLength()) {
						break;
					}
				}
				System.out.println("file end!!");
			}
		}
	}
	
	public void sendBroadcast(byte[] bytes) {
		HashMap<String, ChatHelper> helpers = ChatManager.getInstance().getAll();
		Collection<ChatHelper> coll = helpers.values();
		for (Iterator iterator = coll.iterator(); iterator.hasNext();) {
			ChatHelper chatHelper = (ChatHelper) iterator.next();

			if (!chatHelper.address.trim().equals(this.address.trim())) {
				chatHelper.write(bytes);
			}
			
		}
	}
	
	public void sendBroadcast(byte[] bytes, int start, int offset) {
		HashMap<String, ChatHelper> helpers = ChatManager.getInstance().getAll();
		Collection<ChatHelper> coll = helpers.values();
		for (Iterator iterator = coll.iterator(); iterator.hasNext();) {
			ChatHelper chatHelper = (ChatHelper) iterator.next();

			if (!chatHelper.address.trim().equals(this.address.trim())) {
				chatHelper.write(bytes, start, offset);
			}
			
		}
	}
	
	public void sendBroadcast(int b) {
		HashMap<String, ChatHelper> helpers = ChatManager.getInstance().getAll();
		Collection<ChatHelper> coll = helpers.values();
		for (Iterator iterator = coll.iterator(); iterator.hasNext();) {
			ChatHelper chatHelper = (ChatHelper) iterator.next();

			if (!chatHelper.address.trim().equals(this.address.trim())) {
				chatHelper.write(b);
			}
			
		}
	}
	
	public void write(int bs) {
		try {
			out.write(bs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(byte[] bs) {
		try {
			out.write(bs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(byte[] bs, int start, int offset) {
		try {
			out.write(bs, start, offset);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			handle();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

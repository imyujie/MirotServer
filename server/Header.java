package server;

public class Header {
	public static final String CRLF = "\r\n";
    private String senderAddress;
    private String senderName;
    private String receiverName;
    private String receiverAddress;
    private String contentType;
    private String chatType;
    private String fileName;
    private int chatRoom;
    private long fileLength;

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getContentType() {
        return contentType;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setChatRoom(int chatRoom) {
        this.chatRoom = chatRoom;
    }

    public int getChatRoom() {
        return chatRoom;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getChatType() {
        return chatType;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void addHeaderLine(String line) {
        String[] lineKeyValue = line.split(":");
        String field = lineKeyValue[0].trim().toLowerCase();
        String value = lineKeyValue[1].trim();
        switch (field) {
            case "receiver-name":
                setReceiverName(value);
                break;
            case "receiver-address":
                setReceiverAddress(value);
                break;
            case "sender-name":
                setSenderName(value);
                break;
            case "sender-address":
                setSenderAddress(value);
                break;
            case "content-type":
                setContentType(value);
                break;
            case "file-length":
                setFileLength(Long.parseLong(value));
                break;
            case "chat-type":
                setChatType(value);
                break;
            case "chat-room":
                setChatRoom(Integer.parseInt(value));
                break;
            case "file-name":
                setFileName(value);
                break;
            default:
                break;
        }
    }
    
    public String getHeader() {
		String sn = "Sender-Name: " + getSenderName() + CRLF;
		String sa = "Sender-Address: " + getSenderAddress() + CRLF;
		String ct = "Content-Type: " + getContentType() + CRLF;
		String cht = "Chat-Type: " + getChatType() + CRLF;
		
		String header = sn + sa + ct + cht;
		
		if (getChatType().equals("group-chat")) {
			String cr = "Chat-Room: " + getChatRoom() + CRLF;
			header += cr;
		}
		
		if (!getChatType().equals("text")) {
			String fl = "File-Length: " +getFileLength() + CRLF;
			String fn = "File-Name: " +getFileName() + CRLF;
			header += fl + fn;
		}
		
		return header + CRLF;
	}
}


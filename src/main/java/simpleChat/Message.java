package simpleChat;

public class Message {
    private Header header;
    private String text;

    public Message() {
    }

    public Message(Header header) {
        this.header = header;
    }

    public Message(String text) {
        this.text = text;
    }

    public Message(Header header, String text) {
        this.header = header;
        this.text = text;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

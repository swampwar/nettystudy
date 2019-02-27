package simpleChat;

public class Header {
    private String command;

    public Header() {
    }

    public Header(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}

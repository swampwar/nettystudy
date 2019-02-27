package blocking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockingServer {

    public static void main(String[] args) throws IOException {
        BlockingServer server = new BlockingServer();
        server.run();
    }

    private void run() throws IOException {
        ServerSocket server = new ServerSocket(8888);
        System.out.println("접속 대기중");

        while(true){
            // 클라이언트와 연결되면 새로운 Socket을 생성한다.
            Socket sock = server.accept(); // blocking method
            System.out.println("클라이언트 연결됨.");

            OutputStream out = sock.getOutputStream();
            InputStream in = sock.getInputStream();

            while(true){
                try{
                    int request = in.read(); // blocking method
                    out.write(request); // blocking method
                } catch (IOException e){
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}

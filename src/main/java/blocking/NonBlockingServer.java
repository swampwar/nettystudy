package blocking;

import com.sun.security.ntlm.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class NonBlockingServer {
    private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<SocketChannel, List<byte[]>>();
    private ByteBuffer buffer = ByteBuffer.allocate(2*1024);

    private void startEchoServer(){
        try ( // 소괄호의 자원을 자동해제 (Closeable 구현)

                Selector selector = Selector.open(); // 자신에게 등록된 채널의 변경사항을 검사
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open() // 논블록킹 서버소켓채널 생성

        ) {

            if(serverSocketChannel.isOpen() && selector.isOpen()){ // 객체가 정상생성 되었으면
                serverSocketChannel.configureBlocking(false); // 논블럭킹 모드 설정(디폴트는 블럭킹)
                serverSocketChannel.bind(new InetSocketAddress(8888)); // 포트에 소켓을 바인딩

                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // Selector에 소켓과 감지할 이벤트를 등록
                System.out.println("접속 대기중");

                while(true){
                    selector.select(); // 등록된 소켓의 변경사항 검사(블록킹, 논블록킹 메서드는 selectNow())
                    System.out.println("Selector 변경사항 감지");
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                        SelectionKey key = (SelectionKey) keys.next();
                        while(keys.hasNext()){
                        keys.remove(); // 동일한 이벤트 감지를 제거

                        if(!key.isValid()){
                            System.out.println("key is invalid");
                            continue;
                        }

                        System.out.println("처리 고고");
                        if(key.isAcceptable()){ // I/O 이벤트의 종류가 연결요청
                            this.accept0p(key, selector);
                        } else if(key.isReadable()){ // 데이터 수신 요청
                            this.read0P(key);
                        } else if(key.isWritable()){ // 데이터 쓰기 요청
                            this.write0P(key);
                        }
                    }
                }
            }else {
                System.out.println("서버 소켓을 생성하지 못했습니다.");
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * ServerSocketChannel으로 연결요청이 왔을때 실행되는 메서드
     */
    private void accept0p(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverChannel.accept();
        socketChannel.configureBlocking(false);

        System.out.println("클라이언트 연결됨 : " + socketChannel.getRemoteAddress());

        keepDataTrack.put(socketChannel, new ArrayList<byte[]>());
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    /**
     * ServerSocketChannel으로 쓰기요청이 왔을때 실행되는 메서드
     */
    private void write0P(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        List<byte[]> channelData = keepDataTrack.get(socketChannel);
        Iterator<byte[]> its = channelData.iterator();

        while(its.hasNext()){
            byte[] it = its.next();
            its.remove();
            socketChannel.write(ByteBuffer.wrap(it));
        }

        key.interestOps(SelectionKey.OP_READ);
        System.out.println("write 요청 처리 완료");
    }

    /**
     * ServerSocketChannel으로 데이터 수신요청이 왔을때 실행되는 메서드
     */
    private void read0P(SelectionKey key) {
        try{
            SocketChannel socketChannel = (SocketChannel) key.channel();
            System.out.println("read0 실행 " + socketChannel.getRemoteAddress());
            buffer.clear();
            int numRead = -1;
            try{
                numRead = socketChannel.read(buffer);
            } catch (IOException e){
                System.err.println("데이터 읽기 에러발생");
            }

            if(numRead == -1){
                this.keepDataTrack.remove(socketChannel);
                System.out.println("클라이언트 연결 종료 : " + socketChannel.getRemoteAddress());
                socketChannel.close();
                key.cancel();
                return;
            }

            byte[] data = new byte[numRead];
            System.arraycopy(buffer.array(), 0, data, 0, numRead);
            System.out.println(new String(data, "UTF-8") + " from " + socketChannel.getRemoteAddress());

            doEchoJob(key, data);

        } catch (IOException e){
            System.err.println(e);
        }
    }

    private void doEchoJob(SelectionKey key, byte[] data) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        List<byte[]> channelData = keepDataTrack.get(socketChannel);
        channelData.add(data);

        key.interestOps(SelectionKey.OP_WRITE);
    }

    public static void main(String[] args) {
        NonBlockingServer server = new NonBlockingServer();
        server.startEchoServer();
    }
}

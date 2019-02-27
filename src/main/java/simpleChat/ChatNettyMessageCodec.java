package simpleChat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class ChatNettyMessageCodec extends MessageToMessageDecoder<String> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, String msg, List<Object> list) throws Exception {
        System.out.println("ChatNettyMessageCodec.decode()");

        String command = msg.substring(0, 2);
        String message = msg.substring(2, msg.length()-1) + "\n";
        Header header = new Header(command);
        Message addMsg = new Message(header, message);

        String dummyStr = msg.substring(msg.length()-1);
        System.out.println("command : '" +command+"'");
        System.out.println("msg : '" +message+"'");
        System.out.println("last char : '" +dummyStr+"'");

        list.add(addMsg);
    }
}

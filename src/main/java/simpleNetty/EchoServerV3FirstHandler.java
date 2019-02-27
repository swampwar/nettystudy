package simpleNetty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class EchoServerV3FirstHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf readMsg = (ByteBuf) msg;
        System.out.println("channelRead : " + readMsg.toString(Charset.defaultCharset()));

        ctx.write(msg);
    }
}

package simpleNetty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class EchoServerV4FirstHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf readMsg = (ByteBuf) msg;
        System.out.println("FirstHandler channelRead : " + readMsg.toString(Charset.defaultCharset()));
        ctx.write(msg);

        // 이벤트가 처리후 사라지므로 다시 발생시킨다.(다음 handler에서 처리되기 위해)
        ctx.fireChannelRead(msg);
    }
}

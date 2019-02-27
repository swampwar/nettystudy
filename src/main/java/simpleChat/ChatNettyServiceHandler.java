package simpleChat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.atomic.AtomicInteger;

@ChannelHandler.Sharable
public class ChatNettyServiceHandler extends ChannelInboundHandlerAdapter {

    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    final AttributeKey<Integer> id = AttributeKey.newInstance("id");
    private static final AtomicInteger count = new AtomicInteger(0);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ChatNettyServiceHandler.channelRegistered()");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ChatNettyServiceHandler.channelUnregistered()");

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ChatNettyServiceHandler.channelActive()");

        int value = count.incrementAndGet();
        ctx.channel().attr(id).set(value);
        ctx.writeAndFlush("your id : "+ String.valueOf(value) + "\n");

        channels.writeAndFlush(String.valueOf(value) + " join \n");
        channels.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ChatNettyServiceHandler.channelInactive()");

        ctx.channel().attr(id).remove();
        channels.remove(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ChatNettyServiceHandler.channelReadComplete()");

        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("ChatNettyServiceHandler.exceptionCaught()");
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
        System.out.println("ChatNettyServiceHandler.channelRead()");
        Message msg = (Message) object;

        if ("10".equals(msg.getHeader().getCommand())) {
            channels.writeAndFlush(ctx.channel().attr(id).get() + " :" + msg.getText());;
        } else if ("20".equals(msg.getHeader().getCommand())) {

            String text = msg.getText();
            String substring = text.substring(0, 2).trim();
            String message = text.substring(2, text.length());

            channels.stream().filter(i -> i.attr(id).get() == Integer.parseInt(substring))
                    .forEach(i -> i.writeAndFlush(i.attr(id).get() + " : " +message));

            ctx.writeAndFlush(ctx.channel().attr(id).get() + " : " + message);

        } else if("30".equals(msg.getHeader().getCommand())){
            ctx.disconnect();
        }

    }
}

package netty;

import example.telnet.TelnetServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ServerBootstrapTest {

    private static final int PORT = 8080;

    ServerBootstrap b = new ServerBootstrap();
    NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);

    @Before
    public void setUp(){
        b.group(bossGroup)
                .channel(NioServerSocketChannel.class) // 생성될 채널의 타입
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new LoggingHandler(LogLevel.INFO));
    }

    @Test
    public void serverBootstrapConfigTest() {
        try {
            ChannelFuture future = b.bind(PORT);
            future.addListeners(new ChannelFutureListener() { // bind 완료시점의 동작을 지정한다.
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    Channel ch = channelFuture.channel();
                    Assert.assertTrue(ch.isOpen());
                    Assert.assertTrue(ch.isWritable());
                    Assert.assertTrue(ch.isActive());
                    Assert.assertTrue(ch.isRegistered());

                    ch.close();
                }
            });

            future.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
        }
    }

    @Test
    public void serverBootstrapConfig_closeTest() {
        try {
            ChannelFuture bindFuture = b.bind(PORT).sync();
            bindFuture.channel().close().sync();

            NioServerSocketChannel ch = (NioServerSocketChannel) bindFuture.channel();
            Assert.assertFalse(ch.isOpen());
            Assert.assertFalse(ch.isWritable());
            Assert.assertTrue(ch.isActive()); // Channel close후에도 active
            Assert.assertTrue(ch.isRegistered()); // Channel close후에도 registered

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
        }
    }
}


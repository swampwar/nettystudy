package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DelimiterBasedFrameDecoderTest {
    @Test
    public void DelimiterBasedFrameDecoder(){
        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline()
                .addLast(new DelimiterBasedFrameDecoder(1000, true, Delimiters.lineDelimiter()));

        ByteBuf buf = Unpooled.copiedBuffer("Section1\r\nSection2\nSection3\r\n", CharsetUtil.UTF_8);
        channel.writeInbound(buf);

        ByteBuf outbuf1 = channel.readInbound();
        assertEquals("Section1", outbuf1.toString(CharsetUtil.UTF_8));

        ByteBuf outbuf2 = channel.readInbound();
        assertEquals("Section2", outbuf2.toString(CharsetUtil.UTF_8));

        ByteBuf outbuf3 = channel.readInbound();
        assertEquals("Section3", outbuf3.toString(CharsetUtil.UTF_8));

        assertNull(channel.readInbound()); // 더이상 읽을게 없다.

        channel.finish();
        outbuf1.release();
        outbuf2.release();
        outbuf3.release();
    }
}

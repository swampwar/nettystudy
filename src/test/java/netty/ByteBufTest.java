package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class ByteBufTest {

    @Test
    public void createUnpooledHeapBuffer(){
        ByteBuf buf = Unpooled.buffer(11);
        testBuffer(buf, false);
    }

    @Test
    public void createUnpooledDirectBuffer(){
        ByteBuf buf = Unpooled.directBuffer(11);
        testBuffer(buf, true);
    }

    @Test
    public void createPooledHeapBuffer(){
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(11);
        testBuffer(buf, false);
    }

    @Test
    public void createPooledDirectBuffer(){
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(11);
        testBuffer(buf, true);
    }

    @Test
    public void ByteBufRefereceCountUtil(){
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(11);
        buf.writeBytes("abc".getBytes());

        assertEquals(1, ReferenceCountUtil.refCnt(buf));

        ByteBuf bufSecond = buf.retain(); // 참조 메서드 수를 증가시킨다.
        assertEquals(2, ReferenceCountUtil.refCnt(buf));

        ReferenceCountUtil.release(buf); // 릴리즈를 시켜도 여전히 참조가 가능하다?
        System.out.println(ReferenceCountUtil.refCnt(buf));
        System.out.println(ReferenceCountUtil.refCnt(bufSecond));
    }

    private void testBuffer(ByteBuf buf, boolean isDirect){
        assertEquals(11, buf.capacity());

        assertEquals(isDirect, buf.isDirect());

        buf.writeInt(65537); // 4byte write
        assertEquals(4, buf.readableBytes());
        assertEquals(7, buf.writableBytes()); // 11byte - 4byte

        assertEquals(1, buf.readShort()); // 65537(0x00010001) -> 1(0x0001) 1byte
        assertEquals(2, buf.readableBytes()); // 4byte(int) - 2byte(short)
        assertEquals(7, buf.writableBytes());

        buf.clear();

        assertEquals(0, buf.readableBytes());
        assertEquals(11, buf.writableBytes());
    }
}

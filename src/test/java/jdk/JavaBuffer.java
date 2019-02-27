package jdk;

import org.junit.Test;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

import static org.junit.Assert.assertEquals;

public class JavaBuffer {
    @Test
    public void createBuffer() {
        // Buffer 생성 메서드 3개 : allocate(), allocateDirect(), wrap()
        CharBuffer heapBuffer = CharBuffer.allocate(11);
        assertEquals(11, heapBuffer.capacity());
        assertEquals(false, heapBuffer.isDirect());

        ByteBuffer directBuffer = ByteBuffer.allocateDirect(11);
        assertEquals(11, directBuffer.capacity());
        assertEquals(true, directBuffer.isDirect());

        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0};
        IntBuffer intHeapBuffer = IntBuffer.wrap(array);
        assertEquals(11, intHeapBuffer.capacity());
        assertEquals(false, intHeapBuffer.isDirect());
    }

    @Test
    public void ByteBufferPut() {
        ByteBuffer firstBuffer = ByteBuffer.allocate(11);
        System.out.println("바이트 버퍼 초기값 : " + firstBuffer);
        assertEquals(0, firstBuffer.position());

        byte[] source = "Hello world".getBytes();
        firstBuffer.put(source);
        System.out.println("11바이트 기록 후 : " + firstBuffer);
        assertEquals(11, firstBuffer.position());
    }

    @Test(expected = BufferOverflowException.class)
    public void ByteBufferPut2() {
        ByteBuffer buffer = ByteBuffer.allocate(11);
        byte[] source = "Hello World!".getBytes(); // capa를 넘는 길이의 배열

        for (byte b : source) {
            buffer.put(b);
            System.out.printf("buffer writing [%c] : " + buffer + "\n", b);
        }
    }

    @Test
    public void ByteBufferRead() {
        ByteBuffer buffer = ByteBuffer.allocate(11);
        System.out.println("초기상태 : " + buffer);

        buffer.put((byte) 1);
        buffer.put((byte) 2);
        assertEquals(2, buffer.position());

        buffer.rewind(); // position -> 0
        assertEquals(0, buffer.position());

        assertEquals(1, buffer.get()); // get() 수행시 position 1증가
        assertEquals(1, buffer.position());
    }

    @Test
    public void ByteBufferFlip() {
        ByteBuffer buffer = ByteBuffer.allocate(11);
        System.out.println("초기상태 : " + buffer);

        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        buffer.put((byte) 4);
        assertEquals(4, buffer.position());
        assertEquals(11, buffer.limit());

        buffer.flip(); // limit -> position, position -> 0

        assertEquals(0, buffer.position());
        assertEquals(4, buffer.limit());
    }

    @Test
    public void ByteBufferFlip2() {
        byte[] array = {1, 2, 3, 4, 5, 0, 0, 0, 0, 0, 0};

        ByteBuffer buffer = ByteBuffer.wrap(array);
        System.out.println("초기상태 : " + buffer);
        assertEquals(0, buffer.position());
        assertEquals(11, buffer.limit());

        assertEquals(1, buffer.get());
        assertEquals(2, buffer.get());
        assertEquals(3, buffer.get());
        assertEquals(4, buffer.get());

        assertEquals(4, buffer.position());
        assertEquals(11, buffer.limit());

        buffer.flip(); // limit -> position, position -> 0
        assertEquals(0, buffer.position());
        assertEquals(4, buffer.limit());

        assertEquals(1, buffer.get(0)); // position 변동없음
        assertEquals(2, buffer.get(1)); // position 변동없음
        assertEquals(1, buffer.get());

        assertEquals(1, buffer.position());
        assertEquals(4, buffer.limit());
    }

}

package dev.rama27.components;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientHandle {
    public SocketChannel socketChannel;
    public ByteBuffer readBuffer=ByteBuffer.allocate(1024);
    public ByteBuffer writeBuffer;
    public int id;

    public ClientHandle(SocketChannel socketChannel,int id){
        this.socketChannel=socketChannel;
        this.writeBuffer=ByteBuffer.allocate(1024);
        this.id=id;
    }

}

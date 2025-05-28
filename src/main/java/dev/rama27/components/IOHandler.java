package dev.rama27.components;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;

@Component
public class IOHandler {

    private  final CmdHandler cmdHandler;
    private final RespSerializer respSerializer;
    private int count=0;

    IOHandler(CmdHandler cmdHandler, RespSerializer respSerializer){
        this.respSerializer=respSerializer;
        this.cmdHandler = cmdHandler;
    }

    public void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverSocketChannel= (ServerSocketChannel) key.channel();
        SocketChannel socketChannel=(SocketChannel) serverSocketChannel.accept();

        socketChannel.configureBlocking(false);

        socketChannel.register(selector,SelectionKey.OP_READ);

    }

    public void handleRead(SelectionKey key, Selector selector) throws IOException {
        SocketChannel socketChannel=(SocketChannel) key.channel();
        ByteBuffer buffer= ByteBuffer.allocate(1024);
        ClientHandle clientHandle=new ClientHandle(socketChannel,count++);
        int bytesRead=-1;

        try{
            bytesRead = socketChannel.read(buffer);
                if (bytesRead > 0) {
                    // parse stuff here to strings i guess
                    List<String[]> res=respSerializer.deserialize(buffer.array());
                    for(String[] ss:res){
                        handleCmd(ss,clientHandle);
                    }
                }

        } catch (IOException e) {
            System.out.println("EXCEption at BytesReadBuffer in HanldeRead"+e.getMessage());
        }
        
        

    } private void handleCmd(String[] ss, ClientHandle clientHandle) throws IOException {
        ss[0] = ss[0].toLowerCase();
        String t = ss[0];
        String response = "";
        switch (t) {
            case "ping":
                response=cmdHandler.ping();
                break;
            case "echo":
                response=cmdHandler.echo(ss);
                break;
            case "get":
                response=cmdHandler.get(ss);

                break;
            case "set":
                response=cmdHandler.set(ss);
                break;
            case"config":
                response=cmdHandler.configCmd(ss);
                break;
        }
        clientHandle.writeBuffer=ByteBuffer.wrap(response.getBytes());
        clientHandle.socketChannel.write(clientHandle.writeBuffer);
        clientHandle.socketChannel.close();
    }
}

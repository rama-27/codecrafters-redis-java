package dev.rama27.components;

import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

@Component
public class NioServer {
    private final NioIOStreamsIG nioIOStreamsIG;

    NioServer(NioIOStreamsIG nioIOStreamsIG){
        this.nioIOStreamsIG = nioIOStreamsIG;
    }
    public static final int PORT =6379;

    public void startServer() throws IOException {
        System.out.println("NIO SERVER IS STARTING !!!");

        Selector selector=Selector.open();

        ServerSocketChannel serverSocketChannel= ServerSocketChannel.open();

        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(PORT));

        SelectionKey serverKey= serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);

        System.out.println("EVENT LOOP STARTING!! ");

        while(true){
            int readyChannels= selector.select();

            if(readyChannels==0){
                continue;
            }

            Set<SelectionKey> selectionKeys= selector.selectedKeys();
            Iterator<SelectionKey> selectionKeyIterator=selectionKeys.iterator();

            while(selectionKeyIterator.hasNext()){
                SelectionKey key=selectionKeyIterator.next();

                if(key.isAcceptable()){

                    nioIOStreamsIG.handleAccept(key,selector);
                   // handle key
                }
                else if(key.isReadable()){

                    nioIOStreamsIG.handleRead(key,selector);
                    // handle key
                }

                selectionKeyIterator.remove();

            }
            
        }

    }
}

package dev.rama27.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TcpServer {

    @Autowired
    private RespSerializer respSerializer;
    private CmdHandler cmdHandler;

    private ConcurrentHashMap<String,String> map;
    TcpServer(RespSerializer respSerializer, CmdHandler cmdHandler){
        this.respSerializer=respSerializer;
        this.cmdHandler=cmdHandler;
        this.map=new ConcurrentHashMap<>();
    }
    public  void startServer() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            int count = 0;

            while (true) {
                clientSocket = serverSocket.accept();
                Socket finalSocket = clientSocket;
                Client client = new Client(finalSocket,
                        finalSocket.getInputStream(),
                        finalSocket.getOutputStream(),
                        ++count);
                new Thread(() -> {
                    try {
                        handleClient(client);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }
        private  void handleClient (Client client) throws IOException {

            while (client.socket.isConnected()) {
                byte[] buffer = new byte[client.socket.getReceiveBufferSize()];
                int bytesRead = client.inputStream.read(buffer);

                if (bytesRead > 0) {
                    // parse stuff here to strings i guess
                    List<String[]> res=respSerializer.deserialize(buffer);
                    for(String[] ss:res){
                        handleCmd(ss,client);
                    }
                }
            }

        }

    private void handleCmd(String[] ss, Client client) throws IOException {
        ss[0] = ss[0].toLowerCase();
        String t = ss[0];
        String response = "";
        switch (t) {
            case "ping":
                response=CmdHandler.ping();
                break;
            case "echo":
                response=CmdHandler.echo(ss);
                break;
            case "get":
                response=CmdHandler.get(ss,map);

                break;
            case "set":
                 response=CmdHandler.set(ss,map);

                break;
        }
        client.outputStream.write(response.getBytes());
    }
}

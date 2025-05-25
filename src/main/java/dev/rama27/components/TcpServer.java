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

    private ConcurrentHashMap<String,String> map;
    TcpServer(RespSerializer respSerializer){
        this.respSerializer=respSerializer;
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
        String response;
        switch (t) {
            case "ping":
                client.outputStream.write("+PONG\r\n".getBytes());
                break;
            case "echo":
                response = "";
                for (String s : ss) {
                    if (s.equals("echo")) {
                        continue;
                    }
                    response += s;
                }
                response = "$" + response.length() + "\r\n" + response + "\r\n";
                client.outputStream.write(response.getBytes());
                System.out.println("sending the respone: " + response);
                break;
            case "get":
                if (!map.containsKey(ss[1])) {
                    client.outputStream.write("$-1\r\n".getBytes());
                }
                if (map.containsKey(ss[1])) {
                    response = "";
                    response += "$";
                    response += map.get(ss[1]).length();
                    response += "\r\n";
                    response += map.get(ss[1]);
                    response += "\r\n";
                    client.outputStream.write(response.getBytes());
                }
                break;
            case "set":
                map.put(ss[1], ss[2]);
                client.outputStream.write("+OK\r\n".getBytes());
                if (ss.length > 3) {
                    ss[3] = ss[3].toLowerCase();
                    if (ss[3].equals("px")) {
                    }
                }
                break;
        }
    }
}

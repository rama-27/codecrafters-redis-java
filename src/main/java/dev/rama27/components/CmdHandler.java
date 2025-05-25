package dev.rama27.components;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CmdHandler   {

    public static void ping(Client client) throws IOException {
        client.outputStream.write("+PONG\r\n".getBytes());
    }

    public static void echo(Client client,String[] ss) throws IOException {
        String response = "";
        for (String s : ss) {
            if (s.equals("echo")) {
                continue;
            }
            response += s;
        }
        response = "$" + response.length() + "\r\n" + response + "\r\n";
        client.outputStream.write(response.getBytes());
        System.out.println("sending the respone: " + response);
    }

    public static void get(Client client, String[] ss, ConcurrentHashMap<String, String> map) throws IOException {
        if (!map.containsKey(ss[1])) {
            client.outputStream.write("$-1\r\n".getBytes());
        }
        if (map.containsKey(ss[1])) {
            String response = "";
            response += "$";
            response += map.get(ss[1]).length();
            response += "\r\n";
            response += map.get(ss[1]);
            response += "\r\n";
            client.outputStream.write(response.getBytes());
        }
    }

    public static void set(Client client, String[] ss,
                           ConcurrentHashMap<String, String> map) throws IOException {
        map.put(ss[1], ss[2]);
        client.outputStream.write("+OK\r\n".getBytes());
        if (ss.length > 3) {
            ss[3] = ss[3].toLowerCase();
            if (ss[3].equals("px")) {
            }
        }
    }
}

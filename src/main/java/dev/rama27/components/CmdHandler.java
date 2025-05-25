package dev.rama27.components;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CmdHandler   {

    public static String ping() throws IOException {
        return "+PONG\r\n";
    }

    public static String echo(  String[] ss) throws IOException {
        String response = "";
        for (String s : ss) {
            if (s.equals("echo")) {
                continue;
            }
            response += s;
        }
        response = "$" + response.length() + "\r\n" + response + "\r\n";
        System.out.println("sending the respone: " + response);
        return response;
    }

    public static String get(  String[] ss, ConcurrentHashMap<String, String> map) throws IOException {
        if (!map.containsKey(ss[1])) {

            return "$-1\r\n";
        }
        else  {
            String response = "";
            response += "$";
            response += map.get(ss[1]).length();
            response += "\r\n";
            response += map.get(ss[1]);
            response += "\r\n";
            return response;
        }
    }

    public static String set(  String[] ss,
                           ConcurrentHashMap<String, String> map) throws IOException {
        map.put(ss[1], ss[2]);
        return "+OK\r\n";
    }
}

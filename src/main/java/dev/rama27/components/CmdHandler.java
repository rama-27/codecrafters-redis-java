package dev.rama27.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CmdHandler   {


    Hashh hashh;
    RespSerializer respSerializer;

    CmdHandler(Hashh hashh,RespSerializer respSerializer){
        this.hashh=hashh;
        this.respSerializer=respSerializer;
    }
    public  String ping()  {
        return "+PONG\r\n";
    }

    public  String echo(  String[] ss)   {
        StringBuilder response = new StringBuilder();
        for (String s : ss) {
            if (s.equals("echo")) {
                continue;
            }
            response.append(s);
        }
        response = new StringBuilder("$" + response.length() + "\r\n" + response + "\r\n");
        return response.toString();
    }

    public  String get(  String[] ss) {

        if(hashh.getValue(ss[1]).equals("null")){
            return "$-1\r\n";
        }
        else{

            return respSerializer.serialize(hashh.getValue(ss[1]));
        }

    }

    public  String set(  String[] ss)   {
        if(ss.length==3) {
            String s = hashh.setValue(ss[1], ss[2]);
            return "+" + s + "\r\n";
        }
        else{
            String s= hashh.setValue(ss[1],ss[2], Integer.parseInt(ss[4]));
            return "+" + s + "\r\n";
        }
    }
}

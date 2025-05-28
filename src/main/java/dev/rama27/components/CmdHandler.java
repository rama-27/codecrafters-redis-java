package dev.rama27.components;

import org.springframework.stereotype.Component;


@Component
public class CmdHandler   {


    Hashh hashh;
    RespSerializer respSerializer;
    DataMaps dataMaps;

    CmdHandler(Hashh hashh,RespSerializer respSerializer, DataMaps dataMaps){
        this.hashh=hashh;
        this.respSerializer=respSerializer;
        this.dataMaps=dataMaps;
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

    public String configCmd(String[] ss) {

        ss[1]=ss[1].toLowerCase();
        ss[2]=ss[2].toLowerCase();
        if(ss[1].equals("get")){
            if(ss[2].equals("dir")){
                String data= dataMaps.getConfigMap.get("dir");
                String[] res={"dir",data};
                return respSerializer.serialize(res);
            }
        }


//        try{
//            Path filePath= Paths.get(ss[1]+"/"+ ss[3]);
//            Files.deleteIfExists(filePath);
//            Files.createDirectories(Path.of(ss[1]));
//             Files.createFile(filePath );
//            Process process= Runtime.getRuntime().exec("chmod 777 "+filePath);
////             Files.setAttribute(filePath, "dos:write",true);
//            System.out.println("File created ");
//        }
//        catch (IOException ie){
//            System.err.println("dirFlag() FILE "+ie.getMessage());
//        }
        return null;
    }



}

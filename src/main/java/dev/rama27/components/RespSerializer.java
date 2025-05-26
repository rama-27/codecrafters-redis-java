package dev.rama27.components;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class RespSerializer {

    public  String serialize(String s){
        String response = "$" +
                s.length() +
                "\r\n" +
                s +
                "\r\n";
        return response;
    }


    public List<String[]> deserialize(byte[] command){
        String data= new String(command, StandardCharsets.UTF_8);
        char[] dataArr=data.toCharArray();

        List<String[]> response=new ArrayList<>();

        int idx=0;

        while(idx<dataArr.length){
            char curr=dataArr[idx];
            if(curr=='\u0000'){
                break;
            }
            if(curr=='*'){
                String arrLen="";
                idx++;
                while(idx< dataArr.length && Character.isDigit(dataArr[idx])){
                    arrLen+=dataArr[idx++];
                }
                idx+=2;

                if(dataArr[idx]=='*'){
                    for(int i=0;i<Integer.parseInt(arrLen);i++){
                        String nextedLen="";
                        idx++;
                        while(idx<dataArr.length && Character.isDigit(dataArr[idx])){
                            nextedLen+=dataArr[idx++];
                        }
                        idx+=2;
                        String[] subArray=new String[Integer.parseInt(nextedLen)];
                        idx= getParts(dataArr,idx,subArray);
                        response.add(subArray);
                    }
                }
                else{
                    String[] subArray=new String[Integer.parseInt(arrLen)];
                    idx= getParts(dataArr,idx,subArray);
                    response.add(subArray);

                }
            }
        }
        return response;
    }
    public int getParts(char[] dataArr, int idx, String[] subArray){

        int j=0;
        while(idx<dataArr.length && j<subArray.length){
            if(dataArr[idx]== '$'){
                idx++;
                String partLen="";
                while(idx<dataArr.length && Character.isDigit(dataArr[idx])){
                    partLen+=dataArr[idx++];
                }
                idx+=2;
                String part= "";
                for(int k=0;k<Integer.parseInt(partLen);k++){
                    part+=dataArr[idx++];
                }
                idx+=2;
                subArray[j++]=part;
            }
        }

        return idx;
    }

}

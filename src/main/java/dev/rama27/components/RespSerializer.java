package dev.rama27.components;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class RespSerializer {

    public  String serialize(String s){
        return "$" + s.length() + "\r\n" + s + "\r\n";
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
                StringBuilder arrLen= new StringBuilder();
                idx++;
                while(idx< dataArr.length && Character.isDigit(dataArr[idx])){
                    arrLen.append(dataArr[idx++]);
                }
                idx+=2;

                if(dataArr[idx]=='*'){
                    for(int i = 0; i<Integer.parseInt(arrLen.toString()); i++){
                        StringBuilder nextedLen= new StringBuilder();
                        idx++;
                        while(idx<dataArr.length && Character.isDigit(dataArr[idx])){
                            nextedLen.append(dataArr[idx++]);
                        }
                        idx+=2;
                        String[] subArray=new String[Integer.parseInt(nextedLen.toString())];
                        idx= getParts(dataArr,idx,subArray);
                        response.add(subArray);
                    }
                }
                else{
                    String[] subArray=new String[Integer.parseInt(arrLen.toString())];
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
                StringBuilder partLen= new StringBuilder();
                while(idx<dataArr.length && Character.isDigit(dataArr[idx])){
                    partLen.append(dataArr[idx++]);
                }
                idx+=2;
                StringBuilder part= new StringBuilder();
                for(int k = 0; k<Integer.parseInt(partLen.toString()); k++){
                    part.append(dataArr[idx++]);
                }
                idx+=2;
                subArray[j++]= part.toString();
            }
        }

        return idx;
    }

}

package dev.rama27.components;

import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Hashh {
    public  ConcurrentHashMap<String, Value> map;

    Hashh(){
        map=new ConcurrentHashMap<>();
    }

    public  String getValue(String key){
        Value val=map.get(key);
        if(val!=null){
            if(LocalTime.now().isAfter(val.expires)){
                remove(key);
                return "null";
            }
            return val.value;
        }
        return "null";
    }

    private  void remove(String key) {
        map.remove(key);
    }

    public  String setValue(String key,String value){
        Value val=new Value(value, LocalTime.now(),LocalTime.now().plusSeconds(10));
        map.put(key,val);
        return "OK";
    }

}

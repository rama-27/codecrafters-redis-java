package dev.rama27.components;

import io.micrometer.observation.Observation;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataMaps {
    public ConcurrentHashMap<String,String> getConfigMap;

    public DataMaps(){
        this.getConfigMap=new ConcurrentHashMap<>();
    }

}

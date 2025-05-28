package dev.rama27.components;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.val;

import java.time.LocalTime;

public class Value {
    public String value;
    public LocalTime createdAt;
    public LocalTime expires;
    Value(){

    }
    Value(String value, LocalTime createdAt, LocalTime expires){
        this.value=value;
        this.createdAt=createdAt;
        this.expires=expires;
    }
}

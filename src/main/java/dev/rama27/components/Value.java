package dev.rama27.components;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
public class Value {
    public String value;
    public LocalTime createdAt;
    public LocalTime expires;
}

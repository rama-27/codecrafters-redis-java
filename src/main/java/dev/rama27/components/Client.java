package dev.rama27.components;

import lombok.AllArgsConstructor;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

@AllArgsConstructor
public class Client {
    public Socket socket;
    public InputStream inputStream;
    public OutputStream outputStream;
    
    public int id;
    
}

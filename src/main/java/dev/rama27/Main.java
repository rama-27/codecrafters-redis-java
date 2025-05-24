package dev.rama27;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args){
    System.out.println("Logs from your program will appear here!");

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        try {
          serverSocket = new ServerSocket(port);
          serverSocket.setReuseAddress(true);

          while(true){
              clientSocket = serverSocket.accept();
              Socket finalSocket=clientSocket;
              new Thread(()-> handleClient(finalSocket)).start();
          }

        } catch (IOException e) {
          System.out.println("IOException: " + e.getMessage());
        } finally {
          try {
            if (clientSocket != null) {
              clientSocket.close();
            }
          } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
          }
        }
  }
  private static void handleClient(Socket finalSocket){
          try (BufferedReader br=new BufferedReader(
                  new InputStreamReader(finalSocket.getInputStream())
          );
               OutputStream os = finalSocket.getOutputStream();
          ) {
              String msg;
              while((msg=br.readLine())!=null){
                  System.out.println("RECEIVED MSG: "+msg);
                  if(msg.equals("PING")){
                      os.write("+PONG\r\n".getBytes());
                  }
                  if(msg.equals("ECHO")){
                      String respHeader=br.readLine();
                      String respBody=br.readLine();
                      String response= respHeader+"\r\n"+respBody+"\r\n";
                      os.write(response.getBytes());
                  }
              }
          } catch (IOException e) {
              System.out.println("client exited i guess"+e.getMessage());
          }
          finally{
              try{
                  finalSocket.close();
                  System.out.println("client disconnected");
              }

              catch (IOException ie){
                  System.err.println("IO EXCEPTION WHEN CLOSING : "+ ie.getMessage());
              }
          }

  }
}

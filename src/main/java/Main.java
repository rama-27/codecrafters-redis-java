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
      while(true) {
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
              }
          } catch (IOException e) {
              throw new RuntimeException(e);
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
}

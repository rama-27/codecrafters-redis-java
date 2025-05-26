package dev.rama27;
import dev.rama27.components.TcpServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
  public static void main(String[] args){
    System.out.println("Logs from your program will appear here!");
      AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(Appconfig.class);
      TcpServer app=context.getBean(TcpServer.class);
      app.startServer();
        
  }

}

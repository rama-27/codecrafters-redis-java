package dev.rama27;
import dev.rama27.components.DataMaps;
import dev.rama27.components.NioServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {

    System.out.println("Logs from your program will appear here!");
    AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(Appconfig.class);
    NioServer app=context.getBean(NioServer.class);
    final DataMaps dataMaps=context.getBean(DataMaps.class);

    for(int i=0;i<args.length;i++){
      if(args[i].startsWith("--")){
        dataMaps.getConfigMap.put(args[i].substring(2),args[i+1]);
        i++;
      }
    }
    app.startServer();

  }

}

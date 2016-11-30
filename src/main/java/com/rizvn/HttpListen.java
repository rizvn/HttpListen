package com.rizvn;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class HttpListen
{

 static class Props
 {
   String context;
   Integer port;
   File outputDir;
 }

  public static void main(String[] args) throws Exception
  {
    Props props = readProps();
    HttpServer server = HttpServer.create(new InetSocketAddress(props.port), 0);
    server.createContext("/"+ props.context, new RequestHandler(props));
    server.setExecutor(Executors.newFixedThreadPool(50)); // creates a default executor
    server.start();
  }

  public static Props readProps()
  {
    Props props = new Props();
    props.context =  "receive";
    props.port = 9000;
    props.outputDir = new File("out");

    if(!props.outputDir.exists())
    {
      props.outputDir.mkdirs();
    }

    return props;
  }
}
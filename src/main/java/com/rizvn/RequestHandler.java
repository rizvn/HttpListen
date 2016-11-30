package com.rizvn;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Created by Riz
 */
public class RequestHandler implements HttpHandler
{
  HttpListen.Props props;

  public RequestHandler(HttpListen.Props props)
  {
    this.props = props;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException
  {
    if(exchange.getRequestMethod().equals("POST"))
    {
      try {
        String fileName = UUID.randomUUID().toString();
        File outFile = new File(props.outputDir, fileName);

        try (InputStream in = exchange.getRequestBody();
             OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile))) {
          transferInputToOutput(in, out);
          respondWith(exchange, 200, "SUCCESS");
        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        respondWith(exchange, 500, "Internal server error");
      }
    }
    else
    {
      respondWith(exchange, 405, "Method not allowed");
    }
  }

  public void respondWith(HttpExchange exchange, int code, String content)
  {
    try
    {
      exchange.sendResponseHeaders(code, content.length());
      try (OutputStream os = exchange.getResponseBody())
      {
        os.write(content.getBytes(StandardCharsets.UTF_8));
      }
    }
    catch (Exception ex)
    {
      throw new IllegalStateException(ex);
    }
  }

  public void transferInputToOutput(InputStream is, OutputStream os)
  {
    try
    {
      byte[] buffer = new byte[10 * 1024];

      for (int length; (length = is.read(buffer)) != -1; )
      {
        os.write(buffer, 0, length);
      }
    }
    catch (Exception ex)
    {
      throw new IllegalStateException(ex);
    }
  }
}
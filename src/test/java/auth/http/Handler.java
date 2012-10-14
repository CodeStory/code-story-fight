package auth.http;

import java.io.IOException;
import java.net.URL;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler {

  @Override
  protected URLConnection openConnection(URL url) throws IOException {
    return new URLConnection(url);
  }

}
package auth.http;

import java.net.URL;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler {
  @Override
  protected ClassPathURLConnection openConnection(URL url) {
    return new ClassPathURLConnection(url);
  }
}
package auth.http;

import auth.AuthenticatorTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.parseBoolean;
import static java.lang.System.getProperty;

public class ClassPathURLConnection extends java.net.HttpURLConnection {

  protected ClassPathURLConnection(URL url) {
    super(url);
  }

  @Override
  public void connect() throws IOException {
    connected = true;
  }

  @Override
  public void disconnect() {
    connected = false;
  }

  @Override
  public boolean usingProxy() {
    return false;
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    return new ByteArrayOutputStream();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    if (!connected) {
      connect();
    }
    return this.getClass().getResourceAsStream(url.getPath());
  }

  @Override
  public int getResponseCode() throws IOException {
    Boolean fail = parseBoolean(getProperty(AuthenticatorTest.UNAUTHORIZED, FALSE.toString()));
    if (fail) {
      return HTTP_UNAUTHORIZED;
    }
    return HTTP_OK;
  }
}
package auth.http;

import auth.AuthenticatorTest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.parseBoolean;
import static java.lang.System.getProperty;

public class ClassPathURLConnection extends HttpURLConnection {
  protected ClassPathURLConnection(URL url) {
    super(url);
  }

  @Override
  public void connect() {
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
  public OutputStream getOutputStream() {
    return new ByteArrayOutputStream();
  }

  @Override
  public InputStream getInputStream() {
    if (!connected) {
      connect();
    }
    return this.getClass().getResourceAsStream(url.getPath());
  }

  @Override
  public int getResponseCode() {
    boolean fail = parseBoolean(getProperty(AuthenticatorTest.UNAUTHORIZED, FALSE.toString()));

    return fail ? HTTP_UNAUTHORIZED : HTTP_OK;
  }
}
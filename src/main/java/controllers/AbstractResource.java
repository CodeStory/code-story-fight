package controllers;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import com.sun.jersey.api.NotFoundException;

import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

abstract class AbstractResource {
  private static final long ONE_MONTH = 1000L * 3600 * 24 * 30;

  protected Response seeOther(URI uri) {
    return Response.seeOther(uri).build();
  }

  protected Response seeOther(String url) {
    return seeOther(URI.create(url));
  }

  protected Response ok(Object entity, long modified) {
    return Response.ok(entity).lastModified(new Date(modified)).expires(new Date(modified + ONE_MONTH)).build();
  }

  protected Response ok(Object entity, String mimeType, long modified) {
    return Response.ok(entity, mimeType).lastModified(new Date(modified)).expires(new Date(modified + ONE_MONTH)).build();
  }

  protected Response ok(Object entity) {
    return Response.ok(entity).build();
  }

  protected String read(String path) {
    return read(file(path));
  }

  protected String read(File file) {
    try {
      return Files.toString(file, Charsets.UTF_8);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  protected File file(String path) {
    if (path.endsWith("/")) {
      throw new NotFoundException();
    }

    // Remove version
    path = path.replaceFirst("version-[^/]*/", "");

    try {
      File root = new File("web");
      File file = new File(root, path);
      if (!file.exists() || !file.getCanonicalPath().startsWith(root.getCanonicalPath())) {
        throw new NotFoundException();
      }

      return file;
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }
}

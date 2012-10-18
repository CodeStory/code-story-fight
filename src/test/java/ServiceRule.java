/*
 * This file is part of TestExpert.
 *
 * Copyright (C) 2012
 * "David Gageot" <david@gageot.net>,
 *
 * TestExpert is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TestExpert is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TestExpert. If not, see <http://www.gnu.org/licenses/>.
 */

import com.google.inject.Module;
import org.junit.rules.ExternalResource;

import java.util.Random;

/**
 * JUnit Rule to start/stop a service before/after tests.
 */
public class ServiceRule<T> extends ExternalResource {
  private static final int TRY_COUNT = 10;
  private static final int DEFAULT_PORT = 8183;

  private final Class<T> serviceClass;
  private final Module[] modules;
  private final Random random = new Random();
  private T service;
  private int port;

  private ServiceRule(Class<T> serviceClass, Module... modules) {
    this.serviceClass = serviceClass;
    this.modules = modules;
  }

  public static <T> ServiceRule<T> startWithRandomPort(Class<T> serviceClass, Module... modules) {
    return new ServiceRule<T>(serviceClass, modules);
  }

  @Override
  protected void before() {
    for (int i = 0; i < TRY_COUNT; i++) {
      try {
        port = randomPort();
        service = createService();
        service.getClass().getMethod("start", int.class).invoke(service, port);
        return;
      } catch (Exception e) {
        System.err.println("Unable to bind server: " + e);
      }
    }
    throw new IllegalStateException("Unable to start server");
  }

  @Override
  protected void after() {
    if (service != null) {
      try {
        service.getClass().getMethod("stop").invoke(service);
      } catch (Exception e) {
        System.err.println("Unable to stop server: " + e);
      }
    }
  }

  private T createService() throws Exception {
    try {
      return serviceClass.getDeclaredConstructor(Module[].class).newInstance(new Object[] {modules});
    } catch (Exception e) {
      return serviceClass.getDeclaredConstructor().newInstance();
    }
  }

  public T service() {
    return service;
  }

  public int getPort() {
    return port;
  }

  private synchronized int randomPort() {
    return DEFAULT_PORT + random.nextInt(1000);
  }
}

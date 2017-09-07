package com.gzoltar.maven.messaging;

import java.util.HashMap;
import java.util.Map;
import com.gzoltar.core.messaging.Service;
import com.gzoltar.core.messaging.Service.ServiceFactory;
import com.gzoltar.maven.AbstractGZoltarMojo;

public class ServiceHandlerFactory implements ServiceFactory {

  private Map<String, ServiceHandler> services = new HashMap<String, ServiceHandler>();

  private final AbstractGZoltarMojo mojo;

  public ServiceHandlerFactory(AbstractGZoltarMojo mojo) {
    this.mojo = mojo;
  }

  @Override
  public Service create(String id) {
    ServiceHandler sh = services.get(id);

    if (sh == null) {
      sh = new ServiceHandler(mojo);
      services.put(id, sh);
    }

    return sh;
  }

}

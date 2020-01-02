/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
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

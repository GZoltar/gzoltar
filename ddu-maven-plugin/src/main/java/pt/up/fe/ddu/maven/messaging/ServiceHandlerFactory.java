package pt.up.fe.ddu.maven.messaging;

import java.util.HashMap;
import java.util.Map;

import pt.up.fe.ddu.base.messaging.Service;
import pt.up.fe.ddu.base.messaging.Service.ServiceFactory;
import pt.up.fe.ddu.maven.AbstractDDUMojo;

public class ServiceHandlerFactory implements ServiceFactory {

	private Map<String, ServiceHandler> services = new HashMap<String, ServiceHandler>();
	
	private final AbstractDDUMojo mojo;
	
	public ServiceHandlerFactory(AbstractDDUMojo mojo) {
		this.mojo = mojo;
	}
	
	@Override
	public Service create(String id) {
		ServiceHandler sh = services.get(id);
		
		if(sh == null) {
			sh = new ServiceHandler(mojo);
			services.put(id, sh);
		}
		
		return sh;
	}

}

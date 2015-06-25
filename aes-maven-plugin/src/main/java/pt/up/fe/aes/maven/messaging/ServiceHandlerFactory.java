package pt.up.fe.aes.maven.messaging;

import java.util.HashMap;
import java.util.Map;

import pt.up.fe.aes.base.messaging.Service;
import pt.up.fe.aes.base.messaging.Service.ServiceFactory;
import pt.up.fe.aes.maven.AbstractAESMojo;

public class ServiceHandlerFactory implements ServiceFactory {

	private Map<String, ServiceHandler> services = new HashMap<String, ServiceHandler>();
	
	private final AbstractAESMojo mojo;
	
	public ServiceHandlerFactory(AbstractAESMojo mojo) {
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

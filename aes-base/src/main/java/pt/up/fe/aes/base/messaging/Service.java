package pt.up.fe.aes.base.messaging;

import pt.up.fe.aes.base.events.EventListener;

public interface Service {

	EventListener getEventListener ();
    void interrupted ();
    void terminated ();
    
    public interface ServiceFactory {
    	Service create (String id);
    }
}

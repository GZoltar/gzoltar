package pt.up.fe.ddu.base.messaging;

import pt.up.fe.ddu.base.events.EventListener;

public interface Service {

	EventListener getEventListener ();
    void interrupted ();
    void terminated ();
    
    public interface ServiceFactory {
    	Service create (String id);
    }
}

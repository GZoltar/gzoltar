package com.gzoltar.core.messaging;

import com.gzoltar.core.events.EventListener;

public interface Service {

	EventListener getEventListener ();
    void interrupted ();
    void terminated ();
    
    public interface ServiceFactory {
    	Service create (String id);
    }
}

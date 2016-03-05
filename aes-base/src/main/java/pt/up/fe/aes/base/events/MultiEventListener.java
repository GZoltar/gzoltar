package pt.up.fe.aes.base.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.up.fe.aes.base.model.Node.Type;

public class MultiEventListener implements EventListener {

	private List<EventListener> eventListeners = new ArrayList<EventListener>();
	
	public MultiEventListener(EventListener... els) {
		Collections.addAll(eventListeners, els);
	}
	
	public void add(EventListener el) {
		eventListeners.add(el);
	}
	
	@Override
	public void endTransaction(String transactionName, boolean[] activity, boolean isError) {
		for(EventListener el : eventListeners) {
			el.endTransaction(transactionName, activity, isError);
		}
	}
	
	@Override
	public void endTransaction(String transactionName, boolean[] activity, int hashCode, boolean isError) {
		for(EventListener el : eventListeners) {
			el.endTransaction(transactionName, activity, hashCode, isError);
		}
	}

	@Override
	public void addNode(int id, String name, Type type, int parentId) {
		for(EventListener el : eventListeners) {
			el.addNode(id, name, type, parentId);
		}
	}

	@Override
	public void addProbe(int id, int nodeId) {
		for(EventListener el : eventListeners) {
			el.addProbe(id, nodeId);
		}
		
	}

	@Override
	public void endSession() {
		for(EventListener el : eventListeners) {
			el.endSession();
		}
	}

}

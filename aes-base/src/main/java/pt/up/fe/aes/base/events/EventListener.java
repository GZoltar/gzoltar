package pt.up.fe.aes.base.events;

import pt.up.fe.aes.base.model.Node.Type;

public interface EventListener {

	void endTransaction (String transactionName, boolean[] activity, boolean isError);

	void addNode(int id, String name, Type type, int parentId);

	void addProbe(int id, int nodeId);
	
	void endSession();
}

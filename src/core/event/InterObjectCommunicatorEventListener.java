package core.event;

public interface InterObjectCommunicatorEventListener {
	
	//public String objectName = "";
	
	void onInterObjectCommunicator_CommunicateEvent(Object o);
	
	void onInterObjectCommunicator_CommunicateEvent(String descriptor, Object o);
	
	Object onInterObjectCommunicator_RequestEvent(Object o);
	
	Object onInterObjectCommunicator_RequestEvent(String descriptor, Object o);
	
	String getObjectName();
}

package org.architecturemining.interactionCentric.models;

import org.architecturemining.interactionCentric.models.LinkedListEdgesSet.CustomLinkedList;
import org.architecturemining.interactionCentric.util.XESFunctions;

public class InteractionNetwork {

	public CustomLinkedList network;
	public String callerTag, calleeTag;
	public static XESFunctions xes;
	
	
	//needed for building model from file by json object.
	public InteractionNetwork() {
		super();
	}

	public InteractionNetwork(ParameterSettings iSettings) {
		this.xes = new XESFunctions(iSettings.callerTag, iSettings.calleeTag, iSettings.eventTypeTag);
		network = new CustomLinkedList(xes);
		network.computeNetwork(iSettings.log); 
		this.callerTag = iSettings.callerTag;
		this.calleeTag = iSettings.calleeTag;
	}

	public CustomLinkedList getNetwork() {
		return network;
	}

	public void setNetwork(CustomLinkedList network) {
		this.network = network;
	}

	public String getCallerTag() {
		return callerTag;
	}

	public void setCallerTag(String callerTag) {
		this.callerTag = callerTag;
	}

	public String getCalleeTag() {
		return calleeTag;
	}

	public void setCalleeTag(String calleeTag) {
		this.calleeTag = calleeTag;
	}

	
}

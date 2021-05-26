package org.architecturemining.interactionCentric.models;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.architecturemining.interactionCentric.models.LinkedListEdgesSet.CustomLinkedList;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;

public class InteractionModelV2 {

	public CustomLinkedList network;
	public ArrayList<String> nodes;
	
	
	//needed for building model from file by json object.
	public InteractionModelV2() {
		super();
	}

	public InteractionModelV2(ParameterSettings iSettings) {
		this.nodes = uniqueEntities(iSettings);
		network = new CustomLinkedList(iSettings.callerTag, iSettings.calleeTag, this.nodes);
		network.computeNetwork(iSettings.log);	
	}
	
	public static ArrayList<String> uniqueEntities(ParameterSettings iSettings) {
		Set<String> list = new LinkedHashSet<String>();
		list.add("start");
		for (XTrace trace : iSettings.log) {
			Set<String> traceList = uniqueEntitiesPerTrace(trace,iSettings);
	        list.addAll(traceList);
		}
		list.add("end");
		return new ArrayList<>(list);
	}
	
	public static Set<String> uniqueEntitiesPerTrace(XTrace trace, ParameterSettings iSettings) {
		Set<String> list = new LinkedHashSet<String>();
		for (XEvent event : trace) {
			XAttributeMap attributes = event.getAttributes();
			XAttributeLiteral callerAttribute = (XAttributeLiteral) attributes.get(iSettings.callerTag);
			XAttributeLiteral calleeAttribute = (XAttributeLiteral) attributes.get(iSettings.calleeTag);
			list.add(callerAttribute.toString());
			list.add(calleeAttribute.toString());
		}
		return list;
	}

	public CustomLinkedList getNetwork() {
		return network;
	}

	public void setNetwork(CustomLinkedList network) {
		this.network = network;
	}

	public ArrayList<String> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<String> nodes) {
		this.nodes = nodes;
	}
	
}

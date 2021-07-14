package org.architecturemining.interactionCentric.models.LinkedListEdgesSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.architecturemining.interactionCentric.util.HelperFunctions;
import org.architecturemining.interactionCentric.util.XESFunctions;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class CustomLinkedList {

	public ArrayList<String> nodeNames;
	public Map<String, LinkedListNode> traceNodes = new HashMap<String, LinkedListNode>();
	public XESFunctions xes;
	public boolean eventTypesUsed;
	
		
	public CustomLinkedList() {
		super();
	}

	public CustomLinkedList(XESFunctions xes) {
		super();
		this.xes = xes;
		eventTypesUsed = xes.eventTypeTag != "(empty)";
	}
	
	public void computeNetwork(XLog log) {
		
		this.nodeNames = uniqueEntities(log);
		
		for(String s: nodeNames) {
			traceNodes.put(s, new LinkedListNode(s));
		}
		for(XTrace trace: log) {	
			EdgeMap edgeMap = HelperFunctions.buildEdgeMap(trace, xes, eventTypesUsed);
			String prevNode;
			for(Map.Entry<String, Set<String>> node: edgeMap.edges.entrySet()) {
				if(node.getValue().size() > 0) {
					if(edgeMap.getPrevNodes().containsKey(node.getKey()) && edgeMap.prevNodes.get(node.getKey()).size() > 0) {
						List<String> sortedList = new ArrayList<String>(edgeMap.prevNodes.get(node.getKey()));
						Collections.sort(sortedList);
						prevNode = String.join("", sortedList);
					}else {
						prevNode = "prev";
					}
					traceNodes.get(node.getKey()).addOutgoingNodeSet(prevNode, node.getValue());
				}
			}
		}		
	}
	
	public ArrayList<String> uniqueEntities(XLog log) {
		Set<String> list = new LinkedHashSet<String>();
		list.add("start");
		for (XTrace trace : log) {
			Set<String> traceList = uniqueEntitiesPerTrace(trace);
	        list.addAll(traceList);
		}
		list.add("end");
		return new ArrayList<>(list);
	}
	
	public Set<String> uniqueEntitiesPerTrace(XTrace trace) {
		Set<String> list = new LinkedHashSet<String>();
		for (XEvent event : trace) {
			list.add(xes.getCaller(event));
			list.add(xes.getCallee(event));
			if(eventTypesUsed) {
				list.add(xes.getEventType(event) + "_event");
			}
		}
		return list;
	}
	
	
	public ArrayList<String> getNodeNames() {
		return nodeNames;
	}

	public void setNodeNames(ArrayList<String> nodeNames) {
		this.nodeNames = nodeNames;
	}

	public Map<String, LinkedListNode> getTraceNodes() {
		return traceNodes;
	}

	public void setTraceNodes(Map<String, LinkedListNode> traceNodes) {
		this.traceNodes = traceNodes;
	}

	
	
}

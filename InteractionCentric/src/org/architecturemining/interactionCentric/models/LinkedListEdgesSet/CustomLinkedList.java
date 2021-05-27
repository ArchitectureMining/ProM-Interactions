package org.architecturemining.interactionCentric.models.LinkedListEdgesSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.architecturemining.interactionCentric.util.XESFunctions;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class CustomLinkedList {

	public ArrayList<String> nodeNames;
	
	public Map<String, LinkedListNode> traceNodes = new HashMap<String, LinkedListNode>();
	
	
	private String callerTag;
	private String calleeTag;
	private LinkedListSetOfEdges currentListSet;
		
	public CustomLinkedList() {
		super();
	}

	public CustomLinkedList(String callerTag, String calleeTag, ArrayList<String> nodes) {
		super();
		this.callerTag = callerTag;
		this.calleeTag = calleeTag;		
		this.nodeNames = nodes;
		
	}
	
	public void computeNetwork(XLog log) {
		XESFunctions xes = new XESFunctions(callerTag, calleeTag);
		for(String s: nodeNames) {
			traceNodes.put(s, new LinkedListNode(s));
		}
		int cnt = 1;
		for(XTrace trace: log) {	
			Map<String, Set<String>> edgeMap = buildEdgeMap(trace, xes);
			String prevNode = "prev";
			for(Map.Entry<String, Set<String>> node: edgeMap.entrySet()) {
				if(node.getValue().size() > 0) {
					traceNodes.get(node.getKey()).addOutgoingNodeSet(prevNode, node.getValue());
				}
			}
		}
		
		System.out.println(traceNodes.toString());
		
	}


	private Map<String, Set<String>> buildEdgeMap(XTrace trace, XESFunctions xes){
		
		Set<String> sourceValues = xes.getSourceAttributeValues(trace);
		Set<String> sinkValues = xes.getSinkAttributeValues(trace);
		
		Set<String> uniquevalues = new HashSet<String>();
		uniquevalues.addAll(sourceValues);
		uniquevalues.addAll(sinkValues);
		
		Map<String, Set<String>> edgeMap = new HashMap<String, Set<String>>();
		for(String node: nodeNames) {
			edgeMap.put(node, new HashSet<String>());
		}

		
		for(XEvent ev: trace) {
			edgeMap.get(xes.getCaller(ev)).add(xes.getCallee(ev));
		}
		
		for(String x: xes.getStarterNodes(sinkValues, uniquevalues)) {		
			edgeMap.get("start").add(x);
		}
		
		for(String x: xes.getEndNodes(sourceValues, uniquevalues)) {
			edgeMap.get(x).add("end");
		}
		
		return edgeMap;
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

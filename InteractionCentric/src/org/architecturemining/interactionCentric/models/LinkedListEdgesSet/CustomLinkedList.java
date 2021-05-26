package org.architecturemining.interactionCentric.models.LinkedListEdgesSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.architecturemining.interactionCentric.util.XESFunctions;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class CustomLinkedList {

	public ArrayList<String> nodeNames;
	
	public Map<String, LinkedListNode> traceNodes = new HashMap<String, LinkedListNode>();
	
	
	private String callerTag;
	private String calleeTag;
		
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
			
			Set<String> sourceValues = xes.getSourceAttributeValues(trace);
			Set<String> sinkValues = xes.getSinkAttributeValues(trace);
			
			Set<String> uniquevalues = new HashSet<String>();
			uniquevalues.addAll(sourceValues);
			uniquevalues.addAll(sinkValues);
						
			XAttributeMap attributes = trace.getAttributes();
			String trackingID = "trace" + cnt;
			
			for(XEvent ev: trace) {
				traceNodes.get(xes.getCaller(ev)).addOutgoingNode("prev", trackingID, xes.getCallee(ev));		 		
			}
			
			for(String x: xes.getStarterNodes(sinkValues, uniquevalues)) {	
				traceNodes.get("start").addOutgoingNode("prev", trackingID, x);	
			}
			
			for(String x: xes.getEndNodes(sourceValues, uniquevalues)) {
				traceNodes.get(x).addOutgoingNode("prev", trackingID, "end");	
			}
			cnt++;
		}
		
		System.out.println(traceNodes.toString());
		
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

package org.architecturemining.interactionCentric.models.PointerList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;

public class PointerList {

	PointerListNode root;
	Map<String, PointerListNode> traceNodes = new HashMap<String, PointerListNode>();
	List<PointerListEdge> traceEdges = new ArrayList<PointerListEdge>();
	
	String callerTag;
	String calleeTag;

	public PointerList(String callerTag, String calleeTag) {
		super();
		this.callerTag = callerTag;
		this.calleeTag = calleeTag;
	}
	
	public PointerList(XTrace trace, String callerTag, String calleeTag) {
		this.callerTag = callerTag;
		this.calleeTag = calleeTag;
		
		Set<String> sourceValues = getSourceAttributeValues(trace);
		Set<String> sinkValues = getSinkAttributeValues(trace);
		
		Set<String> uniquevalues = new HashSet<String>();
		uniquevalues.addAll(sourceValues);
		uniquevalues.addAll(sinkValues);
		traceNodes.put("start", new PointerListNode("start"));
		for(String s: uniquevalues) {
			traceNodes.put(s, new PointerListNode(s));
		}
		traceNodes.put("end", new PointerListNode("end"));
		
		for(XEvent e: trace) {
			XAttributeMap att = e.getAttributes();
			String source = att.get(this.callerTag).toString();
			String sink = att.get(this.calleeTag).toString();
			traceEdges.add(new PointerListEdge(traceNodes.get(source), traceNodes.get(sink)));
		}
		for(String s: getStarterNodes(sinkValues, uniquevalues)) {
			traceEdges.add(new PointerListEdge(traceNodes.get("start"), traceNodes.get(s)));
		}
		for(String s: getEndNodes(sourceValues, uniquevalues)) {
			traceEdges.add(new PointerListEdge(traceNodes.get(s), traceNodes.get("end")));
		}		
		root = traceNodes.get("start");
	}
	
	private Set<String> getSourceAttributeValues(XTrace trace) {
		
		Set<String> attributeKeySet = new HashSet<String>();
		for(XEvent e: trace) {
			XAttributeMap att = e.getAttributes();
			XAttributeLiteral source = (XAttributeLiteral) att.get(callerTag);
			attributeKeySet.add(source.toString());
		}
    	return attributeKeySet;
	}
	
	private Set<String> getSinkAttributeValues(XTrace trace) {
		Set<String> attributeKeySet = new HashSet<String>();
		for(XEvent e: trace) {
			XAttributeMap att = e.getAttributes();
			XAttributeLiteral sink = (XAttributeLiteral) att.get(calleeTag);
			attributeKeySet.add(sink.toString());
		}
    	return attributeKeySet;
	}
	
	private Set<String> getStarterNodes(Set<String> sinkValues, Set<String> allValues){
		Set<String> starters = new HashSet<String>();
		starters.addAll(allValues);
		starters.removeAll(sinkValues);
		return starters;
	}
	
	private Set<String> getEndNodes(Set<String> sourceValues, Set<String> allValues){
		Set<String> starters = new HashSet<String>();
		starters.addAll(allValues);
		starters.removeAll(sourceValues);
		return starters;
	}
	
	@Override
	public String toString() {
		String retString = traceEdges.get(0).source.current;
		for(PointerListEdge plE: traceEdges) {
			retString += " -> " + plE.target.current;
		}
		return retString;
	}	
}

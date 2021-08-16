package org.architecturemining.interactionCentric.models.PointerList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.architecturemining.interactionCentric.util.XESFunctions;
import org.architecturemining.interactionCentric.visualizer.graph.GraphEdge;
import org.architecturemining.interactionCentric.visualizer.graph.GraphNode;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class PointerList {

	public Map<String, GraphNode> traceNodes = new HashMap<String, GraphNode>();
	public List<GraphEdge> traceEdges = new ArrayList<GraphEdge>();
	
	
	String callerTag;
	String calleeTag;

	public PointerList(String callerTag, String calleeTag) {
		super();
		this.callerTag = callerTag;
		this.calleeTag = calleeTag;		
	}
	
	public PointerList(String callerTag, String calleeTag, XLog log) {
		super();
		this.callerTag = callerTag;
		this.calleeTag = calleeTag;
	}
	
	public PointerList(XTrace trace, String callerTag, String calleeTag, Map<String, Double> likelihoods) {
		XESFunctions xes = new XESFunctions(callerTag, calleeTag, null);
		this.callerTag = callerTag;
		this.calleeTag = calleeTag;
		
		Set<String> sourceValues = xes.getSourceAttributeValues(trace);
		Set<String> sinkValues = xes.getSinkAttributeValues(trace);
		
		Set<String> uniquevalues = new HashSet<String>();
		uniquevalues.addAll(sourceValues);
		uniquevalues.addAll(sinkValues);
		traceNodes.put("start", new GraphNode("start"));
		for(String s: uniquevalues) {
			traceNodes.put(s, new GraphNode(s));
		}
		traceNodes.put("end", new GraphNode("end"));
		
		for(XEvent e: trace) {
			XAttributeMap att = e.getAttributes();
			String source = att.get(this.callerTag).toString();
			String sink = att.get(this.calleeTag).toString();
			traceEdges.add(new GraphEdge(traceNodes.get(source), traceNodes.get(sink), likelihoods.get(source+"->"+sink)));
		}
		for(String s: xes.getStarterNodes(sinkValues, uniquevalues)) {
			traceEdges.add(new GraphEdge(traceNodes.get("start"), traceNodes.get(s), likelihoods.get("start->"+s)));
		}
		for(String s: xes.getEndNodes(sourceValues, uniquevalues)) {
			traceEdges.add(new GraphEdge(traceNodes.get(s), traceNodes.get("end"), likelihoods.get(s+"->end")));
		}		
	}
}

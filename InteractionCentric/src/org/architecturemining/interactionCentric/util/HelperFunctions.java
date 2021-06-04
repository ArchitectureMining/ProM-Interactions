package org.architecturemining.interactionCentric.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.architecturemining.interactionCentric.models.LinkedListEdgesSet.EdgeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;

public class HelperFunctions {

	public static EdgeMap buildEdgeMap(XTrace trace, XESFunctions xes, List<String> nodeNames){
		
		Set<String> sourceValues = xes.getSourceAttributeValues(trace);
		Set<String> sinkValues = xes.getSinkAttributeValues(trace);
		
		Set<String> uniquevalues = new HashSet<String>();
		uniquevalues.addAll(sourceValues);
		uniquevalues.addAll(sinkValues);
		
		Map<String, Set<String>> edges = new HashMap<String, Set<String>>();
		Map<String, Set<String>> prevNodes =  new HashMap<String, Set<String>>();
		for(String node: nodeNames) {
			edges.put(node, new HashSet<String>());
		}

		for(XEvent ev: trace) {
			edges.get(xes.getCaller(ev)).add(xes.getCallee(ev));
			
			if(prevNodes.containsKey(xes.getCallee(ev)))
				prevNodes.get(xes.getCallee(ev)).add(xes.getCaller(ev));
			else
				prevNodes.put(xes.getCallee(ev), new HashSet<String>(Arrays.asList(xes.getCaller(ev))));
			
		}
		
		for(String x: xes.getStarterNodes(sinkValues, uniquevalues)) {		
			edges.get("start").add(x);		
			if(prevNodes.containsKey(x)) 
				prevNodes.get(x).add("start");
			else 
				prevNodes.put(x, new HashSet<String>(Arrays.asList("start")));
			
		}
		
		for(String x: xes.getEndNodes(sourceValues, uniquevalues)) {
			edges.get(x).add("end");
			if(prevNodes.containsKey("end")) 
				prevNodes.get("end").add(x);
			else 
				prevNodes.put("end", new HashSet<String>(Arrays.asList(x)));
			
		}
		
		return new EdgeMap(edges, prevNodes);
	}
	
}

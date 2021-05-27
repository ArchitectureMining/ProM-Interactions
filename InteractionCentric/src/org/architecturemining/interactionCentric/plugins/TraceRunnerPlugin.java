package org.architecturemining.interactionCentric.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.architecturemining.interactionCentric.models.InteractionNetwork;
import org.architecturemining.interactionCentric.models.SingleLikelihood;
import org.architecturemining.interactionCentric.models.TracesLikelihood;
import org.architecturemining.interactionCentric.models.LinkedListEdgesSet.CustomLinkedList;
import org.architecturemining.interactionCentric.models.LinkedListEdgesSet.LinkedListSetOfEdges;
import org.architecturemining.interactionCentric.util.XESFunctions;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;

/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Plugin that takes an event log with traces as input and determines the likelihood per trace.
*/
public class TraceRunnerPlugin {

	@Plugin(
			name = "Trace Runner Plugin",
			parameterLabels = { "Interaction Network", "XLog"},
			returnLabels = { "Likelihood per Trace" },
			returnTypes = { TracesLikelihood.class },
			userAccessible = true,
			help = "This plugin will run every single trace through the Interation Model and computes the likelihood of that single trace."
	)
	@UITopiaVariant(
            affiliation = "Utrecht University", 
            author = "Arnout Verhaar", 
            email = "w.d.verhaar@students.uu.nl"
    )
	public static TracesLikelihood modelDiscovery(final UIPluginContext context, InteractionNetwork iNetwork, XLog traces) {
		
		List<SingleLikelihood> l = computeLikelihoods(iNetwork, traces);
		
		return new TracesLikelihood(l);
	}
	public static List<SingleLikelihood> computeLikelihoods(InteractionNetwork iNetwork, XLog traces){
		
		List<SingleLikelihood> computations = new ArrayList<SingleLikelihood>();
		XESFunctions xes = new XESFunctions(iNetwork.callerTag, iNetwork.calleeTag);
		for(XTrace trace: traces) {
			Map<String, Set<String>> edgeMap = buildEdgeMap(trace, xes);
			Map<String, Double> traceLikelihood = computeLikelihoodsForSingleTrace(edgeMap, iNetwork.network);
			computations.add(new SingleLikelihood(edgeMap, traceLikelihood, trace));
		}
			
		return computations;	
	}
	
	private static Map<String, Double> computeLikelihoodsForSingleTrace(Map<String, Set<String>> edgeMap, CustomLinkedList network) {
		
		Stack<String> nodeStack = new Stack<String>();
		nodeStack.push("start");
		
		String prevNode = "prev";
		double timesProbability = 1;
		double addedProbability = 0;
		double customProbability = 1;
		int passedNodesCounter = 0;
		while(nodeStack.size() > 0) {
			String currentNode = nodeStack.pop();	
			System.out.println(currentNode);
			Map<String, List<LinkedListSetOfEdges>> traces = network.traceNodes.get(currentNode).outgoingEdgesSets;
			int totalTraces = 1;
			if(traces.containsKey(prevNode)) {
				totalTraces = traces.get(prevNode).stream().mapToInt(x -> x.getOccurenceCounter()).sum();
				passedNodesCounter++;
				if(network.traceNodes.get(currentNode).outgoingEdgesSets.containsKey(prevNode)) {
					boolean matchFound = false;
					for(LinkedListSetOfEdges lis : network.traceNodes.get(currentNode).outgoingEdgesSets.get(prevNode)) {
						if(lis.targetNodes.equals(edgeMap.get(currentNode))) {
							timesProbability *= (float)lis.occurenceCounter / totalTraces;
							addedProbability += (float)lis.occurenceCounter / totalTraces;
							nodeStack.addAll(edgeMap.get(currentNode));
							matchFound = true;
						}
						
					}	
					if(!matchFound) {
						//only lower probability when it follows a path that is not in the learning set.
						customProbability *= 0.1;
						timesProbability *= 0.1;
						addedProbability += 0;
						nodeStack.addAll(edgeMap.get(currentNode));
					}
				}
			}
			
		}
		
		
		HashMap<String, Double> returnMap = new HashMap<String, Double>();
		
		returnMap.put("addedProbability", addedProbability / passedNodesCounter);
		returnMap.put("timesProbability", timesProbability);
		returnMap.put("customProbability", customProbability);
		
		return returnMap;
		
	}
	
	private static Map<String, Set<String>> buildEdgeMap(XTrace trace, XESFunctions xes){
		
		
		
		Set<String> sourceValues = xes.getSourceAttributeValues(trace);
		Set<String> sinkValues = xes.getSinkAttributeValues(trace);
		
		Set<String> uniquevalues = new HashSet<String>();
		uniquevalues.addAll(sourceValues);
		uniquevalues.addAll(sinkValues);
		
		Map<String, Set<String>> edgeMap = new HashMap<String, Set<String>>();
		for(String node: uniquevalues) {
			edgeMap.put(node, new HashSet<String>());
		}
		edgeMap.put("start", new HashSet<String>());
		edgeMap.put("end", new HashSet<String>());
		
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
	
}

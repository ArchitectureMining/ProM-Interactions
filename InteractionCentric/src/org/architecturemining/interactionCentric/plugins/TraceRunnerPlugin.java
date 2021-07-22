package org.architecturemining.interactionCentric.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.architecturemining.interactionCentric.models.InteractionNetwork;
import org.architecturemining.interactionCentric.models.ParameterSettings;
import org.architecturemining.interactionCentric.models.SingleLikelihood;
import org.architecturemining.interactionCentric.models.TracesLikelihood;
import org.architecturemining.interactionCentric.models.LinkedListEdgesSet.CustomLinkedList;
import org.architecturemining.interactionCentric.models.LinkedListEdgesSet.EdgeMap;
import org.architecturemining.interactionCentric.models.LinkedListEdgesSet.LinkedListSetOfEdges;
import org.architecturemining.interactionCentric.util.HelperFunctions;
import org.architecturemining.interactionCentric.util.XESFunctions;
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
			parameterLabels = { "Interaction Network", "ParameterSettings"},
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
	public static TracesLikelihood modelDiscovery(final UIPluginContext context, InteractionNetwork iNetwork, ParameterSettings iSettings) {	
		
		context.getProgress().setMinimum(0);
        context.getProgress().setMaximum(iSettings.log.size());
        context.getProgress().setCaption("Running single traces");
        context.getProgress().setIndeterminate(false);
		
        System.out.println("Er zijn traces: " + iSettings.log.size());
        
		List<SingleLikelihood> computations = new ArrayList<SingleLikelihood>();
		XESFunctions xes = new XESFunctions(iSettings.callerTag, iSettings.calleeTag, iSettings.getEventTypeTag());
		for(XTrace trace: iSettings.log) {
			EdgeMap edgeMap = HelperFunctions.buildEdgeMap(trace, xes, iSettings.getEventTypeTag() != "(empty)");
			TraceInformation traceLikelihood = computeLikelihoodsForSingleTrace(edgeMap, iNetwork.network);
			Map<String, Boolean> analysisResults = analyzeBehaviour(traceLikelihood.likelihoods, edgeMap, traceLikelihood.passedNodesCounter);
			computations.add(new SingleLikelihood(edgeMap, traceLikelihood.likelihoods, traceLikelihood.edgeProbability, trace, analysisResults, traceLikelihood.passedNodesCounter));
			
			context.getProgress().inc();
		}
				
		return new TracesLikelihood(computations, xes);
	}


	private static TraceInformation computeLikelihoodsForSingleTrace(EdgeMap edgeMap, CustomLinkedList network) {
		
		Map<String, Double> edgeProbabilities = new HashMap<String, Double>();
		
		Stack<String> nodeStack = new Stack<String>();
		nodeStack.push("start");
		
		String prevNode = "prev";
		double timesProbability = 1;
		double addedProbability = 0;
		double customProbability = 1;
		double minimalProbability = 1;
		
		int customMatchFailedCounter = 0;
		
		int passedNodesCounter = 0;
		List<String> passedNodes = new ArrayList<String>();
		
		while(nodeStack.size() > 0) {			
			String currentNode = nodeStack.pop();
			//check if node exists in the model...
			Map<String, List<LinkedListSetOfEdges>> traces;
			if(network.traceNodes.containsKey(currentNode)) {
				traces = network.traceNodes.get(currentNode).outgoingEdgesSets;
			}else {
				traces = new HashMap<String, List<LinkedListSetOfEdges>>();
			}
			
			if(edgeMap.getPrevNodes().containsKey(currentNode) && edgeMap.prevNodes.get(currentNode).size() > 0) {
				List<String> sortedList = new ArrayList<String>(edgeMap.prevNodes.get(currentNode));
				Collections.sort(sortedList);
				prevNode = String.join("", sortedList);
			}else {
				prevNode = "prev";
			}
			
			int totalTraces = 1;
			
			for(String node: edgeMap.edges.get(currentNode)) {
				if(!passedNodes.contains(node)) {
					nodeStack.add(node);
					passedNodes.add(node);
				}
			}
			
			if(traces.containsKey(prevNode)) {
				totalTraces = traces.get(prevNode).stream().mapToInt(x -> x.getOccurenceCounter()).sum();							
				if(currentNode != "start") { 
					// do not take the start to first service into account. (this probability is often low because of many different options.)					
					if(network.traceNodes.get(currentNode).outgoingEdgesSets.containsKey(prevNode)) {
						boolean matchFound = false;
						for(LinkedListSetOfEdges lis : network.traceNodes.get(currentNode).outgoingEdgesSets.get(prevNode)) {
							if(lis.targetNodes.equals(edgeMap.edges.get(currentNode))) {
								float interactionProbability = (float)lis.occurenceCounter / totalTraces;		
								
								//correction for non existing traces in the learning model (mostly already differ at the start).
								if(interactionProbability < 0.1 && prevNode.equals("start")){
									interactionProbability = (float) 0.8;
								}
								
								timesProbability *= interactionProbability;
								addedProbability += Math.pow(interactionProbability,3); // pennalize bad interactions	
								
								// implements a weighted probability that is slow to react.
								customProbability *= ((customProbability * 5 + interactionProbability) / 6);
								if(interactionProbability < minimalProbability)
									minimalProbability = interactionProbability;
								passedNodesCounter++;
								
								matchFound = true;
								for(String node: edgeMap.edges.get(currentNode)) {								
									edgeProbabilities.put(currentNode + "_" + node, (double) interactionProbability);
								}
							}					
						}	
						if(!matchFound) {
							passedNodesCounter++;
							customMatchFailedCounter++;
							//only lower probability when it follows a path that is not in the learning set.
														
							if(0.2 < minimalProbability)
								minimalProbability = 0.2;
							
							timesProbability *= 0.1;
							addedProbability += 0;

						}
					}
				}
			}else {
				if(currentNode != "end") {
					customMatchFailedCounter++;
					if(0.2 < minimalProbability)
						minimalProbability = 0.2;					
					timesProbability *= 0.1;
					addedProbability += 0;
					passedNodesCounter++;
				}
					
			}
			
		}
		
		
		HashMap<String, Double> returnMap = new HashMap<String, Double>();
		
		returnMap.put("addedProbability", addedProbability / passedNodesCounter);
		returnMap.put("timesProbability", timesProbability);
		returnMap.put("minimalProbability", minimalProbability);
		
		customProbability = customMatchFailedCounter > 0 ? Math.pow(0.1, customMatchFailedCounter) : customProbability;
		// the customprobability is overruled if there exist a non existing path, which results in 0.1^customMatchFailedCounter.
		
		returnMap.put("customProbability", customProbability);
		
		
		TraceInformation result = new TraceInformation(returnMap, edgeProbabilities, passedNodesCounter);
		
		return result;
		
	}
	
	private static Map<String, Boolean> analyzeBehaviour(Map<String, Double> traceLikelihood, EdgeMap edgemap, int passedNodesCounter) {
		Map<String, Boolean> analysis = new HashMap<String, Boolean>();
		
		for(Map.Entry<String, Double> ent: traceLikelihood.entrySet()) {
			boolean thresholdValue = false;
			switch(ent.getKey()) {
				case "addedProbability":
					thresholdValue = ent.getValue() > 0.6;
					break;
				case "timesProbability":
					thresholdValue = ent.getValue() > (Math.pow(0.5, passedNodesCounter)); // amount of nodes 
					break;
				case "customProbability":
					thresholdValue = ent.getValue() > (Math.pow(0.5, passedNodesCounter));
					break;
				case "minimalProbability":
					thresholdValue = ent.getValue() > 0.2;
				
			}
			
			analysis.put(ent.getKey(), thresholdValue);
		}
		return analysis;
	}
	
	private static int setEditDistance(List<Object> a, List<Object> b) {
		
		if(a.size() == 0) return b.size();
		if(b.size() == 0) return a.size();		
		
		if(a.get(a.size()-1).equals(b.get(b.size()-1)))
			return setEditDistance(a.subList(0, a.size()-1), b.subList(0, b.size()-1));
	
		return 1 + Math.min(Math.min(
				setEditDistance(a, b.subList(0, b.size()-1)), 							// Insertion
				setEditDistance(a.subList(0, a.size()-1), b.subList(0, b.size()-1))),	// Replacing entity
				setEditDistance(a.subList(0, a.size()-1), b));							// Deletion
	}
	
	private static int stepsFromStart(EdgeMap edgie, String a) {
		String currentNode = a;
		int counter = 0;
		while(!currentNode.equals("start")) {
			currentNode = (String) edgie.prevNodes.get(currentNode).toArray()[0];
			counter++;
		}
		
		return counter;
	}
	
	// used to combine a return result
	static private class TraceInformation {
		Map<String, Double> likelihoods;
		Map<String, Double> edgeProbability;
		int passedNodesCounter;
			
		public TraceInformation(Map<String, Double> likelihoods, Map<String, Double> edgeProbability, int passedNodesCounter) {
			super();
			this.likelihoods = likelihoods;
			this.edgeProbability = edgeProbability;
			this.passedNodesCounter = passedNodesCounter;
		}
	}
}

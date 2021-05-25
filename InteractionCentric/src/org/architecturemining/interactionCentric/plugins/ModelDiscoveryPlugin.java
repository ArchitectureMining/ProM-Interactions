package org.architecturemining.interactionCentric.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.architecturemining.interactionCentric.models.InteractionModel;
import org.architecturemining.interactionCentric.models.ParameterSettings;
import org.architecturemining.interactionCentric.util.XESFunctions;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;


/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Plugin that discovers the probability network based on given parameter settings containing the event log.
 * 
 * 
*/
public class ModelDiscoveryPlugin {
	@Plugin(
			name = "Interaction model plugin",
			parameterLabels = { "Parameter Settings(Xlog included)"},
			returnLabels = { "Interaction model" },
			returnTypes = { InteractionModel.class },
			userAccessible = true,
			help = "This plugin will build a model based on the interactions found within an XLog"
	)
	@UITopiaVariant(
            affiliation = "Utrecht University", 
            author = "Arnout Verhaar", 
            email = "w.d.verhaar@students.uu.nl"
    )
	public static InteractionModel modelDiscovery(final UIPluginContext context, ParameterSettings iSettings) {	

		ArrayList<String> s = uniqueEntities(iSettings);
		InteractionModel iModel = new InteractionModel(s, iSettings);
		iModel.setConnectionMatrix(createMatrix(iModel.entities, iSettings));
		iModel.computeProbabilityMatrix(entityCounter);
		return iModel;
		
	}
	
	
	// counter for all entities on their occurence in the event log. Needed for probability calculation
	public static Map<String, Integer> entityCounter = new HashMap<String, Integer>();
	// build the matrix and fill it by counting the connections between nodes. 
	// The start and end node connections are made outside of the loop.
	private static int[][] createMatrix(Map<String, Integer> entities, ParameterSettings iSettings) {
		XESFunctions xes = new XESFunctions(iSettings.callerTag, iSettings.calleeTag);
		
		
		int source, sink;
		int[][] cMatrix = new int[entities.size()][entities.size()];	
		for(String x: entities.keySet()) {
			entityCounter.put(x, 0);
		}
		for (XTrace trace : iSettings.log) {
			
			Set<String> sourceValues = xes.getSourceAttributeValues(trace);
			Set<String> sinkValues = xes.getSinkAttributeValues(trace);
			
			Set<String> uniquevalues = new HashSet<String>();
			uniquevalues.addAll(sourceValues);
			uniquevalues.addAll(sinkValues);
			
			int sourceNodeCount = entityCounter.get("start");
			entityCounter.put("start", sourceNodeCount + 1);
			List<String> added = new ArrayList<String>();
			for (XEvent event : trace) {			
				XAttributeMap attributes = event.getAttributes();
				String callerAttribute = ((XAttributeLiteral) attributes.get(iSettings.callerTag)).toString();
				String calleeAttribute = ((XAttributeLiteral) attributes.get(iSettings.calleeTag)).toString();			
				source = entities.get(callerAttribute);
				sink = entities.get(calleeAttribute);			
				cMatrix[source][sink]++;
				if(!added.contains(callerAttribute)){
					added.add(callerAttribute);
					sourceNodeCount = entityCounter.get(callerAttribute);
					entityCounter.put(callerAttribute, sourceNodeCount + 1);
				}
				
				
				//System.out.println(callerAttribute.toString() + " -> " + calleeAttribute.toString());
			}
			
			// add connections from start node to all entities without having incoming connections
			for(String x: xes.getStarterNodes(sinkValues, uniquevalues)) {		
				source = entities.get("start");
				sink = entities.get(x);			
				cMatrix[source][sink]++;
			}
			
			// add connections from all nodes which do not have outgoing connections, these will be connected to the end node.
			for(String x: xes.getEndNodes(sourceValues, uniquevalues)) {
				source = entities.get(x);
				sink = entities.get("end");
				cMatrix[source][sink]++;
				entityCounter.put(x, entityCounter.get(x) + 1);
			}
		}	
		return cMatrix;
	}

	// Compute all unique entities from an event log by looping over all events and saving the unique values encountered.
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
}

package org.architecturemining.interactionCentric.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.architecturemining.interactionCentric.models.InteractionModel;
import org.architecturemining.interactionCentric.models.ParameterSettings;
import org.architecturemining.interactionCentric.models.LinkedListEdgesSet.EdgeMap;
import org.architecturemining.interactionCentric.util.HelperFunctions;
import org.architecturemining.interactionCentric.util.XESFunctions;
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
public class VisualizeInteractionNetworkPlugin {
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
		XESFunctions xes = new XESFunctions(iSettings.callerTag, iSettings.calleeTag, iSettings.getEventTypeTag());
		ArrayList<String> s = uniqueEntities(iSettings, !iSettings.getEventTypeTag().equals("(empty)"), xes);
		InteractionModel iModel = new InteractionModel(s, iSettings);
		iModel.setConnectionMatrix(createVisualizationModel(iModel.entities, iSettings, xes));
		iModel.computeProbabilityMatrix(entityCounter);
		return iModel;
		
	}
	
	
	// counter for all entities on their occurence in the event log. Needed for probability calculation
	public static Map<String, Integer> entityCounter = new HashMap<String, Integer>();
	// build the matrix and fill it by counting the connections between nodes. 
	// The start and end node connections are made outside of the loop.
	private static int[][] createVisualizationModel(Map<String, Integer> entities, ParameterSettings iSettings, XESFunctions xes) {
				
		int source, sink;
		int[][] cMatrix = new int[entities.size()][entities.size()];	
		for(String x: entities.keySet()) {
			entityCounter.put(x, 0);
		}
		for (XTrace trace : iSettings.log) {
			EdgeMap edgie = HelperFunctions.buildEdgeMap(trace, xes, !iSettings.getEventTypeTag().equals("(empty)"));
			
			for(String node: edgie.edges.keySet()) {
				if(edgie.edges.containsKey(node)) {
					int sourceNodeCount = entityCounter.get(node);
					entityCounter.put(node, sourceNodeCount + 1);
				}
				Set<String> edges = edgie.edges.get(node);
				if(edges != null) {
					for(String e : edges) {
						source = entities.get(node);
						sink = entities.get(e);	
						cMatrix[source][sink]++;
					}
				}
			}
		}	
		return cMatrix;
	}

	// Compute all unique entities from an event log by looping over all events and saving the unique values encountered.
	public static ArrayList<String> uniqueEntities(ParameterSettings iSettings, boolean messageTypesUsed, XESFunctions xes) {
		Set<String> list = new LinkedHashSet<String>();
		list.add("start");
		for (XTrace trace : iSettings.log) {
			for (XEvent event : trace) {
				if(messageTypesUsed) {
					list.add(xes.getEventType(event) + "_event");
				}
				list.add(xes.getCaller(event));
				list.add(xes.getCallee(event));
			}
		}
		list.add("end");
		System.out.println(list.size());
		return new ArrayList<>(list);	
	}
}

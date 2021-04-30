package org.architecturemining.interactionCentric.plugins;

import java.util.ArrayList;
import java.util.Map;

import org.architecturemining.interactionCentric.models.InteractionModel;
import org.architecturemining.interactionCentric.models.ParameterSettings;
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
		return iModel;
		
	}
	
	
	// build the matrix and fill it by counting the connections between nodes. 
	// The start and end node connections are made outside of the loop.
	private static int[][] createMatrix(Map<String, Integer> entities, ParameterSettings iSettings) {
		int source, sink;
		int[][] cMatrix = new int[entities.size()][entities.size()];
		for (XTrace trace : iSettings.log) {

			// set event from start to the first node
			XAttributeMap start_attributes = trace.get(0).getAttributes();
			XAttributeLiteral start_callerAttribute = (XAttributeLiteral) start_attributes.get(iSettings.callerTag);
			source = entities.get("start");
			System.out.println(source);
			sink = entities.get(start_callerAttribute.toString());			
			cMatrix[source][sink]++;	
			
			System.out.println("start -> " + start_callerAttribute.toString());
			
			for (XEvent event : trace) {
				XAttributeMap attributes = event.getAttributes();
				XAttributeLiteral callerAttribute = (XAttributeLiteral) attributes.get(iSettings.callerTag);
				XAttributeLiteral calleeAttribute = (XAttributeLiteral) attributes.get(iSettings.calleeTag);			
				source = entities.get(callerAttribute.toString());
				sink = entities.get(calleeAttribute.toString());			
				cMatrix[source][sink]++;
				System.out.println(callerAttribute.toString() + " -> " + calleeAttribute.toString());
			}
			
			// set event from last node to the end node
			XAttributeMap end_attributes = trace.get(trace.size()-1).getAttributes();
			XAttributeLiteral end_calleeAttribute = (XAttributeLiteral) end_attributes.get(iSettings.calleeTag);
			source = entities.get(end_calleeAttribute.toString());
			sink = entities.get("end");			
			cMatrix[source][sink]++;
			
			System.out.println(end_calleeAttribute.toString() + " -> end");
			
		}	
		return cMatrix;
	}

	// Compute all unique entities from an event log by looping over all events and saving the unique values encountered.
	public static ArrayList<String> uniqueEntities(ParameterSettings iSettings) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("start");
		for (XTrace trace : iSettings.log) {
			for (XEvent event : trace) {
				XAttributeMap attributes = event.getAttributes();
				XAttributeLiteral callerAttribute = (XAttributeLiteral) attributes.get(iSettings.callerTag);
				XAttributeLiteral calleeAttribute = (XAttributeLiteral) attributes.get(iSettings.calleeTag);
				if(!list.contains(callerAttribute.toString()))
					list.add(callerAttribute.toString());
				if(!list.contains(calleeAttribute.toString()))
					list.add(calleeAttribute.toString());
			}
		}
		list.add("end");
		return list;
	}
}

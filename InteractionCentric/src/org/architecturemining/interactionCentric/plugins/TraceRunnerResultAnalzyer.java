package org.architecturemining.interactionCentric.plugins;

import java.util.HashMap;
import java.util.Map;

import org.architecturemining.interactionCentric.models.RunnerAnalysis;
import org.architecturemining.interactionCentric.models.SingleLikelihood;
import org.architecturemining.interactionCentric.models.TracesLikelihood;
import org.architecturemining.interactionCentric.models.runnerAnalysis.SingleTraceVertexInfo;
import org.architecturemining.interactionCentric.models.runnerAnalysis.SingleVertexAnalysis;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;

/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Plugin that takes an event log with traces as input and determines the likelihood per trace.
*/
public class TraceRunnerResultAnalzyer {

	@Plugin(
			name = "TraceRunner Result Analysis Plugin",
			parameterLabels = { "TracesLikelihood"},
			returnLabels = { "Analysis results in a visual" },
			returnTypes = { RunnerAnalysis.class },
			userAccessible = true,
			help = "This plugin analyses the result from the Runner plugin and creates a visual containing information about the results."
	)
	@UITopiaVariant(
            affiliation = "Utrecht University", 
            author = "Arnout Verhaar", 
            email = "w.d.verhaar@students.uu.nl"
    )
	public static RunnerAnalysis modelDiscovery(final UIPluginContext context, TracesLikelihood tLikelihood) {	
		
		context.getProgress().setMinimum(0);
        context.getProgress().setMaximum(tLikelihood.traces.size());
        context.getProgress().setCaption("amount of trace");
        context.getProgress().setIndeterminate(false);	
		context.getProgress().inc();
			
		Map<String, SingleVertexAnalysis> vertexAnalysis = new HashMap<String, SingleVertexAnalysis>();
		
		int totalInteractions = 0;
		
		for(SingleLikelihood sl : tLikelihood.traces) {
			for(String source : sl.getEdgeMap().edges.keySet()) {
				boolean added = false;
				if(!source.equals("start")) {
					for(String target: sl.getEdgeMap().edges.get(source)) {
						Double edgeLikelihood = sl.getEdgeProbabilities().get(source +"_"+ target);
						if(edgeLikelihood == null) {
							edgeLikelihood = 0.00;
						}
					
						if(!added) {
							// add source_target, edgeliklihood, trackingid. 
							added = true;
							if(!vertexAnalysis.containsKey(source)) {
								vertexAnalysis.put(source, new SingleVertexAnalysis(source));
							}
							vertexAnalysis.get(source).outgoingTraces.add(new SingleTraceVertexInfo(edgeLikelihood, sl.trackingID));
						}
						
						if(!vertexAnalysis.containsKey(target)) {
							vertexAnalysis.put(target, new SingleVertexAnalysis(target));
						}
						vertexAnalysis.get(target).incomingTraces.add(new SingleTraceVertexInfo(edgeLikelihood, sl.trackingID));
						totalInteractions++;
					}
				}
			}
		}
		
		return new RunnerAnalysis(vertexAnalysis, tLikelihood.traces.size(), totalInteractions);
	}	
}

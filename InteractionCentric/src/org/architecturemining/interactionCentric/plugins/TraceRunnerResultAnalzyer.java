package org.architecturemining.interactionCentric.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.architecturemining.interactionCentric.models.RunnerAnalysis;
import org.architecturemining.interactionCentric.models.SingleLikelihood;
import org.architecturemining.interactionCentric.models.TracesLikelihood;
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
					}
				}
			}
		}
		
		return new RunnerAnalysis(vertexAnalysis);
	}

	
	public static class SingleVertexAnalysis {
		
		List<SingleTraceVertexInfo> outgoingTraces;
		List<SingleTraceVertexInfo> incomingTraces;
		String vertexName;
		
		public SingleVertexAnalysis(String vertexName) {
			this.vertexName = vertexName;
			outgoingTraces = new ArrayList<SingleTraceVertexInfo>();
			incomingTraces = new ArrayList<SingleTraceVertexInfo>();
		}

		public List<SingleTraceVertexInfo> getOutgoingTraces() {
			return outgoingTraces;
		}

		public void setOutgoingTraces(List<SingleTraceVertexInfo> outgoingTraces) {
			this.outgoingTraces = outgoingTraces;
		}

		public List<SingleTraceVertexInfo> getIncomingTraces() {
			return incomingTraces;
		}

		public void setIncomingTraces(List<SingleTraceVertexInfo> incomingTraces) {
			this.incomingTraces = incomingTraces;
		}

		public String getVertexName() {
			return vertexName;
		}

		public void setVertexName(String vertexName) {
			this.vertexName = vertexName;
		}
		
		
	}
	
	public static class SingleTraceVertexInfo {
		
		public double likelihood;
		public String trackingID;

		public SingleTraceVertexInfo(double likelihood, String trackingID) {
			super();
			this.likelihood = likelihood;
			this.trackingID = trackingID;

		}

		public double getLikelihood() {
			return likelihood;
		}

		public void setLikelihood(double likelihood) {
			this.likelihood = likelihood;
		}

		public String getTrackingID() {
			return trackingID;
		}

		public void setTrackingID(String trackingID) {
			this.trackingID = trackingID;
		}
		
		
	}
}

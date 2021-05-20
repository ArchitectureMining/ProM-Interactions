package org.architecturemining.interactionCentric.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.architecturemining.interactionCentric.models.InteractionModel;
import org.architecturemining.interactionCentric.models.SingleLikelihood;
import org.architecturemining.interactionCentric.models.TracesLikelihood;
import org.architecturemining.interactionCentric.util.XESFunctions;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeMap;
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
			parameterLabels = { "Interaction Model", "XLog"},
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
	public static TracesLikelihood modelDiscovery(final UIPluginContext context, InteractionModel iMod, XLog traces) {
		
		List<SingleLikelihood> l = computeLikelihoodPerTrace(iMod, traces);
		
		return new TracesLikelihood(l);
	}
	public static List<SingleLikelihood> computeLikelihoodPerTrace(InteractionModel iMod, XLog traces){	
		XESFunctions xes = new XESFunctions(iMod.callerTag, iMod.calleeTag);
		int source, sink;
		
		
		
		List<SingleLikelihood> computations = new ArrayList<SingleLikelihood>();
		for(XTrace trace: traces) {
			double likelihood = 0;
			Map<String, Double> likelihoods = new HashMap<String, Double>();
			
			Set<String> sourceValues = xes.getSourceAttributeValues(trace);
			Set<String> sinkValues = xes.getSinkAttributeValues(trace);
			
			Set<String> uniquevalues = new HashSet<String>();
			uniquevalues.addAll(sourceValues);
			uniquevalues.addAll(sinkValues);
			for(XEvent event: trace) {
				XAttributeMap attributes = event.getAttributes();
				String callerAttribute = ((XAttributeLiteral) attributes.get(iMod.callerTag)).toString();
				String calleeAttribute = ((XAttributeLiteral) attributes.get(iMod.calleeTag)).toString();			
				source = iMod.entities.get(callerAttribute);
				sink = iMod.entities.get(calleeAttribute);
				
				double singleLikelihood = iMod.probabilityMatrix[source][sink];
				
				//likelihood *= iMod.probabilityMatrix[source][sink];
				likelihood += singleLikelihood;
				likelihoods.put(callerAttribute + "->" + calleeAttribute,singleLikelihood);
			}
			for(String x: xes.getStarterNodes(sinkValues, uniquevalues)) {		
				source = iMod.entities.get("start");
				sink = iMod.entities.get(x);
				double singleLikelihood = iMod.probabilityMatrix[source][sink];
				likelihood += singleLikelihood;
				likelihoods.put("start" + "->" + x, singleLikelihood);
			}
			
			System.out.println("sink");
			for(String x: xes.getEndNodes(sourceValues, uniquevalues)) {
				System.out.println(x);
				source = iMod.entities.get(x);
				sink = iMod.entities.get("end");
				double singleLikelihood = iMod.probabilityMatrix[source][sink];
				likelihood += singleLikelihood;
				likelihoods.put(x + "->" + "end", singleLikelihood);
			}
			
			likelihood = likelihood / (trace.size() + 2);
			computations.add(new SingleLikelihood(likelihood, trace, iMod.callerTag, iMod.calleeTag, likelihoods));
		}
		
		return computations;
		
	}
	
}

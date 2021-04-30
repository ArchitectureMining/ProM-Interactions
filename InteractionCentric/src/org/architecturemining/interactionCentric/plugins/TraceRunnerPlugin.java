package org.architecturemining.interactionCentric.plugins;

import java.util.ArrayList;
import java.util.List;

import org.architecturemining.interactionCentric.models.InteractionModel;
import org.architecturemining.interactionCentric.models.SingleLikelihood;
import org.architecturemining.interactionCentric.models.TracesLikelihood;
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
		int source, sink;
		List<SingleLikelihood> computations = new ArrayList<SingleLikelihood>();
		for(XTrace trace: traces) {
			double likelihood = 1;
			for(XEvent event: trace) {
				XAttributeMap attributes = event.getAttributes();
				XAttributeLiteral callerAttribute = (XAttributeLiteral) attributes.get(iMod.callerTag);
				XAttributeLiteral calleeAttribute = (XAttributeLiteral) attributes.get(iMod.calleeTag);			
				source = iMod.entities.get(callerAttribute.toString());
				sink = iMod.entities.get(calleeAttribute.toString());			
				likelihood *= iMod.probabilityMatrix[source][sink];
			}
			computations.add(new SingleLikelihood(likelihood, trace, iMod.callerTag, iMod.calleeTag));
		}
		
		return computations;
		
	}
	
}

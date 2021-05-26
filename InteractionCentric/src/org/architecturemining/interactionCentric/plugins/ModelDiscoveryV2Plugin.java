package org.architecturemining.interactionCentric.plugins;

import org.architecturemining.interactionCentric.models.InteractionModelV2;
import org.architecturemining.interactionCentric.models.ParameterSettings;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;

public class ModelDiscoveryV2Plugin {

	@Plugin(
			name = "Reworked Interaction model plugin ",
			parameterLabels = { "Parameter Settings(Xlog included)"},
			returnLabels = { "Interaction model" },
			returnTypes = { InteractionModelV2.class },
			userAccessible = true,
			help = "This plugin will build a model based on the interactions found within an XLog and result in a linked list with sets of outgoing traces per node."
	)
	@UITopiaVariant(
            affiliation = "Utrecht University", 
            author = "Arnout Verhaar", 
            email = "w.d.verhaar@students.uu.nl"
    )
	public static InteractionModelV2 modelDiscovery(final UIPluginContext context, ParameterSettings iSettings) {	
		return new InteractionModelV2(iSettings);		
	}
	
	
}

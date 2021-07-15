package org.architecturemining.interactionCentric.io;

import org.architecturemining.interactionCentric.models.InteractionModel;
import org.architecturemining.interactionCentric.visualizer.ModelVisualizationUI;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;


/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Plugin that visualizes the found probability matrix within a graph view. 
*/
public class InteractionModelVisualizer {
	@Plugin(
			name = "Interaction Model Visualization plugin",
			parameterLabels = { "Interaction model"},
			returnLabels = { "Graph view" },
			returnTypes = { ModelVisualizationUI.class },
			userAccessible = true,
			help = "This plugin will visualize an Interaction Model"
	)
	@UITopiaVariant(
            affiliation = "Utrecht University", 
            author = "Arnout Verhaar", 
            email = "w.d.verhaar@students.uu.nl"
    )
	@Visualizer
	public static ModelVisualizationUI visualizeModel(final UIPluginContext context, InteractionModel iModel) {	
		return new ModelVisualizationUI(context, iModel);	
	}
}

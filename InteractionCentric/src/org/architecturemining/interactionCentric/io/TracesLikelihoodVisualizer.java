package org.architecturemining.interactionCentric.io;

import javax.swing.JComponent;

import org.architecturemining.interactionCentric.models.TracesLikelihood;
import org.architecturemining.interactionCentric.visualizer.RunnerPluginVisualUI;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;

public class TracesLikelihoodVisualizer {
	@Plugin(
			name = "Visualization for TracesLikelihood",
			parameterLabels = { "JComponent"},
			returnLabels = { "TracesLikelihood" },
			returnTypes = { JComponent.class },
			userAccessible = true,
			help = ""
	)
	@UITopiaVariant(
            affiliation = "Utrecht University", 
            author = "Arnout Verhaar", 
            email = "w.d.verhaar@students.uu.nl"
    )
	@Visualizer
	public static RunnerPluginVisualUI visualizer(final UIPluginContext context, TracesLikelihood tL) {	
		return new RunnerPluginVisualUI(context, tL);
	}
}

package org.architecturemining.interactionCentric.io;

import javax.swing.JComponent;

import org.architecturemining.interactionCentric.models.RunnerAnalysis;
import org.architecturemining.interactionCentric.visualizer.AnalyzerVisualUI;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;

public class TraceRunnerResultAnalyzerVisualizer {
	@Plugin(
			name = "Visualization for AnalyzerResult",
			parameterLabels = { "JComponent"},
			returnLabels = { "RunnerAnalysis" },
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
	public static AnalyzerVisualUI visualizer(final UIPluginContext context, RunnerAnalysis ra) {	
		return new AnalyzerVisualUI(context, ra);
	}
}

package org.architecturemining.interactions.visualisation;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.architecturemining.interactions.model.InteractionLog;
import org.architecturemining.interactions.visualisation.graph.InteractionGraph;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;

public class HierarchicalInteractionTextVisualizer {
	@Plugin(
			name = "Hierarchical Interaction Text Visualizer", 
			returnLabels = { "Debug Visualisation of a Hierarchical Interaction Model" }, 
			returnTypes = { JComponent.class }, 
			parameterLabels = { "Interaction Log to visualize" }, 
			userAccessible = true)
	@UITopiaVariant(
            affiliation = "Utrecht University", 
            author = "Tijmen de Jong", 
            email = "tijmendejong+prom@gmail.com"
    )
	@Visualizer
	public JComponent visualize(PluginContext context, InteractionLog log) {
		JPanel mainPanel = new JPanel();
		
		InteractionGraph graph = new InteractionGraph(log);
		
		JTextArea jl = new JTextArea(graph.toString());
		jl.setEditable(false);
		jl.setAutoscrolls(true);
		jl.setLineWrap(true);
		
		mainPanel.add(jl);
		
		return mainPanel;
	}
}

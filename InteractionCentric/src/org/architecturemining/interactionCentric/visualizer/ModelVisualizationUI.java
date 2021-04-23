package org.architecturemining.interactionCentric.visualizer;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import org.architecturemining.interactionCentric.models.InteractionModel;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;


/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Class that extends the JPanel and will actually show the panel as a result of the plugin.
*/

public class ModelVisualizationUI extends JPanel {
	
	// generated serial id
	private static final long serialVersionUID = 1757843017222865547L;
	
	public ModelVisualizationUI(PluginContext context, InteractionModel iModel) {
		this.setBackground(Color.white);
				
		// Create graph
		InteractionGraph graph = new InteractionGraph(iModel);

		this.setLayout(new BorderLayout());
		
		// 	Visualization panel
		ProMJGraphPanel visualisation = ProMJGraphVisualizer.instance().visualizeGraph(context, graph);  
		
		// Compose panel
		this.add(visualisation, BorderLayout.CENTER);	
	}
	
	

}

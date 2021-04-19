package org.architecturemining.interactions.visualisation.graph;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.architecturemining.interactions.model.InteractionLog;
import org.architecturemining.interactions.visualisation.components.InteractionPanel;
import org.deckfour.xes.model.XLog;
import org.jgraph.JGraph;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;

public class GraphPanel extends JPanel {

	private static final long serialVersionUID = -8995570111844269901L;
	
	private InteractionPanel interactionPanel;

	private InteractionLog log;

	private PluginContext context;
	
	private JFrame graphFrame;
		
	public GraphPanel(PluginContext context, InteractionLog log) {
		this.context = context;
		this.log = log;

		JPanel popupPanel = new JPanel();
		
		JButton popupButton = new JButton("Open graph window");
		
		// If the button is pressed, create a new popup window
		popupButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	graphFrame = new JFrame();
        		graphFrame.add(createGraphPanel());
        		graphFrame.setTitle("Interaction Graph Visualization");
        		// Make the new window cover the screen
                graphFrame.setExtendedState(graphFrame.getExtendedState() | graphFrame.MAXIMIZED_BOTH);
                graphFrame.setVisible(true);
            }
        });
		
		popupPanel.add(popupButton);
		
		this.add(popupPanel);
	}
	
	public boolean canExportInteractions() {
		return interactionPanel.selectionIsValidInteraction();
	}
	
	public XLog getInteractions() {
		return interactionPanel.exportInteractions();
	}
	
	private JPanel createGraphPanel() {
		JPanel container = new JPanel();
		
		// Create graph
		InteractionGraph graph = new InteractionGraph(log);
		
		// Initialize panels
		// 		Main panel
		container.setLayout(new BorderLayout());
		
		// 		Visualisation panel
		ProMJGraphPanel visualisation = ProMJGraphVisualizer.instance().visualizeGraph(context, graph);
		JGraph jGraph = visualisation.getGraph();
		
		// 		Interaction Panel
		// 			Displays whether the interactions associated with the selected nodes 
		interactionPanel = new InteractionPanel();
				
		// Initialize event handlers
		InteractionGraphListener iga = new InteractionGraphListener(jGraph, interactionPanel, graph);
		visualisation.getGraph().addGraphSelectionListener(iga);
		
		// Compose panel
		container.add(visualisation, BorderLayout.CENTER);
		container.add(interactionPanel, BorderLayout.PAGE_END);
		
		return container;
	}

	/**
	 * 	This method is called from apply method in the WizardStep 
	 */
	public void closePopup() {
		graphFrame.dispatchEvent(new WindowEvent(graphFrame, WindowEvent.WINDOW_CLOSING));
	}
}

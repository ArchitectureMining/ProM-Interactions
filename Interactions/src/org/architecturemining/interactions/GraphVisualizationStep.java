package org.architecturemining.interactions;

import javax.swing.JComponent;

import org.architecturemining.interactions.model.InteractionLog;
import org.architecturemining.interactions.visualisation.graph.GraphPanel;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;

/**
 * This class exists as a hack. It seems that in ProM it is not possible to 
 * truly register objects with the framework from a Visualizer. To circumvent
 * this limitation, we include the visualization as a step in the Wizard. This
 * might lead to some unpleasant flow of control as the mining logic needs to
 * be in the wizard. 
 * 
 * @author Tijmen
 *
 */
public class GraphVisualizationStep implements ProMWizardStep<XLog> {
	private GraphPanel nestedPanel;
	
	public GraphVisualizationStep(UIPluginContext context, InteractionLog log, GraphPanel nestedPanel) {
		this.nestedPanel = nestedPanel;
	}

	public XLog apply(XLog model, JComponent component) {
		nestedPanel.closePopup(); // Close the window when we finish the wizard
		return nestedPanel.getInteractions();
	}

	public boolean canApply(XLog model, JComponent component) {
		return nestedPanel.canExportInteractions();
	}

	public JComponent getComponent(XLog model) {
		return nestedPanel;
	}

	public String getTitle() {
		return "Select the interactions you would like to export";
	}

}

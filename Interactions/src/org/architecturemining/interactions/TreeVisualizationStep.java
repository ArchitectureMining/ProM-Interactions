package org.architecturemining.interactions;

import javax.swing.JComponent;

import org.architecturemining.interactions.model.InteractionLog;
import org.architecturemining.interactions.visualisation.tree.TreePanel;
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
public class TreeVisualizationStep implements ProMWizardStep<XLog> {
	private TreePanel nestedPanel;
	
	public TreeVisualizationStep(UIPluginContext context, InteractionLog log, TreePanel nestedPanel) {
		this.nestedPanel = nestedPanel;
	}

	public XLog apply(XLog model, JComponent component) {
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

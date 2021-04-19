package org.architecturemining.interactions;
import java.util.Set;

import org.architecturemining.interactions.model.InteractionBuilderSettings;
import org.architecturemining.interactions.util.XAttributeLiteralValues;
import org.architecturemining.interactions.visualisation.graph.GraphPanel;
import org.architecturemining.interactions.visualisation.tree.TreePanel;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.util.ui.wizard.ListWizard;
import org.processmining.framework.util.ui.wizard.ProMWizardDisplay;
import org.processmining.plugins.utils.ProvidedObjectHelper;

public class InteractionBuilder {
	@Plugin(
			name = "Interaction Builder",
			parameterLabels = { "XLog with caller and callee" },
			returnLabels = { "Interaction Log" },
			returnTypes = { InteractionBuilderSettings.class },
			userAccessible = true,
			help = "This plugin creates the objects necessary for Interaction Visualizer from an XLog"
	)
	@UITopiaVariant(
            affiliation = "Utrecht University", 
            author = "Tijmen de Jong", 
            email = "tijmendejong+prom@gmail.com"
    )
	public static InteractionBuilderSettings buildInteraction(final UIPluginContext context, XLog log) {
		// Create the settings model
		String[] attributes = getAttributeValues(context, log); 
		InteractionBuilderSettings iSettings = new InteractionBuilderSettings(attributes);
		// The mining logic is in the wizard. More explanation can be found in the VisualizationStep class
		ListWizard<InteractionBuilderSettings> wizard = new ListWizard<>(
				new InteractionBuilderStep(iSettings, log));
		iSettings = ProMWizardDisplay.show(context, wizard, iSettings);
		
		if (iSettings == null) {
			context.getFutureResult(0).cancel(true);
			return null;
		}
		
		return iSettings;
	}
	
	@Plugin(
			name = "Interaction to XLog (Graph View)",
			parameterLabels = { "Interaction Log" },
			returnLabels = { "Interactions as XLog" },
			returnTypes = { XLog.class },
			userAccessible = true,
			help = "This plugin visualizes an Interaction Log as a graph and"
					+ " allows users to export interactions"
	)
	@UITopiaVariant(
            affiliation = "Utrecht University", 
            author = "Tijmen de Jong", 
            email = "tijmendejong+prom@gmail.com"
    )
	public static XLog exportInteractionGraphView(final UIPluginContext context, InteractionBuilderSettings model) {
		GraphPanel nestedPanel = new GraphPanel(context, model.getInteractionLog());
		
		ListWizard<XLog> wizard = new ListWizard<>(
				new GraphVisualizationStep(context, model.getInteractionLog(), nestedPanel));
		
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		 
		XLog interactions = ProMWizardDisplay.show(context, wizard, factory.createLog());
		
		if (interactions == null) {
			context.getFutureResult(0).cancel(true);
			return null;
		}
		
		// Add code that gets the XLog of the interactions here
		ProvidedObjectHelper.setFavorite(context, interactions);
		return interactions;
	}
	
	@Plugin(
			name = "Interaction to XLog (Tree View)",
			parameterLabels = { "Interaction Log" },
			returnLabels = { "Interactions as XLog" },
			returnTypes = { XLog.class },
			userAccessible = true,
			help = "This plugin visualizes an Interaction Log as a set of trees and"
					+ " allows users to export interactions"
	)
	@UITopiaVariant(
            affiliation = "Utrecht University", 
            author = "Tijmen de Jong", 
            email = "tijmendejong+prom@gmail.com"
    )
	public static XLog exportInteractionTreeView(final UIPluginContext context, InteractionBuilderSettings model) {
		TreePanel nestedPanel = new TreePanel(context, model.getInteractionLog());
		
		ListWizard<XLog> wizard = new ListWizard<>(
				new TreeVisualizationStep(context, model.getInteractionLog(), nestedPanel));
		
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		 
		XLog interactions = ProMWizardDisplay.show(context, wizard, factory.createLog());
		
		if (interactions == null) {
			context.getFutureResult(0).cancel(true);
			return null;
		}
		
		// Add code that gets the XLog of the interactions here
		ProvidedObjectHelper.setFavorite(context, interactions);
		return interactions;
	}
	
	/**
	 * 
	 * @param context	Plugin Context for updating the progress bar
	 * @param log		An XLog
	 * @return			The keys of all attributes in the log
	 */
	private static String[] getAttributeValues(UIPluginContext context, XLog log) {
    	XAttributeLiteralValues attributeValues = new XAttributeLiteralValues(log);
    	context.getProgress().setMaximum(100);
    	context.getProgress().setValue(5);
    	Set<String> attributeKeySet = attributeValues.getAllAttributeKeys();
    	context.getProgress().setValue(25);
    	    	
    	String[] attributes = attributeKeySet.toArray(new String[attributeKeySet.size()]);
    	context.getProgress().setValue(30);
    	
    	return attributes;
	}

}

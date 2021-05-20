package org.architecturemining.interactionCentric.plugins;

import java.util.Set;

import org.architecturemining.interactionCentric.models.ParameterSettings;
import org.architecturemining.interactionCentric.util.XAttributeLiteralValues;
import org.architecturemining.interactionCentric.wizard.ParameterSettingsWizardBuilder;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.util.ui.wizard.ListWizard;
import org.processmining.framework.util.ui.wizard.ProMWizardDisplay;

/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Plugin that will show a popup with dropdown boxes to enable the user to set specific parameters which are needed for the DiscoveryPlugin.
*/

public class ParameterSettingsPlugin {
	@Plugin(
			name = "Interaction Network Parameter Settings",
			parameterLabels = { "Xlog with interactions"},
			returnLabels = { "Parameter Settings" },
			returnTypes = { ParameterSettings.class },
			userAccessible = true,
			help = "This plugin creates the objects necessary for Interaction Network Discovery Plugin from an XLog"
	)
	@UITopiaVariant(
            affiliation = "Utrecht University", 
            author = "Arnout Verhaar", 
            email = "w.d.verhaar@students.uu.nl"
    )
	public static ParameterSettings parameterPlugin(final UIPluginContext context, XLog log) {	
		String[] attributes = getAttributeValues(context, log); 
		ParameterSettings iSettings = new ParameterSettings(attributes, log);
		
		ListWizard<ParameterSettings> wizard = new ListWizard<>(
				new ParameterSettingsWizardBuilder(iSettings, log));
		iSettings = ProMWizardDisplay.show(context, wizard, iSettings);
		
		if (iSettings == null) {
			context.getFutureResult(0).cancel(true);
			return null;
		}
		
		return iSettings;
		
	}
	
	private static String[] getAttributeValues(UIPluginContext context, XLog log) {
    	XAttributeLiteralValues attributeValues = new XAttributeLiteralValues(log);
    	Set<String> attributeKeySet = attributeValues.getAllAttributeKeys(); 	
    	return attributeKeySet.toArray(new String[attributeKeySet.size()]);
	}
}

package org.architecturemining.interactionCentric.plugins;

import java.io.IOException;
import java.util.Arrays;
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
import org.processmining.log.csv.CSVFile;
import org.processmining.log.csv.config.CSVConfig;
import org.processmining.log.csvimport.CSVConversion;
import org.processmining.log.csvimport.CSVConversion.ConversionResult;
import org.processmining.log.csvimport.config.CSVConversionConfig;
import org.processmining.log.csvimport.exception.CSVConversionException;

/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Plugin that will show a popup with dropdown boxes to enable the user to set specific parameters which are needed for the DiscoveryPlugin.
*/

public class ParameterSettingsPlugin {
	@Plugin(
			name = "Interaction Network Parameter Settings from CSV",
			parameterLabels = { "CSV with interactions"},
			returnLabels = { "Parameter Settings" },
			returnTypes = { ParameterSettings.class },
			userAccessible = true,
			help = "This plugin parses the csv to a xes file and requests the input parameters. "
	)
	@UITopiaVariant(
            affiliation = "Utrecht University", 
            author = "Arnout Verhaar", 
            email = "w.d.verhaar@students.uu.nl"
    )
	public static ParameterSettings parameterPlugin(final UIPluginContext context, CSVFile log) {	
		
		
		
		
		CSVConfig importConfig;
		String[] attributes = new String[0];
		try {
			importConfig = new CSVConfig(log);
			attributes = log.readHeader(importConfig);
		} catch (CSVConversionException | IOException e) {
			e.printStackTrace();
			
		}
		
		ParameterSettings iSettings = new ParameterSettings(attributes, log);
		ListWizard<ParameterSettings> wizard = new ListWizard<>(
				new ParameterSettingsWizardBuilder<Object>(iSettings));
		iSettings = ProMWizardDisplay.show(context, wizard, iSettings);
		
		
		if (iSettings == null) {
			context.getFutureResult(0).cancel(true);
			return null;
		}
		
		System.out.println(iSettings);
		
		XLog xesLog = createXesLogFromCSV(log, iSettings);
		iSettings.setLog(xesLog);
		
		return iSettings;
		
	}

	
	private static XLog createXesLogFromCSV(CSVFile csvFile, ParameterSettings iSettings) {
		try {
			//CSVFile csvFile = new CSVFileReferenceUnivocityImpl(file.toPath());
			CSVConfig importConfig = new CSVConfig(csvFile);
			CSVConversionConfig conversionConfig = new CSVConversionConfig(csvFile, importConfig);
			conversionConfig.autoDetect();
			
			//Preprocessing
			conversionConfig.setCaseColumns(Arrays.asList(iSettings.getCaseID()));
			
			
			//XES conversion
			CSVConversion converter = new CSVConversion();
			ConversionResult<XLog> result = converter.doConvertCSVToXES(csvFile, importConfig, conversionConfig);
			converter.setMaxSortingMemory(0);
			
			System.out.println(result.getConversionErrors());
			
			return result.getResult();
			
		} catch (CSVConversionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	//Additional option to import an XES file instead of .csv which will prevent needing to parse the complete file again.
	@Plugin(
			name = "Interaction Network Parameter Settings from XES",
			parameterLabels = { "Xlog with interactions"},
			returnLabels = { "Parameter Settings" },
			returnTypes = { ParameterSettings.class },
			userAccessible = true,
			help = "This plugin takes a XES file and requests the input parameters. "
	)
	@UITopiaVariant(
            affiliation = "Utrecht University", 
            author = "Arnout Verhaar", 
            email = "w.d.verhaar@students.uu.nl"
    )
	public static ParameterSettings parameterPluginXes(final UIPluginContext context, XLog log) {	

		String[] attributes = getAttributeValues(log); 	
		ParameterSettings iSettings = new ParameterSettings(attributes, log);
		ListWizard<ParameterSettings> wizard = new ListWizard<>(
				new ParameterSettingsWizardBuilder<Object>(iSettings));
		iSettings = ProMWizardDisplay.show(context, wizard, iSettings);		
		if (iSettings == null) {
			context.getFutureResult(0).cancel(true);
			return null;
		}		
		return iSettings;	
	}
	
	private static String[] getAttributeValues(XLog log) {
    	XAttributeLiteralValues attributeValues = new XAttributeLiteralValues(log);
    	Set<String> attributeKeySet = attributeValues.getAllAttributeKeys();	    	
    	String[] attributes = attributeKeySet.toArray(new String[attributeKeySet.size()]);
	
    	return attributes;
	}
}

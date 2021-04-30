package org.architecturemining.interactionCentric.io;

import java.io.InputStream;

import org.architecturemining.interactionCentric.models.InteractionModel;
import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;

import com.fasterxml.jackson.databind.ObjectMapper;

@Plugin(
		name = "Import InteractionModel",
		parameterLabels = { "Filename" },
		returnLabels = { "InteractionModel" },
		returnTypes = { InteractionModel.class })
@UIImportPlugin(description = "Model importer", extensions = {"imod"})

public class InteractionModelImporter extends AbstractImportPlugin{

	@Override
	protected InteractionModel importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes) throws Exception {		
		// Fill in object from input
		ObjectMapper objectReader = new ObjectMapper();
		InteractionModel i = objectReader.readValue(input, InteractionModel.class);	
		return i;
	}
}

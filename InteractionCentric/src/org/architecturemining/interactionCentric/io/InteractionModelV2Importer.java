package org.architecturemining.interactionCentric.io;

import java.io.InputStream;

import org.architecturemining.interactionCentric.models.InteractionModelV2;
import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;

import com.fasterxml.jackson.databind.ObjectMapper;

@Plugin(
		name = "Import InteractionModelV2",
		parameterLabels = { "Filename" },
		returnLabels = { "InteractionModelV2" },
		returnTypes = { InteractionModelV2.class })
@UIImportPlugin(description = "InteractionModel importer", extensions = {"imod2"})
public class InteractionModelV2Importer extends AbstractImportPlugin{
	
	@Override
	protected InteractionModelV2 importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes) throws Exception {		
		// Fill in object from input
		ObjectMapper objectReader = new ObjectMapper();
		InteractionModelV2 i = objectReader.readValue(input, InteractionModelV2.class);	
		return i;
	}

}

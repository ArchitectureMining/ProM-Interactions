package org.architecturemining.interactionCentric.io;

import java.io.InputStream;

import org.architecturemining.interactionCentric.models.InteractionNetwork;
import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;

import com.fasterxml.jackson.databind.ObjectMapper;

@Plugin(
		name = "Import Interaction Network",
		parameterLabels = { "Filename" },
		returnLabels = { "InteractionNetwork" },
		returnTypes = { InteractionNetwork.class })
@UIImportPlugin(description = "InteractionModel importer", extensions = {"imod2"})
public class InteractionNetworkImporter extends AbstractImportPlugin{
	
	@Override
	protected InteractionNetwork importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes) throws Exception {		
		// Fill in object from input
		ObjectMapper objectReader = new ObjectMapper();
		InteractionNetwork i = objectReader.readValue(input, InteractionNetwork.class);	
		System.out.println("Imported Interaction Network:");
		System.out.println(i.network.traceNodes.size());
		return i;
	}

}

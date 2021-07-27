package org.architecturemining.interactionCentric.io;

import java.io.InputStream;

import org.architecturemining.interactionCentric.models.TracesLikelihood;
import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;

import com.fasterxml.jackson.databind.ObjectMapper;


@Plugin(
		name = "Import TracesLikelihood",
		parameterLabels = { "Filename" },
		returnLabels = { "Likelihood per Trace" },
		returnTypes = { TracesLikelihood.class })
@UIImportPlugin(description = "TracesLikelihood importer", extensions = {"tsl"})
public class TracesLikelihoodImporter extends AbstractImportPlugin{
	
	@Override
	protected TracesLikelihood importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes) throws Exception {		
		// Fill in object from input
		ObjectMapper objectReader = new ObjectMapper();
		TracesLikelihood i = objectReader.readValue(input, TracesLikelihood.class);	
		System.out.println("Imported TracesLikelihood:");
		System.out.println(i.getTraces().size());
		return i;
	}

}

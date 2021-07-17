package org.architecturemining.interactionCentric.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.architecturemining.interactionCentric.models.TracesLikelihood;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Plugin( 
		name="TracesLikelihood Exporter", 
		returnLabels = {}, 
		returnTypes = {}, 
		parameterLabels = {"TracesLikelihood", "File"}, 
		userAccessible= true
	)
	@UIExportPlugin( 
			description="TracesLikelihood Exporter", 
	        extension = "tsl"
	)
public class TracesLikelihoodExporter {

	@UITopiaVariant(affiliation = "Utrecht University",
			author = "W.D. Verhaar",
			email = "w.d.verhaar@students.uu.nl")
	@PluginVariant(requiredParameterLabels = { 0, 1 })
	public void exportModel(UIPluginContext context, TracesLikelihood TL, File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		PrintWriter pwriter = new PrintWriter(writer);
		// write object as a json object.
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(TL);
		pwriter.print(json);
		pwriter.close();
	}
	
}

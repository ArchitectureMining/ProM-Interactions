package org.architecturemining.interactionCentric.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.architecturemining.interactionCentric.models.RunnerAnalysis;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Plugin( 
		name="RunnerAnalysis Exporter", 
		returnLabels = {}, 
		returnTypes = {}, 
		parameterLabels = {"RunnerAnalysis", "File"}, 
		userAccessible= true
	)
	@UIExportPlugin( 
			description="RunnerAnalysis Exporter", 
	        extension = "ra"
	)
public class TraceRunnerAnalysisExporter {	
	@UITopiaVariant(affiliation = "Utrecht University",
			author = "W.D. Verhaar",
			email = "w.d.verhaar@students.uu.nl")
	@PluginVariant(requiredParameterLabels = { 0, 1 })
	public void exportModel(UIPluginContext context, RunnerAnalysis onto, File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		PrintWriter pwriter = new PrintWriter(writer);
		// write object as a json object.
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(onto);
		pwriter.print(json);
		pwriter.close();
	}
}

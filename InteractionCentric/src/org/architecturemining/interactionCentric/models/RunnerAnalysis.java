package org.architecturemining.interactionCentric.models;

import java.util.Map;

import org.architecturemining.interactionCentric.plugins.TraceRunnerResultAnalzyer.SingleVertexAnalysis;

public class RunnerAnalysis {

	public Map<String, SingleVertexAnalysis> vertexAnalysis;

	public RunnerAnalysis(Map<String, SingleVertexAnalysis> vertexAnalysis) {
		super();
		this.vertexAnalysis = vertexAnalysis;
	}

}

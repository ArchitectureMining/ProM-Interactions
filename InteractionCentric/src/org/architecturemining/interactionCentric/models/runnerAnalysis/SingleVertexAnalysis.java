package org.architecturemining.interactionCentric.models.runnerAnalysis;

import java.util.ArrayList;
import java.util.List;

public class SingleVertexAnalysis {
	
	public List<SingleTraceVertexInfo> outgoingTraces;
	public List<SingleTraceVertexInfo> incomingTraces;
	public String vertexName;
	
	public SingleVertexAnalysis(String vertexName) {
		this.vertexName = vertexName;
		outgoingTraces = new ArrayList<SingleTraceVertexInfo>();
		incomingTraces = new ArrayList<SingleTraceVertexInfo>();
	}

	public List<SingleTraceVertexInfo> getOutgoingTraces() {
		return outgoingTraces;
	}

	public void setOutgoingTraces(List<SingleTraceVertexInfo> outgoingTraces) {
		this.outgoingTraces = outgoingTraces;
	}

	public List<SingleTraceVertexInfo> getIncomingTraces() {
		return incomingTraces;
	}

	public void setIncomingTraces(List<SingleTraceVertexInfo> incomingTraces) {
		this.incomingTraces = incomingTraces;
	}

	public String getVertexName() {
		return vertexName;
	}

	public void setVertexName(String vertexName) {
		this.vertexName = vertexName;
	}
	
	
}
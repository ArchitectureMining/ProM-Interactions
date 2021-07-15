package org.architecturemining.interactionCentric.models;

import java.util.List;

import org.architecturemining.interactionCentric.util.XESFunctions;

public class TracesLikelihood {
	
	public List<SingleLikelihood> traces;
	public XESFunctions xes;
	
	public TracesLikelihood(List<SingleLikelihood> traces, XESFunctions xes) {
		super();
		this.traces = traces;
		this.xes = xes;
	}

	public List<SingleLikelihood> getTraces() {
		return traces;
	}

	public void setTraces(List<SingleLikelihood> traces) {
		this.traces = traces;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(SingleLikelihood sl: traces) {
			sb.append(sl.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
}

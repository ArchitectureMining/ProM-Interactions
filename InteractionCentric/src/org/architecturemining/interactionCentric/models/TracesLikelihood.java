package org.architecturemining.interactionCentric.models;

import java.util.List;

public class TracesLikelihood {
	
	public List<SingleLikelihood> traces;
	
	public TracesLikelihood(List<SingleLikelihood> traces) {
		super();
		this.traces = traces;
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

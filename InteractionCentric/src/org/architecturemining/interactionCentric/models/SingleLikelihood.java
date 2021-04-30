package org.architecturemining.interactionCentric.models;

import org.architecturemining.interactionCentric.models.PointerList.PointerList;
import org.deckfour.xes.model.XTrace;

public class SingleLikelihood {

	double likelihood;
	XTrace trace;
	PointerList pointers;
	
	public SingleLikelihood() {
		super();
	}
	public SingleLikelihood(double likelihood, XTrace trace, String caller, String callee) {
		super();
		this.likelihood = likelihood;
		this.trace = trace;
		this.pointers = new PointerList(trace, caller, callee);
	}
	public double getLikelihood() {
		return likelihood;
	}
	public void setLikelihood(double likelihood) {
		this.likelihood = likelihood;
	}
	public XTrace getTrace() {
		return trace;
	}
	public void setTrace(XTrace trace) {
		this.trace = trace;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(likelihood + " | ");
		sb.append(pointers.toString());
		return sb.toString();
	}
	
}

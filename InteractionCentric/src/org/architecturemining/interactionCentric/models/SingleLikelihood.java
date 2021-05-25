package org.architecturemining.interactionCentric.models;

import java.util.Map;

import org.architecturemining.interactionCentric.models.PointerList.PointerList;
import org.deckfour.xes.model.XTrace;

public class SingleLikelihood {

	double computedLikelihood;
	Map<String, Double> likelihoodPerInteraction; 
	XTrace trace;
	public PointerList pointers;
	
	public SingleLikelihood() {
		super();
	}
	public SingleLikelihood(double computedLikelihood, XTrace trace, String caller, String callee, Map<String, Double> likelihoodPerInteraction) {
		super();
		this.computedLikelihood = computedLikelihood;
		this.trace = trace;
		this.pointers = new PointerList(trace, caller, callee, likelihoodPerInteraction);
		this.likelihoodPerInteraction = likelihoodPerInteraction;
	}
	public double getLikelihood() {
		return computedLikelihood;
	}
	public void setLikelihood(double computedLikelihood) {
		this.computedLikelihood = computedLikelihood;
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
		sb.append(computedLikelihood + " | ");
		sb.append(pointers.toString());
		return sb.toString();
	}
	
}

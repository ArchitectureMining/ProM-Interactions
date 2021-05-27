package org.architecturemining.interactionCentric.models;

import java.util.Map;
import java.util.Set;

import org.deckfour.xes.model.XTrace;

public class SingleLikelihood {

	
	Map<String, Set<String>> edgeMap;
	Map<String, Double> likelihoods;
	XTrace trace;
	
	public SingleLikelihood() {
		super();
	}
	
	public SingleLikelihood(Map<String, Set<String>> edgeMap, Map<String, Double> likelihoods, XTrace trace) {
		super();
		this.edgeMap = edgeMap;
		this.likelihoods = likelihoods;
		this.trace = trace;
	}



	public Map<String, Double> getLikelihood() {
		return likelihoods;
	}
	public Double getLikelihood(String function) {
		return likelihoods.get(function);
	}

	public void setLikelihood(Map<String, Double> likelihood) {
		this.likelihoods = likelihood;
	}

	public Map<String, Set<String>> getEdgeMap() {
		return edgeMap;
	}

	public void setEdgeMap(Map<String, Set<String>> edgeMap) {
		this.edgeMap = edgeMap;
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
		sb.append(getLikelihood() + " | ");
		return sb.toString();
	}
	
}

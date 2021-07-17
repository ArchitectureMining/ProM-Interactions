package org.architecturemining.interactionCentric.models;

import java.util.Map;

import org.architecturemining.interactionCentric.models.LinkedListEdgesSet.EdgeMap;
import org.deckfour.xes.model.XTrace;

public class SingleLikelihood {

	
	EdgeMap edgeMap;
	public Map<String, Double> likelihoods;
	Map<String, Double> edgeProbabilities;
	Map<String, Boolean> behaviour;
	public String trackingID;
	
	public SingleLikelihood() {
		super();
	}
	
	public SingleLikelihood(EdgeMap edgeMap, Map<String, Double> likelihoods, Map<String, Double> edgeProbabilities, XTrace trace, Map<String, Boolean> behaviour) {
		super();
		this.edgeMap = edgeMap;
		this.likelihoods = likelihoods;
		this.behaviour = behaviour;
		this.edgeProbabilities = edgeProbabilities;
		this.trackingID = trace.getAttributes().get("concept:name").toString();
	}
	
	

	public String getTrackingID() {
		return trackingID;
	}

	public void setTrackingID(String trackingID) {
		this.trackingID = trackingID;
	}

	public Map<String, Double> getLikelihood() {
		return likelihoods;
	}
	public Double getLikelihood(String function) {
		return likelihoods.get(function);
	}

	public EdgeMap getEdgeMap() {
		return edgeMap;
	}

	public void setEdgeMap(EdgeMap edgeMap) {
		this.edgeMap = edgeMap;
	}

	public Map<String, Double> getLikelihoods() {
		return likelihoods;
	}

	public void setLikelihoods(Map<String, Double> likelihoods) {
		this.likelihoods = likelihoods;
	}

	public Map<String, Boolean> getBehaviour() {
		return behaviour;
	}

	public void setBehaviour(Map<String, Boolean> behaviour) {
		this.behaviour = behaviour;
	}

	public Map<String, Double> getEdgeProbabilities() {
		return edgeProbabilities;
	}

	public void setEdgeProbabilities(Map<String, Double> edgeProbabilities) {
		this.edgeProbabilities = edgeProbabilities;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getLikelihood() + " | ");
		return sb.toString();
	}
	
}

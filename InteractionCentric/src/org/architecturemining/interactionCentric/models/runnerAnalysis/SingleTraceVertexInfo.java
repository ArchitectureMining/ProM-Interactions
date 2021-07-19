package org.architecturemining.interactionCentric.models.runnerAnalysis;

public class SingleTraceVertexInfo {
	
	public double likelihood;
	public String trackingID;

	public SingleTraceVertexInfo(double likelihood, String trackingID) {
		super();
		this.likelihood = likelihood;
		this.trackingID = trackingID;

	}

	public double getLikelihood() {
		return likelihood;
	}

	public void setLikelihood(double likelihood) {
		this.likelihood = likelihood;
	}

	public String getTrackingID() {
		return trackingID;
	}

	public void setTrackingID(String trackingID) {
		this.trackingID = trackingID;
	}
	
	
}
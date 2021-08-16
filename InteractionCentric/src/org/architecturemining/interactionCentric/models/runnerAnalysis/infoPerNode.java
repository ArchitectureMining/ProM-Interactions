package org.architecturemining.interactionCentric.models.runnerAnalysis;

import org.python.icu.text.DecimalFormat;

public class infoPerNode {
	public double averageTotal, averageIncoming, averageOutgoing;
	public String nodeName;
	
	public infoPerNode(String nodeName, double averageTotal, double averageIncoming, double averageOutgoing) {
		super();
		DecimalFormat df2 = new DecimalFormat("#.###");

		//String likelihood = df2.format(selection.getEdgeProbabilities().get(likelihoodCalculation));
		this.averageTotal = averageTotal;
		this.averageIncoming = averageIncoming;
		this.averageOutgoing = averageOutgoing;
		this.nodeName = nodeName;
	}

	public double getAverageTotal() {
		return averageTotal;
	}

	public void setAverageTotal(double averageTotal) {
		this.averageTotal = averageTotal;
	}
	
	
}
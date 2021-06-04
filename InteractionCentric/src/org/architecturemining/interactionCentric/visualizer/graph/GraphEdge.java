package org.architecturemining.interactionCentric.visualizer.graph;

import java.text.DecimalFormat;

import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Implementaton of the edge used in the InteractionGraph. 
 * An edge consists out of 2 nodes and a weight(strength)
*/
public class GraphEdge extends DefaultWeightedEdge{

	private GraphNode source, target;
	private double strength;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

	
	public GraphEdge(GraphNode source, GraphNode target, double strength) {
		this.source = source;
		this.target = target;
		this.strength = strength;
	}
	
	@Override
	public String toString() {
		if(strength > 0) {
			return df2.format(strength);
		}else
			return "";//df2.format(strength);
		//return source.getLabel() + " -> " + target.getLabel();
	}


	public GraphNode getSource() {
		return source;
	}

	public GraphNode getTarget() {
		return target;
	}

	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

}

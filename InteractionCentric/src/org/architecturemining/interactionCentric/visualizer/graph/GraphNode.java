package org.architecturemining.interactionCentric.visualizer.graph;

import org.processmining.models.graphbased.NodeID;

/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Implementation of the node used in the InteractionGraph. 
 * Each node contains an id and a name.  
*/

public class GraphNode{

	private NodeID id;
	// The FQN including ID of the object represented in this node. The label is more concise
	public String fullName;

	
	public GraphNode(String name) {
		this.fullName = name;
		this.id = new NodeID();
	
	}
	
	public NodeID getId() {
		return this.id;
	}

	public String getLabel() {
		return this.fullName;
	}
	
	public String toString() {
		return this.fullName;
	}



}

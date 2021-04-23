package org.architecturemining.interactionCentric.visualizer.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Set;

import javax.swing.SwingConstants;

import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.NodeID;
import org.processmining.models.graphbased.directed.ContainableDirectedGraphElement;
import org.processmining.models.graphbased.directed.ContainingDirectedGraphNode;
import org.processmining.models.graphbased.directed.DirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraphNode;

/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Implementation of the node used in the InteractionGraph. 
 * Each node contains an id and a name.  
*/

public class GraphNode implements ContainingDirectedGraphNode, ContainableDirectedGraphElement{

	private NodeID id;
	// The FQN including ID of the object represented in this node. The label is more concise
	private String fullName;
	private AttributeMap attributes;
	

	
	public GraphNode(String name) {
		this.fullName = name;
		this.id = new NodeID();
		this.attributes = new AttributeMap();
		
		getAttributeMap().put(AttributeMap.AUTOSIZE, true);
		getAttributeMap().put(AttributeMap.RESIZABLE, false);
		getAttributeMap().put(AttributeMap.FILLCOLOR, new Color(.95F, .95F, .95F, .1F));
		getAttributeMap().put(AttributeMap.LABELVERTICALALIGNMENT, SwingConstants.CENTER);
		getAttributeMap().put(AttributeMap.PREF_ORIENTATION, SwingConstants.WEST);
		getAttributeMap().put(AttributeMap.LABEL, name);
		getAttributeMap().put(AttributeMap.EDGECOLOR, org.fusesource.jansi.Ansi.Color.BLUE);
		getAttributeMap().put(AttributeMap.SHAPE, new org.processmining.models.shapes.Ellipse());		
	}
	
	public NodeID getId() {
		return this.id;
	}

	public String getLabel() {
		return this.fullName;
	}

	public DirectedGraph<?, ?> getGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	public AttributeMap getAttributeMap() {
		return attributes;
	}

	public int compareTo(DirectedGraphNode o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Dimension getCollapsedSize() {
		return new Dimension(80, 40); //  nog geen idee wat dit doet...
	}

	public ContainingDirectedGraphNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<? extends ContainableDirectedGraphElement> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addChild(ContainableDirectedGraphElement child) {
		// TODO Auto-generated method stub
		
	}

}

package org.architecturemining.interactions.visualisation.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.SwingConstants;

import org.deckfour.xes.model.XEvent;
import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.NodeID;
import org.processmining.models.graphbased.directed.ContainableDirectedGraphElement;
import org.processmining.models.graphbased.directed.ContainingDirectedGraphNode;
import org.processmining.models.graphbased.directed.DirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraphNode;

public class InteractionGraphNode implements ContainingDirectedGraphNode, ContainableDirectedGraphElement {

	private InteractionGraph model;
	private NodeID id;
	// The FQN including ID of the object represented in this node. The label is more concise
	private String fullName;
	
	private InteractionGraphNode parent;
	private HashSet<ContainableDirectedGraphElement> children;
	
	private AttributeMap attributes;
	
	/**
	 * Notice that this is not a list of InteractionGraphEdges (or any other 
	 * DirectedGraphEdge). Instead it is a list of nodes that, specifically
	 * the head nodes of all the edges of which this node is the tail.
	 * 
	 * Simply put: 
	 * 	tail: 	this (InteractionGraphNode)
	 * 	edges: 	InteractionGraphEdges stored in the model (!)
	 * 	heads:	The InteractionGraphNodes in the edges field below
	 */
	private HashMap<String, InteractionGraphNode> edges;
	
	/**
	 * @param name			FQN of the object represented in this node
	 * @param model			Model to which this node belongs
	 */
	public InteractionGraphNode(String name, InteractionGraph model) {
		this.model = model;
		this.id = new NodeID();
		this.fullName = name;
		
		this.attributes = new AttributeMap();
		this.children = new HashSet<>();
		this.edges =  new HashMap<>();
		
		getAttributeMap().put(AttributeMap.SIZE, new Dimension(80, 40));
		getAttributeMap().put(AttributeMap.RESIZABLE, true);
		getAttributeMap().put(AttributeMap.FILLCOLOR, new Color(.95F, .95F, .95F, .1F));
		getAttributeMap().put(AttributeMap.LABELVERTICALALIGNMENT, SwingConstants.TOP);
		getAttributeMap().put(AttributeMap.PREF_ORIENTATION, SwingConstants.WEST);
		getAttributeMap().put(AttributeMap.LABEL, name);
	}
	
	/**
	 * The same as the other constructor, there is a long and a short name
	 * 
	 * @param fullName		FQN of the object represented in this node
	 * @param model			Model to which this node belongs
	 * @param shortName		Short name of the node, used for the visualisation only
	 */
	public InteractionGraphNode(String fullName, InteractionGraph model, String shortName) {
		this(shortName, model);
		this.fullName = fullName;
	}

	/**
	 * This is the method that should be called when creating an edge in an
	 * InteractionGraph. It handles the creation of InteractionGraphEdges,
	 * adding them to the main graph and adding them to the node itself.
	 * 
	 * @param node		The head node of the edge. Remember:
	 * 						tail => head
	 * 					=	this object, InteractionGraphEdge, node 
	 * @param event		The XEvents that are the XES equivalents of the 
	 * 						interactions represented in this new node.
	 * @return			A reference to the current node, which is useful
	 * 						when creating a graph.
	 */
	public InteractionGraphNode addEdge(InteractionGraphNode node, XEvent event) {
		String headLabel = node.getLabel();
		
		if (edges.containsKey(headLabel)) {
			InteractionGraphEdge edge = model.getEdge(InteractionGraphEdge.buildEdgeLabel(getLabel(), headLabel));
			edge.addEvent(event);
		} else {
			// The InteractionGraphEdge constructor accepts a list but we only have a single event
			ArrayList<XEvent> eventList = new ArrayList<>();
			eventList.add(event);
			
			InteractionGraphEdge edge = new InteractionGraphEdge(this, node, model, eventList);
			
			model.addEdge(edge);
			edges.put(headLabel, node);
		}
				
		return this;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NODE: ");
		sb.append(this.getLabel());
		sb.append("\n");
		sb.append("  edges: ");
		for(InteractionGraphNode n: edges.values()) {
			sb.append(n.getLabel());
			sb.append(", ");
		}
		sb.append("\n");
		sb.append("  children: ");
		for(ContainableDirectedGraphElement n: children) {
			if (n instanceof InteractionGraphNode) {
				sb.append(((InteractionGraphNode) n).getLabel());
				sb.append(", ");
			}
		}
		sb.append("\n");
		if (this.getParent() != null) {
			sb.append("  parent: ");
			sb.append(this.getParent().getLabel());
			sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}
	
	/**
	 * @param name		The short name of the node used for visualization
	 */
	public void setLabel(String name) {
		getAttributeMap().put(AttributeMap.LABEL, name);
	}
	
	/**
	 * @return	The list of head nodes of all the edges of which this node is
	 * the tail. See the comment above the edges field.
	 */
	public List<InteractionGraphNode> getEdges() {
		return new LinkedList<InteractionGraphNode>(edges.values());
	}
	
	public InteractionGraphNode addEdge(InteractionGraphNode node) {
		edges.put(node.getLabel(), node);
		
		InteractionGraphEdge edge = new InteractionGraphEdge(this, node, model, null);
		model.addEdge(edge);
		
		return this;
	}

	public String getLabel() {
		return fullName;
	}
	
	public String getShortLabel() {
		return (String) getAttributeMap().get(AttributeMap.LABEL);
	}

	public DirectedGraph<?, ?> getGraph() {
		return model;
	} 
	
	public AttributeMap getAttributeMap() {
		return attributes;
	}

	public int compareTo(DirectedGraphNode node) {
		int comp = getId().compareTo(node.getId());
		return comp;
	}

	public NodeID getId() {
		return id;
	}

	public Dimension getCollapsedSize() {
		return new Dimension(80, 40);
	}

	public Set<? extends ContainableDirectedGraphElement> getChildren() {
		return children;
	}

	public void addChild(ContainableDirectedGraphElement child) {
		children.add(child);
	}

	public ContainingDirectedGraphNode getParent() {
		return parent;
	}
	
	public boolean isRoot() {
		return (parent == null);
	}

	public void setParent(InteractionGraphNode parent) {
		this.parent = parent;
		parent.addChild(this);
	}
}

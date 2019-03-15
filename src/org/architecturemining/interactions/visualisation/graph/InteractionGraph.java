package org.architecturemining.interactions.visualisation.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.architecturemining.interactions.model.Interaction;
import org.architecturemining.interactions.model.InteractionLog;
import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.directed.DirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;
import org.processmining.models.graphbased.directed.DirectedGraphNode;

public class InteractionGraph implements DirectedGraph<InteractionGraphNode, InteractionGraphEdge> {
	
	private AttributeMap attributes = new AttributeMap();;
	private HashMap<String, InteractionGraphNode> nodes = new HashMap<>();
	private HashMap<String, InteractionGraphEdge> edges = new HashMap<>();
	
	/**
	 * Create an InteractionGraph from an InteractionLog
	 * 
	 * @param log
	 */
	public InteractionGraph(InteractionLog log) {
		HashMap<String, String[]> mapping = log.getMapping().getMapping();
		
		for (Interaction ia : log.getLog()) {
			// Add the caller and callee nodes
			String[] callerHierarchy = mapping.get(ia.getCaller());
			
			InteractionGraphNode caller = addNodeIfNotExists(ia.getCaller(), callerHierarchy);
			InteractionGraphNode callee = addNodeIfNotExists(ia.getCallee(), mapping.get(ia.getCallee()));

			caller.addEdge(callee, ia.getEvent());
		}
	}
	
	/**
	 * Create a mock InteractionGraph
	 */
	public InteractionGraph() {
		InteractionGraphNode x = new InteractionGraphNode("x", this);
		InteractionGraphNode y = new InteractionGraphNode("y", this);
		
		InteractionGraphNode a = new InteractionGraphNode("a", this);
		InteractionGraphNode b = new InteractionGraphNode("b", this);
		InteractionGraphNode c = new InteractionGraphNode("c", this);
		
		a.setParent(x);
		b.setParent(x);
		c.setParent(y);

		addNode(x).addNode(y).addNode(a).addNode(b).addNode(c);
				
		a.addEdge(b);
		b.addEdge(c);
		c.addEdge(a);
	}
	
	private InteractionGraphNode addNodeIfNotExists(String label) {
		InteractionGraphNode newNode;
		
		if (! nodes.containsKey(label)) {
			newNode = new InteractionGraphNode(label, this);
			addNode(newNode);
		} else {
			newNode = nodes.get(label); 
		}
		
		return newNode;
	}
	
	private InteractionGraphNode addNodeIfNotExists(String label, String[] hierarchy) {
		InteractionGraphNode newNode;

		if (!nodes.containsKey(label)) {
			InteractionGraphNode currentNode = null;

			// Walk down the hierarchy path, each time adding the node and making 
			// it a parent of the last node
			for (String parent : hierarchy) {
				InteractionGraphNode child = addNodeIfNotExists(parent);
				if (currentNode != null) {
					child.setParent(currentNode);
				}
				currentNode = child;
			}
			
			newNode = currentNode;
		} else {
			newNode = nodes.get(label);
		}

		return newNode;
	}

	private InteractionGraph addNode(InteractionGraphNode node) {
		nodes.put(node.getLabel(), node);
		return this;
	}

	public String getLabel() {
		return "Interaction Graph";
	}

	public DirectedGraph<?, ?> getGraph() {
		return this;
	}

	public AttributeMap getAttributeMap() {
		return attributes;
	}

	public Set<InteractionGraphNode> getNodes() {
		return new HashSet<InteractionGraphNode>(nodes.values());
	}

	public Set<InteractionGraphEdge> getEdges() {
		return new HashSet<InteractionGraphEdge>(edges.values());
	}
	
	// All edges from node1 to node2 and vice versa
	public Set<InteractionGraphEdge> getEdges(DirectedGraphNode node1, DirectedGraphNode node2) {
		HashSet<InteractionGraphEdge> l = new HashSet<>();
		for(InteractionGraphEdge e: edges.values()) {
			if ((e.getTarget() == node1 && e.getSource() == node2) ||
				(e.getTarget() == node2 && e.getSource() == node1)) {
				l.add(e);
			}
		}
		
		return l;
	}
	
	public Set<InteractionGraphEdge> getEdgesIncludingChildren(DirectedGraphNode node1, DirectedGraphNode node2) {
		HashSet<InteractionGraphEdge> l = new HashSet<>();
		
		Set<DirectedGraphNode> node1Set = getChildrenOfNode(node1);
		Set<DirectedGraphNode> node2Set = getChildrenOfNode(node2);
		
		for(InteractionGraphEdge e: edges.values()) {
			InteractionGraphNode target = e.getTarget();
			InteractionGraphNode source = e.getSource();
			
			if ((node1Set.contains(target) && node2Set.contains(source)) ||
					(node2Set.contains(target) && node1Set.contains(source))) {
				l.add(e);
			}
		}
		
		return l;
	}
	
	/**
	 * Recursively find all the children of a node
	 * 
	 * @param node		The node whose children we want
	 * @return			A set of children of the node, or only the node itself if it is not a parent 
	 */
	@SuppressWarnings("unchecked")
	private Set<DirectedGraphNode> getChildrenOfNode(DirectedGraphNode node) {
		Set<DirectedGraphNode> nodes = new HashSet<>();
		
		if (node instanceof InteractionGraphNode) {
			InteractionGraphNode ign = (InteractionGraphNode) node;
			Set<DirectedGraphNode> children = (Set<DirectedGraphNode>) ign.getChildren();
			if (children == null || children.size() == 0) {
				nodes.add(node);
				return nodes;
			} else {
				for (DirectedGraphNode child : children) {
					nodes.addAll(getChildrenOfNode(child));
				}
				return nodes;
			}
		} else { // This shouldn't happen but is just there for safety
			nodes.add(node);
			return nodes;
		}
	}
	
	public InteractionGraphEdge getEdge(String key) {
		return edges.get(key);
	}

	public Collection<InteractionGraphEdge> getInEdges(DirectedGraphNode node) {
		LinkedList<InteractionGraphEdge> l = new LinkedList<>();
		for(InteractionGraphEdge e: edges.values()) {
			if (e.getTarget() == node) {
				l.add(e);
			}
		}
		return l;
	}

	public Collection<InteractionGraphEdge> getOutEdges(DirectedGraphNode node) {
		LinkedList<InteractionGraphEdge> l = new LinkedList<>();
		for(InteractionGraphEdge e: edges.values()) {
			if (e.getSource() == node) {
				l.add(e);
			}
		}
		return l;
	}

	@SuppressWarnings("rawtypes")
	public void removeEdge(DirectedGraphEdge edge) {
		edges.remove(edge);
	}

	public void removeNode(DirectedGraphNode node) {
		nodes.remove(node.getLabel());
	}

	public int compareTo(DirectedGraph<InteractionGraphNode, InteractionGraphEdge> o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(InteractionGraphNode node: nodes.values()) {		
			sb.append(node.toString());
		}
		
		return sb.toString();
	}

	/*
	 * This should only be called by node objects, otherwise the edge will be orphaned.
	 * Or well, edges don't really have parents, but they do have heads and tails, and
	 * their heads and tails wouldn't be attached if the node didn't add them. So you
	 * could say they were torso'd or something.
	 */
	public void addEdge(InteractionGraphEdge edge) {
		edges.put(edge.getLabel(), edge);
	}
}

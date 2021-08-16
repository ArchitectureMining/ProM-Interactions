package org.architecturemining.interactionCentric.visualizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.architecturemining.interactionCentric.models.InteractionModel;
import org.architecturemining.interactionCentric.models.SingleLikelihood;
import org.architecturemining.interactionCentric.models.LinkedListEdgesSet.EdgeMap;
import org.architecturemining.interactionCentric.visualizer.graph.GraphEdge;
import org.architecturemining.interactionCentric.visualizer.graph.GraphNode;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraphNode;


/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Class for creating the InteractionGraph which is based on nodes and edges. 
*/

public class InteractionGraph extends DefaultDirectedGraph<GraphNode, GraphEdge>{
	private static final long serialVersionUID = -749606944194953790L;
	private Map<String, GraphNode> nodes = new HashMap<>();
	private List<GraphEdge> edges = new ArrayList<GraphEdge>();
	
	// Create the graph by adding all nodes first
	// After creating the nodes, all connections from the probability matrix are created.
	// The built in visualizer shows this in a panel.
	public InteractionGraph(InteractionModel iModel) {
				
		super(GraphEdge.class);
		List<String> nodeNames = iModel.nodeList;
		for(Map.Entry<String, Integer> entry : iModel.entities.entrySet()) {
			nodes.put(entry.getKey(), new GraphNode(entry.getKey()));
		}
		int rowCounter = 0;
		for(double[] row: iModel.probabilityMatrix) {
			int colCounter = 0;
			for(double y: row) {
				if(y > 0) {
					GraphNode source = nodes.get(nodeNames.get(rowCounter));
					GraphNode target = nodes.get(nodeNames.get(colCounter));
					GraphEdge e = new GraphEdge(source, target, y);
					edges.add(e);
				}
				colCounter++;
			}
			rowCounter++;
		}
		
		this.fillGraph();
		
	}
	
	
	public InteractionGraph(SingleLikelihood sL) {
		super(GraphEdge.class);
		
		EdgeMap edgemap = sL.getEdgeMap();
		for(Entry<String, Set<String>> entry : edgemap.edges.entrySet()) {
			nodes.put(entry.getKey(), new GraphNode(entry.getKey()));
		}
		
		for(Entry<String, Set<String>> entry : edgemap.edges.entrySet()) {
			for(String s: entry.getValue()) {
				GraphNode source = nodes.get(entry.getKey());
				GraphNode target = nodes.get(s);
				Double probability = (double) 0;
				if(sL.getEdgeProbabilities().containsKey(entry.getKey()+ "_" + s)) {
					probability = sL.getEdgeProbabilities().get(entry.getKey() + "_" + s);
				}
				
				GraphEdge e = new GraphEdge(source, target, probability);
				edges.add(e);
			}
		}	
		this.fillGraph();
		
	}
	
	public void fillGraph() {
		for(GraphNode gn: nodes.values()) {	
			addVertex(gn);		
		}
		for(GraphEdge ge: edges) {
			addEdge(ge.getSource(), ge.getTarget(), ge);
		}
	}

	public Set<GraphNode> getNodes() {
		return new HashSet<GraphNode>(nodes.values());
	}

	public Set<GraphEdge> getEdges() {
		return new HashSet<GraphEdge>(edges);
	}

	public Collection<GraphEdge> getInEdges(DirectedGraphNode node) {
		return null;
	}

	public Collection<GraphEdge> getOutEdges(DirectedGraphNode node) {
		return null;
	}
	
}

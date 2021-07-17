package org.architecturemining.interactionCentric.models.LinkedListEdgesSet;

import java.util.Map;
import java.util.Set;

public class EdgeMap {
	
	public Map<String, Set<String>> edges;
	public Map<String, Set<String>> prevNodes;
	
	
		
	public EdgeMap() {
		super();
	}

	public EdgeMap(Map<String, Set<String>> edges, Map<String, Set<String>> prevNodes) {
		super();
		this.edges = edges;
		this.prevNodes = prevNodes;
	}
	
	public Map<String, Set<String>> getEdges() {
		return edges;
	}
	public void setEdges(Map<String, Set<String>> edges) {
		this.edges = edges;
	}
	public Map<String, Set<String>> getPrevNodes() {
		return prevNodes;
	}
	public void setPrevNodes(Map<String, Set<String>> prevNodes) {
		this.prevNodes = prevNodes;
	}
	
	

}

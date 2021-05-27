package org.architecturemining.interactionCentric.models.LinkedListEdgesSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LinkedListNode {
	public String current;
	public Map<String, List<LinkedListSetOfEdges>> outgoingEdgesSets;
	
	public LinkedListNode() {
		super();		
	}
	
	public LinkedListNode(String current) {
		super();
		this.current = current;
		this.outgoingEdgesSets = new HashMap<String, List<LinkedListSetOfEdges>>();
	}
	public String getCurrent() {
		return current;
	}
	public void setCurrent(String current) {
		this.current = current;
	}

	public Map<String, List<LinkedListSetOfEdges>> getOutgoingEdgesSets() {
		return outgoingEdgesSets;
	}
	public void setOutgoingEdgesSets(Map<String, List<LinkedListSetOfEdges>> outgoingEdgesSets) {
		this.outgoingEdgesSets = outgoingEdgesSets;
	}

	public void addOutgoingNodeSet(String prevNode, Set<String> edges) {
		if(!outgoingEdgesSets.containsKey(prevNode))
			outgoingEdgesSets.put(prevNode, new ArrayList<LinkedListSetOfEdges>());
		
		boolean found = false;
		List<LinkedListSetOfEdges> currentEntry = outgoingEdgesSets.get(prevNode);
		for(LinkedListSetOfEdges ent: currentEntry) {		
			if(ent.targetNodes.equals(edges)) {
				ent.occurenceCounter++;
				found = true;
				break;
			}		
		}	
		if(!found) {
			currentEntry.add(new LinkedListSetOfEdges(edges, 1));
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(List<LinkedListSetOfEdges> set: outgoingEdgesSets.values()) {
			for(LinkedListSetOfEdges ent: set){
				sb.append("[ " + ent.toString() + " ] & ");
			}
		}
		String s = sb.toString();
		if(s.length() > 2)
			return s.substring(0, sb.toString().length() - 2);
		else
			return s;
	}
	
}

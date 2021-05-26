package org.architecturemining.interactionCentric.models.LinkedListEdgesSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LinkedListNode {
	public String current;
	public Map<String, Map<String, LinkedListSetOfEdges>> outgoingEdgesSets;
	
	public LinkedListNode() {
		super();		
	}
	
	public LinkedListNode(String current) {
		super();
		this.current = current;
		this.outgoingEdgesSets = new HashMap<String, Map<String, LinkedListSetOfEdges>>();
	}
	public String getCurrent() {
		return current;
	}
	public void setCurrent(String current) {
		this.current = current;
	}

	public Map<String, Map<String, LinkedListSetOfEdges>> getOutgoingEdgesSets() {
		return outgoingEdgesSets;
	}
	public void setOutgoingEdgesSets(Map<String, Map<String, LinkedListSetOfEdges>> outgoingEdgesSets) {
		this.outgoingEdgesSets = outgoingEdgesSets;
	}
	public void addOutgoingNode(String prevNode, String trackingID, String nodeToAdd) {
		// check if both maps contain the desired keys.
		// Otherwise add the key with an empty list and rerun the method.
		if(outgoingEdgesSets.containsKey(prevNode)) {
			if(outgoingEdgesSets.get(prevNode).containsKey(trackingID)) {
				outgoingEdgesSets.get(prevNode).get(trackingID).targetNodes.add(nodeToAdd);
				
			}else {
				outgoingEdgesSets.get(prevNode).put(trackingID, new LinkedListSetOfEdges(new HashSet<String>(), 0));
				addOutgoingNode(prevNode, trackingID, nodeToAdd);
			}
			
		}else {
			outgoingEdgesSets.put(prevNode, new HashMap<String, LinkedListSetOfEdges>());
			addOutgoingNode(prevNode, trackingID, nodeToAdd);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Map<String, LinkedListSetOfEdges> set: outgoingEdgesSets.values()) {
			for(Map.Entry<String, LinkedListSetOfEdges> ent: set.entrySet()){
				sb.append(ent.getKey() + " = [ " + ent.getValue().toString() + " ]");
			}
		}
		return sb.toString();
	}
	
}

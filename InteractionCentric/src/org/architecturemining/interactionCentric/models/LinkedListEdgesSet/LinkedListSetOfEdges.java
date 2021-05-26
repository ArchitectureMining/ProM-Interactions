package org.architecturemining.interactionCentric.models.LinkedListEdgesSet;

import java.util.Set;

public class LinkedListSetOfEdges {

	public Set<String> targetNodes;
	public int occurenceCounter = 0;
	
	public LinkedListSetOfEdges() {
		super();
	}
	
	public LinkedListSetOfEdges(Set<String> targetNodes, int occurenceCounter) {
		super();
		this.targetNodes = targetNodes;
		this.occurenceCounter = occurenceCounter;
	}

	public Set<String> getTargetNodes() {
		return targetNodes;
	}

	public void setTargetNodes(Set<String> targetNodes) {
		this.targetNodes = targetNodes;
	}

	public int getOccurenceCounter() {
		return occurenceCounter;
	}

	public void setOccurenceCounter(int occurenceCounter) {
		this.occurenceCounter = occurenceCounter;
	}
	
	@Override
	public String toString() {
		String retString = "";
		for(String s: targetNodes) {
			retString += s + ", ";
		}		
		return retString.substring(0, retString.length() - 2);
		
	}
	
}

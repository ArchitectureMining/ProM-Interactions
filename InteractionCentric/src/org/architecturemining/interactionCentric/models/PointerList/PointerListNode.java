package org.architecturemining.interactionCentric.models.PointerList;

import java.util.ArrayList;
import java.util.List;

public class PointerListNode {
	String current;
	List<PointerListNode> nextNodes = new ArrayList<PointerListNode>();
	
	public PointerListNode(String current) {
		super();
		this.current = current;
	
	}
	public String getCurrent() {
		return current;
	}
	public void setCurrent(String current) {
		this.current = current;
	}
	public List<PointerListNode> getNextNodes() {
		return nextNodes;
	}
	public void setNextNodes(List<PointerListNode> nextNodes) {
		this.nextNodes = nextNodes;
	}
	
	
	
}

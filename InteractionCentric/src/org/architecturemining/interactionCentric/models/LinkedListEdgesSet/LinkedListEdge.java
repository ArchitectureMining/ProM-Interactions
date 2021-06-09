package org.architecturemining.interactionCentric.models.LinkedListEdgesSet;

public class LinkedListEdge {

	public LinkedListNode source;
	public LinkedListNode target;
	
	public LinkedListEdge(LinkedListNode source, LinkedListNode target) {
		super();
		this.source = source;
		this.target = target;
	}
	public LinkedListNode getSource() {
		return source;
	}
	public void setSource(LinkedListNode source) {
		this.source = source;
	}
	public LinkedListNode getTarget() {
		return target;
	}
	public void setTarget(LinkedListNode target) {
		this.target = target;
	}
	
	
	
}

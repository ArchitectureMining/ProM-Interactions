package org.architecturemining.interactionCentric.models.PointerList;

public class PointerListEdge {

	public PointerListNode source;
	public PointerListNode target;
	
	public PointerListEdge(PointerListNode source, PointerListNode target) {
		super();
		this.source = source;
		this.target = target;
	}
	public PointerListNode getSource() {
		return source;
	}
	public void setSource(PointerListNode source) {
		this.source = source;
	}
	public PointerListNode getTarget() {
		return target;
	}
	public void setTarget(PointerListNode target) {
		this.target = target;
	}
	
	
	
}

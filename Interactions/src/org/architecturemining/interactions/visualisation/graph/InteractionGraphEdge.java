package org.architecturemining.interactions.visualisation.graph;

import java.util.ArrayList;

import org.deckfour.xes.model.XEvent;
import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.directed.DirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;

public class InteractionGraphEdge implements DirectedGraphEdge<InteractionGraphNode, InteractionGraphNode> {

	private ArrayList<XEvent> events;
	private InteractionGraph model;
	private InteractionGraphNode head;
	private InteractionGraphNode tail;
	
	private AttributeMap attributes;

	/**
	 * @param tail		Tail or source of the edge
	 * @param head		Head or target of the edge
	 * @param model		InteractionGraph to which the edge belongs
	 * @param events	XEvents associated with the edge
	 */
	public InteractionGraphEdge(InteractionGraphNode tail, InteractionGraphNode head,
			InteractionGraph model, ArrayList<XEvent> events) {
		this.events = events;
		this.model = model;
		this.head = head;
		this.tail = tail;
		
		attributes = new AttributeMap();
		
		getAttributeMap().put(AttributeMap.EDGEEND, AttributeMap.ArrowType.ARROWTYPE_CLASSIC);
	}
 
	public String getLabel() {
		return toString();
	}

	public DirectedGraph<?, ?> getGraph() {
		return model;
	}

	public AttributeMap getAttributeMap() {
		return attributes;
	}

	public InteractionGraphNode getSource() {
		return tail;
	}

	public InteractionGraphNode getTarget() {
		return head;
	}

	public String toString() {
		return buildEdgeLabel(tail.getLabel(), head.getLabel());
	}
	
	/**
	 * This method is static because other objects need to know what label to 
	 * expect when looking for an edge. For example, InteractionGraph uses
	 * the labels of edges as keys in a HashMap. 
	 * 
	 * @param tailLabel		Label of the tail node
	 * @param headLabel		Label of the head node
	 * @return				A formatted string that represents the edge
	 */
	public static String buildEdgeLabel(String tailLabel, String headLabel) {
		return tailLabel + " -> " + headLabel;
	}

	/**
	 * @return the events
	 */
	public ArrayList<XEvent> getEvents() {
		return events;
	}

	/**
	 * @param events 	The event to add
	 */
	public void addEvent(XEvent event) {
		events.add(event);
	}
	
	
}

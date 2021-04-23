package org.architecturemining.interactionCentric.visualizer.graph;

import java.text.DecimalFormat;

import javax.swing.SwingConstants;

import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.directed.DirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;

/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Implementaton of the edge used in the InteractionGraph. 
 * An edge consists out of 2 nodes and a weight(strength)
*/
public class GraphEdge implements DirectedGraphEdge<GraphNode, GraphNode>{

	private GraphNode source, target;
	private AttributeMap attributes;
	
	public GraphEdge(GraphNode source, GraphNode target, double strength) {
		this.source = source;
		this.target = target;
		
		attributes = new AttributeMap();
		getAttributeMap().put(AttributeMap.EDGEEND, AttributeMap.ArrowType.ARROWTYPE_CLASSIC);
		getAttributeMap().put(AttributeMap.EDGEMIDDLEFILLED, true);
		
		DecimalFormat df = new DecimalFormat("#.00");
		getAttributeMap().put(AttributeMap.LABEL, df.format(strength));
		getAttributeMap().put(AttributeMap.SHOWLABEL, true);
		getAttributeMap().put(AttributeMap.LABELALONGEDGE, true);
		getAttributeMap().put(AttributeMap.PREF_ORIENTATION, SwingConstants.WEST);
	}
	
	public String getLabel() {
		return source.getLabel() + " -> " + target.getLabel();
	}
	
	@Override
	public String toString() {
		return getLabel();
	}

	public DirectedGraph<?, ?> getGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	public AttributeMap getAttributeMap() {
		return attributes;
	}

	public GraphNode getSource() {
		return source;
	}

	public GraphNode getTarget() {
		return target;
	}

}

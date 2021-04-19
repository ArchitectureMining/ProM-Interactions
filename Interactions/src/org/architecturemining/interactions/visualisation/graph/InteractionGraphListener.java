package org.architecturemining.interactions.visualisation.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.architecturemining.interactions.visualisation.components.InteractionPanel;
import org.deckfour.xes.model.XEvent;
import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.processmining.models.jgraph.elements.ProMGraphCell;

public class InteractionGraphListener implements GraphSelectionListener {
	private InteractionPanel iPanel;
	private InteractionGraph iGraph;
	private JGraph jGraph;

	public InteractionGraphListener(JGraph jGraph, InteractionPanel iPanel, InteractionGraph iGraph) {
		this.jGraph = jGraph;
		this.iPanel = iPanel;
		this.iGraph = iGraph;
	}

	public void valueChanged(GraphSelectionEvent e) {
		Object[] cells = jGraph.getSelectionCells();
		
		HashMap<Set<String>, ArrayList<XEvent>> events = new HashMap<>();

		if (cells.length > 0 && cells[0] instanceof ProMGraphCell) {
			// Fancy way to check if all elements are ProMGraphCells
			if (cells.length == 0) {
				iPanel.draw("No selection");
			} else if (Arrays.stream(cells).allMatch(cell -> cell instanceof ProMGraphCell)) {
				/**
				 * The selections occur in the ProMGraph. This means the selections occur in 
				 * ProMGraphEdges. Each ProMGraphEdge holds a reference to the DirectedGraphEdge
				 * that was used to create it. As you can see, the process of getting the original 
				 * edge object is quite involved.
				 */
				HashSet<InteractionGraphNode> nodeSet = new HashSet<>();
				for (Object cell : cells) {
					nodeSet.add((InteractionGraphNode) ((ProMGraphCell) cell).getNode());
				}
				events = getEventsFromNodes(nodeSet);
			} else {
				if (cells.length > 0) {
					iPanel.draw("Please select only nodes");
				} else {
					iPanel.draw("Invalid selection");
				}
			}
		}
		
		if (events == null || events.size() == 0) {
			iPanel.setInvalidSelection();
		} else {
			iPanel.setSelectedInteractions(events);
		}
	}
	
	private HashMap<Set<String>, ArrayList<XEvent>> getEventsFromNodes(HashSet<InteractionGraphNode> nodeSet) {
		/* Pair creation algorithm basically works as follows:
		 * Take a set like A B C D
		 * Start with any element (A in our case)
		 * Take the combinations with every object (including itself)
		 * 		A-A, A-B, A-C, A-D
		 * Remove A from the set
		 * Repeat
		 * 		B-B, B-C, B-D
		 * 		C-C, C-D
		 * 		D-D 
		 */
		Iterator<InteractionGraphNode> iterator = nodeSet.iterator();
		HashMap<Set<String>, ArrayList<XEvent>> traces = new HashMap<>();
		
		while (iterator.hasNext()) {
			InteractionGraphNode node1 = iterator.next();
			
			for (InteractionGraphNode node2 : nodeSet) {
				Set<InteractionGraphEdge> edges = iGraph.getEdgesIncludingChildren(node1, node2);
				if (edges.size() > 0) {
					Set<String> interactionLabel = new HashSet<>();
					interactionLabel.add(node1.getLabel());
					interactionLabel.add(node2.getLabel());
					
					traces.put(interactionLabel, eventsFromEdges(edges));
				}
			}
			
			iterator.remove();
		}

		return traces;
	}
	
	private ArrayList<XEvent> eventsFromEdges(Set<InteractionGraphEdge> edges) {
		ArrayList<XEvent> newInteractions = new ArrayList<XEvent>();
		
		for (InteractionGraphEdge edge : edges) {
			newInteractions.addAll(edge.getEvents());
		}
		
		return newInteractions;
	}
}

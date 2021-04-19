package org.architecturemining.interactions.visualisation.tree;

import java.util.ArrayList;
import java.util.HashMap;

import org.architecturemining.interactions.model.Interaction;
import org.deckfour.xes.model.XEvent;

/**
 * @author Tijmen
 *
 *         This class is used as a container object within JTree nodes. It may
 *         contain a collection of interactions. It always has a label.
 * 
 *         This kind of object is necessary because DefaultMutableTreeNode
 *         accepts a 'User Object' as its contained object, but we like to be
 *         sure that regardless of whether there is a set of interactions
 *         associated with a node, the node label is always what we expect it to
 *         be. This means implementing a custom toString.
 */
public class InteractionTreeNode {
	private String label;
	private String fullName;
	private HashMap<String, ArrayList<Interaction>> interactions;
	private HashMap<String, ArrayList<XEvent>> events;
	private boolean hasInteractions;

	public InteractionTreeNode(Interaction ia, String label, String fullName) {
		// We don't know if the label should correspond to the caller or the callee,
		// so we just ask for a label
		this.label = label;
		this.interactions = new HashMap<String, ArrayList<Interaction>>();
		this.events = new HashMap<String, ArrayList<XEvent>>();
		this.fullName = fullName;
		hasInteractions = true;
		addInteraction(ia);
	}

	/**
	 * This constructor is for hierarchy nodes i.e. nodes that don't hold any
	 * interactions, only children that do
	 * 
	 * @param label
	 */
	public InteractionTreeNode(String label, String fullName) {
		this.label = label;
		this.interactions = new HashMap<String, ArrayList<Interaction>>();
		this.events = new HashMap<String, ArrayList<XEvent>>();
		this.fullName = fullName;
		hasInteractions = false;
	}

	/**
	 * @return the interactions
	 */
	public HashMap<String, ArrayList<Interaction>> getInteractions() {
		return interactions;
	}

	/**
	 * @return the events
	 */
	public HashMap<String, ArrayList<XEvent>> getEvents() {
		return events;
	}

	/**
	 * This method should be called when associating a new interaction with this
	 * node. It makes sure that all interactions that relate to a node are kept
	 * together in one place.
	 * 
	 * @param ia
	 */
	public void addInteraction(Interaction ia) {
		hasInteractions = true;
		
		// Only add an interaction if this node is the caller
		if (ia.getCaller().equals(fullName)) {
			// Boilerplate to deal with maps of lists
			if (events.containsKey(ia.getCallee())) {
				events.get(ia.getCallee()).add(ia.getEvent());
			} else {
				ArrayList<XEvent> l = new ArrayList<>();
				l.add(ia.getEvent());
				events.put(ia.getCallee(), l);
			}
			
			// Identical to above
			if (interactions.containsKey(ia.getCallee())) {
				interactions.get(ia.getCallee()).add(ia);
			} else {
				ArrayList<Interaction> l = new ArrayList<>();
				l.add(ia);
				interactions.put(ia.getCallee(), l);
			}
		}
	}

	public String toString() {
		return label;
	}
	
	/**
	 * Because calling toString if you want the label might be confusing
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
	
	public boolean hasInteractions() {
		return hasInteractions;
	}

	public String getFullName() {
		return fullName;
	}
}

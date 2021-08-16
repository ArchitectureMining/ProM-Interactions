package org.architecturemining.interactions.model;

import java.util.HashMap;

/**
 * @author Tijmen
 * 
 * This class provides a mapping between interaction objects
 * used inside an interaction log and the hierarchical structure
 * of those objects.
 *
 */
public class InteractionLogHierarchyMapping {
	
	private HashMap<String, String[]> mapping = new HashMap<>();

	public InteractionLogHierarchyMapping() {
		mapping = new HashMap<>();
	}
	
	/**
	 * @param node		the interaction object to add
	 * @param fqn		the FQN of the associated node
	 * @return whether the element was already in the mapping
	 */
	public boolean addNode(String node, String fqn) {
		if (! mapping.containsKey(node)) {
			mapping.put(node, labelToHierarchy(fqn));
			return false;
		}
		return true;
	}

	/**
	 * @return the nodeToHierarchy
	 */
	public HashMap<String, String[]> getMapping() {
		return mapping;
	}
	
	/**
	 * All elements of the FQN except 
	 * 
	 * We use reverse order because that's easier later on when we create a 
	 * graph
	 * 
	 * @param label		label of the node
	 * @return			A reverse ordered array of hierarchical sub elements
	 */
	private String[] labelToHierarchy(String label) {
		String[] hierarchy = stringToHierarchy(label);
		
		int numberOfElements = hierarchy.length;
		String[] output = new String[numberOfElements];
		
		for (int i = numberOfElements - 1; i >= 0; i--) {
			output[i] = hierarchy[i];
		}
		
		return output;
	}
	
	// This method can be replaced if for any reason the separator in the hierarchy is different
	private static String[] stringToHierarchy(String input) {
		return input.split("\\."); 
	}
	
}

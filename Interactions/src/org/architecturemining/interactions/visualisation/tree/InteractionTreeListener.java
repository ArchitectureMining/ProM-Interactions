package org.architecturemining.interactions.visualisation.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.architecturemining.interactions.visualisation.components.InteractionPanel;
import org.deckfour.xes.model.XEvent;

public class InteractionTreeListener implements TreeSelectionListener {
	private InteractionPanel iPanel;
	private JTree tree;

	public InteractionTreeListener(JTree tree, InteractionPanel iPanel) {
		this.tree = tree;
		this.iPanel = iPanel;
	}

	/*
	 * When the selection changes, this method is triggered. The selection event
	 * is ignored, because similarly to the graph listener, it doesn't provide
	 * us with the information we need. We ask the tree panel itself for its
	 * selected elements.
	 */
	public void valueChanged(TreeSelectionEvent arg0) {
		TreePath[] selection = tree.getSelectionModel().getSelectionPaths();

		// Get ITNs of all the selected nodes
		HashSet<InteractionTreeNode> interactionNodes = new HashSet<>();
		for (TreePath tp : selection) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
			interactionNodes.addAll(getInteractionTreeNodes(treeNode));
		}

		// Get all the keys we need for finding nodes from interactions
		HashSet<String> interactionNodeNames = new HashSet<>();
		for (InteractionTreeNode itn : interactionNodes) {
			interactionNodeNames.add(itn.getFullName());
		}

		if (interactionNodes.size() == 0) {
			iPanel.draw("No selection");
		} else {
			/* Each Interaction contains a map of all related nodes (key) and
			 * the events associated with the edge between the current node and
			 * the other node.  
			 */
			HashMap<Set<String>, ArrayList<XEvent>> eventMap = new HashMap<>();

			for (InteractionTreeNode itn : interactionNodes) {
				String caller = itn.getFullName();
				HashMap<String, ArrayList<XEvent>> currentEventMap = itn.getEvents();
				for (Entry<String, ArrayList<XEvent>> c : currentEventMap.entrySet()) {
					if (interactionNodeNames.contains(c.getKey())) {
						HashSet<String> keySet = new HashSet<>();
						keySet.add(caller);
						keySet.add(c.getKey());
						eventMap.put(keySet, c.getValue());
					}
				}
			}

			if (eventMap.size() == 0) {
				iPanel.draw("The selected nodes don't have any events in common");
				iPanel.setInvalidSelection();
			} else {
				iPanel.setSelectedInteractions(eventMap);
			}
		}
	}

	private Collection<InteractionTreeNode> getInteractionTreeNodes(DefaultMutableTreeNode treeNode) {
		HashSet<InteractionTreeNode> interaction = new HashSet<>();

		// If the user selected a leaf, we add the leaf
		if (treeNode.isLeaf()) {
			InteractionTreeNode itn = (InteractionTreeNode) treeNode.getUserObject();
			interaction.add(itn);
		} else { // If the user selected a non-leaf, add all the corresponding leafs 
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) treeNode.getChildAt(i);
				// We always want a leaf
				interaction.addAll(getAllLeafs(child));
			}
		}

		return interaction;
	}

	private HashSet<InteractionTreeNode> getAllLeafs(DefaultMutableTreeNode node) {
		HashSet<InteractionTreeNode> set = new HashSet<>();
		if (node.isLeaf()) {
			set.add((InteractionTreeNode) node.getUserObject());
		} else {
			for (int i = 0; i < node.getChildCount(); i++) {
				set.addAll(getAllLeafs((DefaultMutableTreeNode) node.getChildAt(i)));
			}
		}

		return set;
	}

}

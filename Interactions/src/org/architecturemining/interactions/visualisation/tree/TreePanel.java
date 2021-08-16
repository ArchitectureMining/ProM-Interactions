package org.architecturemining.interactions.visualisation.tree;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.lang3.StringUtils;
import org.architecturemining.interactions.model.Interaction;
import org.architecturemining.interactions.model.InteractionLog;
import org.architecturemining.interactions.visualisation.components.InteractionPanel;
import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;

public class TreePanel extends JPanel {

	private static final long serialVersionUID = -8995570111844269901L;
	
	private InteractionPanel interactionPanel;
		
	public TreePanel(PluginContext context, InteractionLog log) {
		// Initialize panels
		// 		Main panel
		this.setLayout(new BorderLayout());
		
		// 		Visualisation panel
		JTree tree = buildTree(log);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		
		// 		Interaction Panel
		// 			Displays whether the selected nodes can have 
		interactionPanel = new InteractionPanel();
				
		// Initialize event handlers
		InteractionTreeListener itl = new InteractionTreeListener(tree, interactionPanel);
		tree.addTreeSelectionListener(itl);
		
		// Compose panel
		this.add(tree, BorderLayout.CENTER);
		this.add(interactionPanel, BorderLayout.PAGE_END);
	}
	
	public boolean canExportInteractions() {
		return interactionPanel.selectionIsValidInteraction();
	}
	
	public XLog getInteractions() {
		return interactionPanel.exportLogWithIndividualTraces();
	}
	
	private JTree buildTree(InteractionLog log) {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("All nodes");
		JTree tree = new JTree(top);
		
		HashMap<String, String[]> mapping = log.getMapping().getMapping();
		
		HashMap<String, DefaultMutableTreeNode> treeMapping = new HashMap<>();
		
		for (Interaction ia : log.getLog()) {
			// Add the caller and callee nodes
			String[] callerHierarchy = mapping.get(ia.getCaller());
			String[] calleeHierarchy = mapping.get(ia.getCallee());
			treeMapping = addHierarchy(callerHierarchy, treeMapping, top, ia);
			treeMapping = addHierarchy(calleeHierarchy, treeMapping, top, ia);
		}
		
		return tree;
	}
	
	private HashMap<String, DefaultMutableTreeNode> addHierarchy(
				String[] hierarchy, 
				HashMap<String, DefaultMutableTreeNode> treeNodeMap, 
				DefaultMutableTreeNode top,
				Interaction ia) {
		DefaultMutableTreeNode lastNode = top;
		
		for (int i = 0; i < hierarchy.length; i++) {
			// ".".join(hierarchy[:i]) for you Python people ;-)
			String concat = StringUtils.join(Arrays.copyOfRange(hierarchy, 0, i + 1), '.');
			DefaultMutableTreeNode newNode;
			InteractionTreeNode itn;
			
			if (treeNodeMap.containsKey(concat)) {
				newNode = treeNodeMap.get(concat);
				
				// If we are on a leaf node and this leaf node already exists,
				// add the interaction to the leaf node
				if (i == hierarchy.length - 1) {
					itn = (InteractionTreeNode) newNode.getUserObject();
					itn.addInteraction(ia);
				}
			} else {
				if (i == hierarchy.length - 1) {
					// Create a new leaf node
					itn = new InteractionTreeNode(ia, hierarchy[i], concat);
					newNode = new DefaultMutableTreeNode(itn);
				} else {
					// Create a new hierarchy node
					itn = new InteractionTreeNode(hierarchy[i], concat);
					newNode = new DefaultMutableTreeNode(itn);
				}
				
				lastNode.add(newNode);					
				treeNodeMap.put(concat, newNode);
			}
			lastNode = newNode;
		}
		return treeNodeMap;
	}
}

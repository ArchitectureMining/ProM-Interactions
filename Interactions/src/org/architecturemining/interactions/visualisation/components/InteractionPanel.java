package org.architecturemining.interactions.visualisation.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JTextArea;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.framework.util.ui.widgets.ProMScrollablePanel;

/**
 * @author Tijmen
 * 
 *         This panel is used to indicate whether there are any interactions in
 *         the selection. The selection must be either an edge or two nodes for
 *         there to be an interaction.
 * 
 */
public class InteractionPanel extends ProMScrollablePanel {
	private static final long serialVersionUID = 1L;
	
	private boolean canExport;
	private HashMap<Set<String>, ArrayList<XEvent>> selectedInteractions;

	public InteractionPanel() {
		this.canExport = false;

		draw("No selection");
	}
	
	/**
	 * Set the interactions and assumes that the selection is valid
	 * 
	 * @param selectedInteractions the selectedInteractions to set
	 */
	public void setSelectedInteractions(HashMap<Set<String>, ArrayList<XEvent>> selectedInteractions) {
		this.selectedInteractions = selectedInteractions;
		canExport = true;
		draw(formatEvents(selectedInteractions.values()));
	}
	
	public boolean selectionIsValidInteraction() {
		return canExport;
	}
	
	public void draw(String text) {
		this.removeAll();
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		JTextArea jl = new JTextArea(text);
		jl.setEditable(false);
		jl.setRows(1);
		jl.setLineWrap(true);

		this.add(jl);

		this.revalidate();
		this.repaint();
	}

	private String formatEvents(Collection<ArrayList<XEvent>> values) {
		int noInteractions = values.size();
		ArrayList<String> interactionList = new ArrayList<>(); 
		for (ArrayList<XEvent> interaction : values) {
			interactionList.add(abbreviateList(interaction, 3).toString());
		}
		if (noInteractions == 1) {
			return "1 interaction selected: " + abbreviateList(interactionList, 3);
		} else {
			return String.valueOf(noInteractions) + " interactions selected: " + abbreviateList(interactionList, 3);
		}
		
	}
	
	/**
	 * Abbreviates a the string representation of a list
	 * 
	 * @param list
	 *            Input list
	 * @param maxLength
	 *            The maximum number of list items to be used
	 * @return toString of abbreviated list or the original list if |list| <=
	 *         maxLength
	 */
	private <T> String abbreviateList(List<T> list, int maxLength) {
		if (list.size() <= maxLength || maxLength < 2) {
			return list.toString();
		} else {
			String listPart = list.subList(0, maxLength).toString();
			int remaining = list.size() - maxLength;
			return listPart + " (and " + String.valueOf(remaining) + " more)";
		}
	}

	/**
	 * This method is called by the Wizard when the 'Finish' button is pressed
	 */
	public XLog exportInteractions() {
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		
		XLog interactionXLog = factory.createLog();
		
		XTrace trace = factory.createTrace();
		
		XAttributeMap am = factory.createAttributeMap();
		am.put(XConceptExtension.KEY_NAME, factory.createAttributeLiteral(XConceptExtension.KEY_NAME, "All interactions", XConceptExtension.instance()));
		trace.setAttributes(am);
		
		for (Entry<Set<String>, ArrayList<XEvent>> interaction : selectedInteractions.entrySet()) {
			trace.addAll(interaction.getValue());
		}
		
		interactionXLog.add(trace);
		
		return interactionXLog;
	}

	/**
	 * @param factory
	 * @param interactionXLog
	 * @return An XLog where every Set is a separate Trace
	 * 
	 */
	public XLog exportLogWithIndividualTraces() {
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		
		XLog interactionXLog = factory.createLog();
		
		for (Entry<Set<String>, ArrayList<XEvent>> interaction : selectedInteractions.entrySet()) {
			XTrace trace = factory.createTrace();
			XAttributeMap am = factory.createAttributeMap();
			String traceLabel = interaction.getKey().toString(); // This could be nicer
			am.put(XConceptExtension.KEY_NAME, factory.createAttributeLiteral(XConceptExtension.KEY_NAME, traceLabel, XConceptExtension.instance()));
			trace.addAll(interaction.getValue());
			trace.setAttributes(am);
			interactionXLog.add(trace);
		}
		
		return interactionXLog;
	}

	public void setInvalidSelection() {
		canExport = false;
		draw("No selection");
	}
}

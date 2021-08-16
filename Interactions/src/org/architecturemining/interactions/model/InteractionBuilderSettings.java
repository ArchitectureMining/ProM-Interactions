package org.architecturemining.interactions.model;

import org.deckfour.xes.model.XLog;

public class InteractionBuilderSettings {
	
	private String[] callers;
	private String[] callees;
	private String[] calls;
	private String caller;
	private String callee;
	private String call;
	private String filterParam1;
	private String filterParam2;
	private XLog xLog;
	private InteractionLog iLog;
	private boolean rename;
	
	public InteractionBuilderSettings(String[] attributes) {
		// Default values
		this.callers = attributes;
		this.callees = attributes;
		this.calls = attributes;
	}

	public String getCaller() {
		return caller;
	}
	
	public String getCallerHierarchyKey() {
		return caller + "Hierarchy";
	}
	
	public String getCalleeHierarchyKey() {
		return callee + "Hierarchy";
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public String[] getPossibleCallers() {
		return callers;
	}

	public void setPossibleCallers(String[] attributes) {
		if (callees.length > 0) {
			this.callers = attributes;
			this.caller = attributes[0];
		}
	}

	public String[] getPossibleCallees() {
		return callees;
	}

	public void setPossibleCallees(String[] attributes) {
		if (attributes.length > 0) {
			this.callees = attributes;
			this.callee = attributes[0];
		}
	}

	public void setCallee(String callee) {
		this.callee = callee;
	}

	public String getCallee() {
		return callee;
	}

	public void setFilter(String selectedItem, String selectedItem2) {
		this.filterParam1 = selectedItem;
		this.filterParam2 = selectedItem2;
	}
	
	public String[] getFilter() {
		String[] out = {filterParam1, filterParam2};
		return out;
	}
	
	public String toString() {
		String summary = String.format(
				  "Caller: %s "
				+ "Callee: %s "
				+ "filterParam1: %s "
				+ "filterParam2: %s ",
				caller,
				callee,
				filterParam1,
				filterParam2);
		return summary;
		
	}

	/**
	 * @return the xLog
	 */
	public XLog getXLog() {
		return xLog;
	}

	/**
	 * @param xLog the xLog to set
	 */
	public void setXLog(XLog xLog) {
		this.xLog = xLog;
	}

	public void setInteractionLog(InteractionLog iLog) {
		this.iLog = iLog;
	}
	
	public InteractionLog getInteractionLog() {
		return iLog;
	}

	public String[] getPossibleCalls() {
		return calls;
	}
	
	public String getCall() {
		return call;
	}
	
	public boolean shouldRename() {
		return rename;
	}

	public void setCall(String selectedItem) {
		call = selectedItem;
	}

	public void setRename(boolean selected) {
		rename = selected;
	}
}

package org.architecturemining.interactionCentric.models;

import org.deckfour.xes.model.XLog;


/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Model containing all parameter settings needed for parsing an event log by the Discovery Plugin
*/
public class ParameterSettings {
	public String callerTag;
	public String calleeTag;
	public String[] possibleOptions;
	public XLog log;
	
	public ParameterSettings(String[] attributes, XLog log) {
		this.possibleOptions = attributes;
		this.log = log;
	}

	public String getCallerTag() {
		return callerTag;
	}

	public void setCallerTag(String callerTag) {
		this.callerTag = callerTag;
	}

	public String getCalleeTag() {
		return calleeTag;
	}

	public void setCalleeTag(String calleeTag) {
		this.calleeTag = calleeTag;
	}
	
	
}

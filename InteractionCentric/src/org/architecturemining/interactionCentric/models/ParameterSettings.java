package org.architecturemining.interactionCentric.models;

import org.deckfour.xes.model.XLog;
import org.processmining.log.csv.CSVFile;


/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Model containing all parameter settings needed for parsing an event log by the Discovery Plugin
*/
public class ParameterSettings {
	public String caseID;
	public String callerTag;
	public String calleeTag;
	public String eventTypeTag;
	public String[] possibleOptions;
	public CSVFile csvLog;
	public XLog log;
	
	public ParameterSettings(String[] attributes, CSVFile csvLog) {
		this.possibleOptions = attributes;
		this.csvLog = csvLog;
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

	public CSVFile getCsvLog() {
		return csvLog;
	}

	public void setCsvLog(CSVFile csvLog) {
		this.csvLog = csvLog;
	}

	public XLog getLog() {
		return log;
	}

	public void setLog(XLog log) {
		this.log = log;
	}
	
	public String getCaseID() {
		return caseID;
	}

	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}

	public String getEventTypeTag() {
		return eventTypeTag;
	}

	public void setEventTypeTag(String eventTypeTag) {
		this.eventTypeTag = eventTypeTag;
	}
	
	
	
}

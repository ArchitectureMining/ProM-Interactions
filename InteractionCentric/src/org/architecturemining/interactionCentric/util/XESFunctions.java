package org.architecturemining.interactionCentric.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;

public class XESFunctions {

	public String callerTag, calleeTag, eventTypeTag;
	public XESFunctions(String cllrTag, String clleeTag, String eventTypeTag) {
		this.calleeTag = clleeTag;
		this.callerTag = cllrTag;
		this.eventTypeTag = eventTypeTag;
	};
	
	public XESFunctions() {} // empty constructor needed for loading imod objects into PROM
	
	public Set<String> getSourceAttributeValues(XTrace trace) {
		Set<String> attributeKeySet = new HashSet<String>();
		for(XEvent e: trace) {
			XAttributeMap att = e.getAttributes();
			XAttributeLiteral source = (XAttributeLiteral) att.get(callerTag);
			attributeKeySet.add(source.toString());
		}
    	return attributeKeySet;
	}
	
	public Set<String> getSinkAttributeValues(XTrace trace) {
		Set<String> attributeKeySet = new HashSet<String>();
		for(XEvent e: trace) {
			XAttributeMap att = e.getAttributes();
			XAttributeLiteral sink = (XAttributeLiteral) att.get(calleeTag);
			attributeKeySet.add(sink.toString());
		}
    	return attributeKeySet;
	}
	
	public Set<String> getAllMessageTypes(XTrace trace){
		Set<String> attributeKeySet = new HashSet<String>();
		for(XEvent e: trace) {
			XAttributeMap att = e.getAttributes();
			XAttributeLiteral event = (XAttributeLiteral) att.get(eventTypeTag);
			attributeKeySet.add(event.toString() +  "_event");
		}
    	return attributeKeySet;
	}
	
	public Set<String> getStarterNodes(Set<String> sinkValues, Set<String> allValues){
		Set<String> starters = new HashSet<String>();
		starters.addAll(allValues);
		starters.removeAll(sinkValues);
		return starters;
	}
	
	public Set<String> getEndNodes(Set<String> sourceValues, Set<String> allValues){
		Set<String> starters = new HashSet<String>();
		starters.addAll(allValues);
		starters.removeAll(sourceValues);
		return starters;
	}
	
	public String getCaller(XEvent ev) {
		XAttributeMap att = ev.getAttributes();
		XAttributeLiteral source = (XAttributeLiteral) att.get(callerTag);
		return source.toString();
	}
	
	public String getCallee(XEvent ev) {
		XAttributeMap att = ev.getAttributes();
		XAttributeLiteral sink = (XAttributeLiteral) att.get(calleeTag);
		return sink.toString();
	}
	
	public String getEventType(XEvent ev) {
		XAttributeMap att = ev.getAttributes();
		XAttributeLiteral sink = (XAttributeLiteral) att.get(eventTypeTag);
		return sink.toString();
	}
	
	public List<String> uniqueEntitiesPerTrace(XTrace trace) {
		Set<String> list = new LinkedHashSet<String>();
		for (XEvent event : trace) {
			list.add(getCaller(event));
			list.add(getCallee(event));
			if(!eventTypeTag.equals("(empty)")) {
				list.add(getEventType(event) + "_event");
			}
		}
		List<String> mainList = new ArrayList<String>();
		mainList.addAll(list);
		return mainList;
	}
	
	
	// getters and setters needed for building the object by a json object.
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

	public String getEventTypeTag() {
		return eventTypeTag;
	}

	public void setEventTypeTag(String eventTypeTag) {
		this.eventTypeTag = eventTypeTag;
	}
	
	
}

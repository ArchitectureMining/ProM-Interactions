package org.architecturemining.interactionCentric.util;

import java.util.HashSet;
import java.util.Set;

import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;

public class XESFunctions {

	public String callerTag, calleeTag;
	public XESFunctions(String cllrTag, String clleeTag) {
		this.calleeTag = clleeTag;
		this.callerTag = cllrTag;
	};
	
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
	
}

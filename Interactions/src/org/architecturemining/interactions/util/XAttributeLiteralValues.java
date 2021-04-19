package org.architecturemining.interactions.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.model.XAttributable;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XVisitor;

public class XAttributeLiteralValues {
	
	private final XLog log;
	
	private final Map<String,Set<Object>> eventAttributeValues;
	private final Map<String,Set<Object>> allAttributeValues;
	
	private final Map<String, XAttribute> eventAttributeType;
	
	public XAttributeLiteralValues(XLog log) {
		this.log = log;
		
		eventAttributeValues = new HashMap<String, Set<Object>>();
		allAttributeValues = new HashMap<String, Set<Object>>();
		
		eventAttributeType = new HashMap<String, XAttribute>();
		
		log.accept(new XAttributeValuesVisitor());
	}
	
	public Set<String> getAllAttributeKeys() {
		return allAttributeValues.keySet();
	}
	
	public XLog getLog() {
		return log;
	}
	
	
	/**
	 * Is called only once by the constructor of XAttributeValues;
	 * @author jvdwerf
	 *
	 */
	private class XAttributeValuesVisitor extends XVisitor {
	
		public void init(XLog log) {
		}
		
		
		private void addValue(Map<String,Set<Object>> list, String key, Object value) {
			if (list.containsKey(key)) {
				list.get(key).add(value);
			} else {
				Set<Object> ns = new HashSet<Object>();
				ns.add(value);
				list.put(key, ns);
			}
		}
		
		private void addType(Map<String, XAttribute> list, XAttribute attr) {
			if (!list.containsKey(attr.getKey())) {
				list.put(attr.getKey(), attr);
			}
		}
		
		public void visitAttributePre(XAttribute attr, XAttributable parent) {
			// Modified by ttjdejong to only include XAttributeLiteral objects 
			// at the event level
			Object value;
			if (attr instanceof XAttributeLiteral && parent instanceof XEvent) {
				value = ((XAttributeLiteral) attr).getValue();
				addValue(eventAttributeValues, attr.getKey(), value);
				addType(eventAttributeType, attr);
				addValue(allAttributeValues, attr.getKey(), value);
			}	
		}
	}
}

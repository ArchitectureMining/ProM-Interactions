package org.architecturemining.interactions.model;

import java.util.ArrayList;

import org.architecturemining.interactions.util.InteractionStringUtils;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

public class InteractionLog {
	
	private ArrayList<Interaction> log;
	private InteractionLogHierarchyMapping mapping;
	private XFactory factory;
	
	public InteractionLog(InteractionBuilderSettings model, XLog xLog)  {
		factory = XFactoryRegistry.instance().currentDefault();		
		log = new ArrayList<>(xLog.size());
		mapping = new InteractionLogHierarchyMapping();
		
		String callerKey = model.getCaller();
		String calleeKey = model.getCallee();
		
		for (XTrace trace : xLog) {
			for (XEvent event : trace) {
				XAttributeMap attributes = event.getAttributes();
				XAttributeLiteral callerAttribute = (XAttributeLiteral) attributes.get(callerKey);
				XAttributeLiteral calleeAttribute = (XAttributeLiteral) attributes.get(calleeKey);
				
				String callerString = callerAttribute.getValue();
				String calleeString = calleeAttribute.getValue();
				
				Interaction interaction = new Interaction(callerString, calleeString, event);
				
				addNodesToMapping(callerString, calleeString);
				
				// If the user opted to rename events, we do that here
				if (model.shouldRename()) {
					event = renameEvent(event, model);
				}
				
				log.add(interaction);
			}
		}
	}
	
	private XEvent renameEvent(XEvent event, InteractionBuilderSettings model) {
		XAttributeMap am = event.getAttributes();
		String call = ((XAttributeLiteral) am.get(model.getCall())).toString();
		String caller = ((XAttributeLiteral) am.get(model.getCaller())).toString();
		String callee = ((XAttributeLiteral) am.get(model.getCallee())).toString();
		
		String newName = 
				InteractionStringUtils.abbreviateNameWithoutIdentifier(caller) +
				" => " +
				InteractionStringUtils.abbreviateCall(call) +
				" => " +
				InteractionStringUtils.abbreviateNameWithoutIdentifier(callee);
		
		// Overwrite concept:name or add it if it doesn't exist
		am.put(XConceptExtension.KEY_NAME, factory.createAttributeLiteral(XConceptExtension.KEY_NAME, newName, XConceptExtension.instance()));
		
		return event;
	}

	private void addNodesToMapping(String callerString, String calleeString) {
		mapping.addNode(callerString, callerString);
		mapping.addNode(calleeString, calleeString);
	}

	public InteractionLogHierarchyMapping getMapping() {
		return mapping;
	}
	
	public ArrayList<Interaction> getLog() {
		return log;
	}
	
	public int size() {
		return log.size();
	}

}

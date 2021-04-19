package org.architecturemining.tests.interactions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.architecturemining.interactions.model.InteractionBuilderSettings;
import org.architecturemining.interactions.model.InteractionLog;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryBufferedImpl;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.junit.jupiter.api.Test;

public class InteractionLogTester {
	
	@Test
	public void testLogConversion() throws Throwable {
		XLog log = createTestLog();
		
		assertEquals(log.size(), 1);
		
		String[] attributes = {"Caller", "Callee", "Unrelated", "Timestamp", "Call"};
		InteractionBuilderSettings iSettings = new InteractionBuilderSettings(attributes);
		iSettings.setCaller("Caller");
		iSettings.setCallee("Callee");
		InteractionLog iLog = new InteractionLog(iSettings, log);
		
		assertEquals(iLog.size(), log.size());
	}

	private XLog createTestLog() {
		XFactory factory = new XFactoryBufferedImpl();
		XLog testLog = factory.createLog();
		
		XAttributeMap attributeMap = factory.createAttributeMap();
		
		XAttributeLiteral callerLiteral = factory.createAttributeLiteral("Caller", "com.java.test.Caller", null);
		XAttributeLiteral calleeLiteral = factory.createAttributeLiteral("Callee", "com.java.test.Callee", null);
		XAttributeLiteral callLiteral = factory.createAttributeLiteral("Call", "testCall()", null);
		
		attributeMap.put("Caller", callerLiteral);
		attributeMap.put("Callee", calleeLiteral);
		attributeMap.put("Call", callLiteral);
		
		XTrace trace = factory.createTrace();
		
		XEvent event = factory.createEvent();
		event.setAttributes(attributeMap);
		
		trace.add(event);
				
		testLog.add(trace);
		
		return testLog;
	}
	
	
}

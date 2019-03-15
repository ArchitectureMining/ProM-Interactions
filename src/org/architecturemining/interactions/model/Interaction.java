package org.architecturemining.interactions.model;

import org.deckfour.xes.model.XEvent;

public class Interaction {

	private String caller;
	private String callee;
	private String call;
	private XEvent event;
	
	public Interaction(String caller, String callee, XEvent event) {
		this.caller = caller;
		this.callee = callee;
		this.event = event;
	}

	/**
	 * @return the caller
	 */
	public String getCaller() {
		return caller;
	}

	/**
	 * @return the callee
	 */
	public String getCallee() {
		return callee;
	}

	/**
	 * @return the call
	 */
	public String getCall() {
		return call;
	}

	/**
	 * @return the event
	 */
	public XEvent getEvent() {
		return event;
	}
}

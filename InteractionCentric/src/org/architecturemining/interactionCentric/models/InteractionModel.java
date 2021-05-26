package org.architecturemining.interactionCentric.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.processmining.framework.annotations.AuthoredType;

/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Model containing all interactions parsed from a single event log containing interactions.
*/
@AuthoredType(typeName = "Interaction Model",
affiliation = "Utrecht University", 
author = "Arnout Verhaar", 
email = "w.d.verhaar@students.uu.nl")
public class InteractionModel {

	// maps a entity name to an index.
	public Map<String, Integer> entities = new HashMap<String, Integer>();
	public Map<Integer, String> entitiesIndexMapping = new HashMap<Integer, String>();
	// list of all entities.
	public ArrayList<String> nodeList;
	// connection matrix which counts the amount of connections between two nodes within a network. row is caller, column is callee.
	public int[][] connectionMatrix;
	// matrix filled with doubles computed when setting the connectionmatrix. value/total outgoing calls.
	public double[][] probabilityMatrix;
	public String callerTag;
	public String calleeTag;
	
	
	public InteractionModel() {
		super();
	}
	
	public InteractionModel(ArrayList<String> entityList, ParameterSettings iSettings) {
		super();
		this.nodeList = entityList;
		int cnt = 0;
		for(String x: entityList) {
			this.entities.put(x, cnt);
			this.entitiesIndexMapping.put(cnt, x);
			cnt++;
		}
		this.callerTag = iSettings.callerTag;
		this.calleeTag = iSettings.calleeTag;
	}
	
	public ArrayList<String> getNodeList() {
		return nodeList;
	}
	public void setNodeList(ArrayList<String> nodeList) {
		this.nodeList = nodeList;
	}
	public double[][] getProbabilityMatrix() {
		return probabilityMatrix;
	}
	public void setProbabilityMatrix(double[][] probabilityMatrix) {
		this.probabilityMatrix = probabilityMatrix;
	}
	
	public Map<String, Integer> getEntities() {
		return entities;
	}
	public void setEntities(Map<String, Integer> entities) {
		this.entities = entities;
	}
	public int[][] getConnectionMatrix() {
		return connectionMatrix;
	}
	
	// Sets the connectionmatrix and computes the probabiltymatrix which is always based on the connectionmatrix.
	public void setConnectionMatrix(int[][] connectionMatrix) {
		this.connectionMatrix = connectionMatrix;		
	}
	
	public void computeProbabilityMatrix(Map<String, Integer> nodesCounter) {
		this.probabilityMatrix = new double[this.entities.size()][this.entities.size()];
		
		// old method
		if(false) {
			int x = 0;		
			for(int[] row: connectionMatrix) {
				int total = IntStream.of(row).sum();
				if(total != 0) {
					int y = 0;
					for(int value: row) {
						this.probabilityMatrix[x][y] = (double)value / (double)total;
						y++;
					}
				}
				x++;
			}
		}else {
			int x = 0;
			for(int[] row: connectionMatrix) {
				String source = entitiesIndexMapping.get(x);
				int y = 0;
				for(int value: row) {
					this.probabilityMatrix[x][y] = (double)value / (double)nodesCounter.get(source);
					y++;
				}				
				x++;
			}
		}
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

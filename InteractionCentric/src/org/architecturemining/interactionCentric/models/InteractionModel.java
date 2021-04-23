package org.architecturemining.interactionCentric.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Model containing all interactions parsed from a single event log containing interactions.
*/
public class InteractionModel {

	// maps a entity name to an index.
	public Map<String, Integer> entities = new HashMap<String, Integer>();
	// list of all entities.
	public ArrayList<String> nodeList;
	// connection matrix which counts the amount of connections between two nodes within a network. row is caller, column is callee.
	public int[][] connectionMatrix;
	// matrix filled with doubles computed when setting the connectionmatrix. value/total outgoing calls.
	public double[][] probabilityMatrix;
	
	public InteractionModel(ArrayList<String> entityList) {
		super();
		this.nodeList = entityList;
		int cnt = 0;
		for(String x: entityList) {
			this.entities.put(x, cnt);
			cnt++;
		}
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
		this.probabilityMatrix = new double[this.entities.size()][this.entities.size()];
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
	}
}

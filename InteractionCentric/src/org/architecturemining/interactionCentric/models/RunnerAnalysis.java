package org.architecturemining.interactionCentric.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.architecturemining.interactionCentric.models.runnerAnalysis.SingleTraceVertexInfo;
import org.architecturemining.interactionCentric.models.runnerAnalysis.SingleVertexAnalysis;
import org.architecturemining.interactionCentric.models.runnerAnalysis.infoPerNode;


public class RunnerAnalysis {

	public Map<String, SingleVertexAnalysis> vertexAnalysis;
	public List<infoPerNode> sortedAnalysis;
	public int totalTraces, totalInteractions;

	public RunnerAnalysis(Map<String, SingleVertexAnalysis> vertexAnalysis, int totalTraces, int totalInteractions) {
		super();
		this.vertexAnalysis = vertexAnalysis;
		this.totalTraces = totalTraces;
		this.totalInteractions = totalInteractions;
		sortAnalysis();
	}
	
	public void sortAnalysis() {
		sortedAnalysis = new ArrayList<infoPerNode>();
		
		for(Entry<String, SingleVertexAnalysis> sva : vertexAnalysis.entrySet()) {
			double total = 0, incoming = 0, outgoing = 0;
			int outgoingSize = sva.getValue().getOutgoingTraces().size();
			int incomingSize = sva.getValue().getIncomingTraces().size();
			for( SingleTraceVertexInfo st : sva.getValue().getOutgoingTraces()) {
				total += st.likelihood;
				outgoing += st.likelihood;
			}
			for( SingleTraceVertexInfo st : sva.getValue().getIncomingTraces()) {
				total += st.likelihood;
				incoming += st.likelihood;
			}
			
			sortedAnalysis.add(new infoPerNode(sva.getKey(), total / (incomingSize + outgoingSize), incoming / incomingSize, outgoing / outgoingSize));
			
		}
		
		sortedAnalysis.sort(Comparator.comparingDouble(infoPerNode::getAverageTotal));
		System.out.println(sortedAnalysis);
	}
}


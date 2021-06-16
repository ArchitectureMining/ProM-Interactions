package org.architecturemining.interactionCentric.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.architecturemining.interactionCentric.models.LinkedListEdgesSet.EdgeMap;
import org.architecturemining.interactionCentric.visualizer.graph.GraphEdge;
import org.architecturemining.interactionCentric.visualizer.graph.GraphNode;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

public class HelperFunctions {

	public static EdgeMap buildEdgeMap(XTrace trace, XESFunctions xes, List<String> nodeNames, boolean incorporateMessageTypes){
		
		Set<String> sourceValues = xes.getSourceAttributeValues(trace);
		Set<String> sinkValues = xes.getSinkAttributeValues(trace);
		
		Set<String> uniquevalues = new HashSet<String>();
		uniquevalues.addAll(sourceValues);
		uniquevalues.addAll(sinkValues);
		
		Map<String, Set<String>> edges = new HashMap<String, Set<String>>();
		Map<String, Set<String>> prevNodes =  new HashMap<String, Set<String>>();
		for(String node: nodeNames) {
			edges.put(node, new HashSet<String>());
		}

		for(XEvent ev: trace) {
			
			String caller = xes.getCaller(ev);
			String callee = xes.getCallee(ev);
			String message;
			if(incorporateMessageTypes) {
				message = xes.getEventType(ev) + "_event";
				edges.get(caller).add(message);
				edges.get(message).add(callee);
				
				if(prevNodes.containsKey(message))
					prevNodes.get(message).add(caller);
				else
					prevNodes.put(message, new HashSet<String>(Arrays.asList(caller)));
				
				if(prevNodes.containsKey(callee))
					prevNodes.get(callee).add(message);
				else
					prevNodes.put(callee, new HashSet<String>(Arrays.asList(message)));
			}else {
				edges.get(caller).add(callee);
				if(prevNodes.containsKey(callee))
					prevNodes.get(callee).add(caller);
				else
					prevNodes.put(callee, new HashSet<String>(Arrays.asList(caller)));
			}	
		}
		
		for(String x: xes.getStarterNodes(sinkValues, uniquevalues)) {		
			edges.get("start").add(x);		
			if(prevNodes.containsKey(x)) 
				prevNodes.get(x).add("start");
			else 
				prevNodes.put(x, new HashSet<String>(Arrays.asList("start")));
			
		}
		
		for(String x: xes.getEndNodes(sourceValues, uniquevalues)) {
			edges.get(x).add("end");
			if(prevNodes.containsKey("end")) 
				prevNodes.get("end").add(x);
			else 
				prevNodes.put("end", new HashSet<String>(Arrays.asList(x)));
			
		}
		
		return new EdgeMap(edges, prevNodes);
	}
	
	public static mxGraphComponent createGraphPanel(DefaultDirectedGraph<GraphNode, GraphEdge> ddg, boolean withoutStart, boolean withoutEnd) {
        // create a visualization using JGraph, via an adapter		
		GraphNode endNode = null, startNode = null;
		Set<GraphNode> vertices = ddg.vertexSet();
		
		if(withoutEnd) {		
			for(GraphNode x: vertices) {
				if(x.fullName.equals("end")) {
					endNode = x;			
					break;
				}
			}			       	
        }
		
		if(withoutStart) {		
			for(GraphNode x: vertices) {
				if(x.fullName.equals("start")) {
					startNode = x;
					break;
				}
			}			       	
        }
			
		List<GraphNode> removeTheseNodes = new ArrayList<GraphNode>();
		for(GraphNode x: vertices) {
			if(ddg.outgoingEdgesOf(x).size() == 0 && !x.fullName.equals("end")) {
				removeTheseNodes.add(x);
			}
		}
		
		
		ddg.removeVertex(startNode);
		ddg.removeVertex(endNode);
		ddg.removeAllVertices(removeTheseNodes);
		
        JGraphXAdapter<GraphNode, GraphEdge> jgxAdapter = new JGraphXAdapter<GraphNode, GraphEdge>(ddg);
        HashMap<mxICell, GraphNode> cellToNodeMap = jgxAdapter.getCellToVertexMap();
        mxGraphComponent component = new mxGraphComponent(jgxAdapter);
        component.setConnectable(false);
        mxGraph graphVisual = component.getGraph();
        graphVisual.setAllowDanglingEdges(false);
        graphVisual.setCellsEditable(true);
        
        // positioning via jgraphx layouts
        mxFastOrganicLayout layout = new mxFastOrganicLayout(jgxAdapter);  
        
        layout.setForceConstant(150); // the higher, the more separated
        layout.setMinDistanceLimit(5);
        layout.setMaxIterations(10000);
                
        Map<String, Object> nodeStyle = graphVisual.getStylesheet().getDefaultVertexStyle();
        graphVisual.setHtmlLabels(true);
        nodeStyle.put(mxConstants.STYLE_FILLCOLOR, "#444444");
        nodeStyle.put(mxConstants.STYLE_STROKEWIDTH, "2");
        
        
        
        Map<String, Object> edgeStyle = graphVisual.getStylesheet().getDefaultEdgeStyle();
        edgeStyle.put(mxConstants.STYLE_STROKEWIDTH, 4);
        edgeStyle.put(mxConstants.STYLE_STARTSIZE, 8);
        edgeStyle.put(mxConstants.STYLE_ENDSIZE, 8);

        layout.execute(jgxAdapter.getDefaultParent());  
        graphVisual.setCellsResizable(false);
        graphVisual.setAutoSizeCells(false);
        
        //repaint the event cells
        Object[] x = graphVisual.getChildVertices(graphVisual.getDefaultParent());
        Object[] eventNodes = Arrays.asList(x).stream().filter(v -> StringUtils.containsIgnoreCase(cellToNodeMap.get(v).fullName, "_event")).collect(Collectors.toList()).toArray();
        graphVisual.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#cbb57f", eventNodes);
        
        
        return component;
	}
	
}

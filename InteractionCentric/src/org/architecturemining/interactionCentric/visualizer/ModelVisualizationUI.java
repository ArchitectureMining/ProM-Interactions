package org.architecturemining.interactionCentric.visualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.architecturemining.interactionCentric.models.InteractionModel;
import org.architecturemining.interactionCentric.visualizer.graph.GraphEdge;
import org.architecturemining.interactionCentric.visualizer.graph.GraphNode;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.processmining.framework.plugin.PluginContext;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;


/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Class that extends the JPanel and will actually show the panel as a result of the plugin.
*/

public class ModelVisualizationUI extends JPanel {

	// generated serial id
	private static final long serialVersionUID = 1757843017222865547L;
	private boolean hideStart, hideEnd;
	private mxGraphComponent graphPanel;
	
	public ModelVisualizationUI(PluginContext context, InteractionModel iModel) {
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel optionsPanel = new JPanel();
		JPanel graph_wrapper = new JPanel();
		
		this.setBackground(Color.white);
				
		// Create graph
		InteractionGraph graph = new InteractionGraph(iModel);

		this.setLayout(new BorderLayout());
		
		// 	Visualization panel
		//ProMJGraphPanel visualisation = ProMJGraphVisualizer.instance().visualizeGraph(context, graph);  
		
		
		// Compose panel
		graphPanel = createGraphPanel(graph);
			
		mainPanel.add(optionsPanel, BorderLayout.WEST);
		
		JCheckBox hideStartCheckbox = new JCheckBox("Hide 'start' node");
		hideStartCheckbox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				hideStart = e.getStateChange() == ItemEvent.SELECTED;
				graph_wrapper.removeAll();
				InteractionGraph graph = new InteractionGraph(iModel);
				graphPanel = createGraphPanel(graph);				
				graph_wrapper.add(graphPanel, BorderLayout.CENTER);
				graph_wrapper.revalidate();
				graph_wrapper.repaint();
			}
		});
		
		JLabel lblNewLabel = new JLabel("Graph Options");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		JCheckBox hideEndCheckbox = new JCheckBox("Hide 'end' node");
		hideEndCheckbox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				hideEnd = e.getStateChange() == ItemEvent.SELECTED;
				graph_wrapper.removeAll();
				InteractionGraph graph = new InteractionGraph(iModel);
				graphPanel = createGraphPanel(graph);	
				graphPanel.setGridStyle(graphPanel.GRID_STYLE_DASHED);
				graph_wrapper.add(graphPanel, BorderLayout.CENTER);
				graph_wrapper.revalidate();
				graph_wrapper.repaint();
			}
		});
		GroupLayout gl_optionsPanel = new GroupLayout(optionsPanel);
		gl_optionsPanel.setHorizontalGroup(
			gl_optionsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_optionsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_optionsPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(hideStartCheckbox, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
						.addComponent(hideEndCheckbox, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(66, Short.MAX_VALUE))
		);
		gl_optionsPanel.setVerticalGroup(
			gl_optionsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_optionsPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(19)
					.addComponent(hideStartCheckbox)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(hideEndCheckbox)
					.addContainerGap(759, Short.MAX_VALUE))
		);
		optionsPanel.setLayout(gl_optionsPanel);	
		graph_wrapper.setLayout(new BorderLayout(0, 0));
		graph_wrapper.add(graphPanel);
		mainPanel.add(graph_wrapper, BorderLayout.CENTER);
		this.add(mainPanel, BorderLayout.CENTER);
	}
	
	
	private mxGraphComponent createGraphPanel(DefaultDirectedGraph<GraphNode, GraphEdge> ddg) {
		
		GraphNode endNode = null, startNode = null;
		System.out.println(hideEnd);
		System.out.println(hideStart);
		
		if(hideEnd) {		
			for(GraphNode x: ddg.vertexSet()) {
				if(x.fullName.equals("end")) {
					endNode = x;			
					break;
				}
			}			       	
        }
		
		if(hideStart) {		
			for(GraphNode x: ddg.vertexSet()) {
				if(x.fullName.equals("start")) {
					startNode = x;
					break;
				}
			}			       	
        }
		
		ddg.removeVertex(startNode);
		ddg.removeVertex(endNode);
		
        // create a visualization using JGraph, via an adapter
        JGraphXAdapter<GraphNode, GraphEdge> jgxAdapter = new JGraphXAdapter<GraphNode, GraphEdge>(ddg);
       

        mxGraphComponent graph_instance = new mxGraphComponent(jgxAdapter);
        graph_instance.setConnectable(false);
        mxGraph graphVisual = graph_instance.getGraph();
        graphVisual.setAllowDanglingEdges(false);
        
  		graphVisual.setStylesheet(createDefaultEdgeStyle());
  		jgxAdapter.setStylesheet(createDefaultEdgeStyle());

        // positioning via jgraphx layouts
        mxFastOrganicLayout layout = new mxFastOrganicLayout(jgxAdapter);

        layout.execute(jgxAdapter.getDefaultParent());
        layout.setForceConstant(60 + ddg.vertexSet().size() * 2); // the higher, the more separated
        layout.setDisableEdgeStyle(true); // true transforms the edges and makes them direct lines
        
     // layout using morphing
        graphVisual.getModel().beginUpdate();
        try {
            layout.execute(graphVisual.getDefaultParent());
        } finally {
            mxMorphing morph = new mxMorphing(graph_instance, 4, 50, 70);

            morph.addListener(mxEvent.DONE, new mxIEventListener() {
                @Override
                public void invoke(Object arg0, mxEventObject arg1) {
                	graphVisual.getModel().endUpdate();
                }

            });

            morph.startAnimation();
        }
        
        graph_instance.setZoomPolicy(1);
        return graph_instance;
	}
	
	
	protected mxStylesheet createDefaultEdgeStyle()
    {
        Map<String, Object> style = new Hashtable<String, Object>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
        style.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
        style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        style.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        style.put(mxConstants.STYLE_STROKECOLOR, "#6482B9");
        style.put(mxConstants.STYLE_FONTCOLOR, "#446299");
        
        mxStylesheet mxStyle = new mxStylesheet();
        mxStyle.setDefaultVertexStyle(style);
        
        return mxStyle;
    }
}

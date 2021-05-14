package org.architecturemining.interactionCentric.visualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JPanel;

import org.architecturemining.interactionCentric.models.InteractionModel;
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
	
	public ModelVisualizationUI(PluginContext context, InteractionModel iModel) {
		this.setBackground(Color.white);
				
		// Create graph
		InteractionGraph graph = new InteractionGraph(iModel);

		this.setLayout(new BorderLayout());
		
		// 	Visualization panel
		//ProMJGraphPanel visualisation = ProMJGraphVisualizer.instance().visualizeGraph(context, graph);  
		
		
		// Compose panel
		mxGraphComponent graphPanel = createGraphPanel(graph);
		this.add(graphPanel, BorderLayout.CENTER);	
	}
	
	
	private mxGraphComponent createGraphPanel(DefaultDirectedGraph ddg) {
		JPanel jp = new JPanel();
        // create a visualization using JGraph, via an adapter
        JGraphXAdapter jgxAdapter = new JGraphXAdapter<>(ddg);

        mxGraphComponent component = new mxGraphComponent(jgxAdapter);
        component.setConnectable(false);
        mxGraph graphVisual = component.getGraph();
        graphVisual.setAllowDanglingEdges(false);
        
  		graphVisual.setStylesheet(createDefaultEdgeStyle());
  		jgxAdapter.setStylesheet(createDefaultEdgeStyle());
        jp.add(component);

        // positioning via jgraphx layouts
        mxFastOrganicLayout layout = new mxFastOrganicLayout(jgxAdapter);

        layout.execute(jgxAdapter.getDefaultParent());
        layout.setForceConstant(150); // the higher, the more separated
        layout.setDisableEdgeStyle(true); // true transforms the edges and makes them direct lines
        
     // layout using morphing
        graphVisual.getModel().beginUpdate();
        try {
            layout.execute(graphVisual.getDefaultParent());
        } finally {
            mxMorphing morph = new mxMorphing(component, 4, 5, 70);

            morph.addListener(mxEvent.DONE, new mxIEventListener() {
                @Override
                public void invoke(Object arg0, mxEventObject arg1) {
                	graphVisual.getModel().endUpdate();
                }

            });

            morph.startAnimation();
        }
        
        //jgxAdapter.getStylesheet().setDefaultVertexStyle(nodeStyle);
        return component;
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

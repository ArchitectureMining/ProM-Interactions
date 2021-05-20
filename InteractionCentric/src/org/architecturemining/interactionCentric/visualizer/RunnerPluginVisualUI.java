package org.architecturemining.interactionCentric.visualizer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.architecturemining.interactionCentric.models.TracesLikelihood;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.processmining.framework.plugin.PluginContext;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;

public class RunnerPluginVisualUI extends JPanel {
	private final JPanel topbar = new JPanel();
	public RunnerPluginVisualUI(PluginContext context, TracesLikelihood tL) {
		topbar.setBackground(Color.GRAY);
		this.setBackground(Color.white);
		
		JPanel contentPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(contentPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(topbar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(topbar, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, 488, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(113, Short.MAX_VALUE))
		);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		splitPane.setResizeWeight(1.0);
		
		splitPane.setOneTouchExpandable(true);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addComponent(splitPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE)
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addComponent(splitPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
		);
		
		JPanel right_panel = new JPanel();
		splitPane.setRightComponent(right_panel);
		
		
		JPanel left_list_panel = new JPanel();
		splitPane.setLeftComponent(left_list_panel);
		splitPane.setDividerLocation(0.3);
		DefaultListModel<String> traceList = new DefaultListModel<String>();
		for(int cnt = 1; cnt <= tL.traces.size(); cnt++){
			traceList.addElement("Trace " + cnt);
		}
		
		JList<String> tracesList = new JList<String>(traceList);
		
		JLabel list_title = new JLabel("Runned traces");
		list_title.setFont(new Font("Tahoma", Font.BOLD, 18));
		GroupLayout gl_left_list_panel = new GroupLayout(left_list_panel);
		gl_left_list_panel.setHorizontalGroup(
			gl_left_list_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_left_list_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_left_list_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(tracesList, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
						.addComponent(list_title))
					.addContainerGap(380, Short.MAX_VALUE))
		);
		gl_left_list_panel.setVerticalGroup(
			gl_left_list_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_left_list_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(list_title)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tracesList, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
					.addContainerGap())
		);
		left_list_panel.setLayout(gl_left_list_panel);
		contentPanel.setLayout(gl_contentPanel);
		
		JLabel lblNewLabel = new JLabel("Runner Plugin Results");
		lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNewLabel.setForeground(Color.WHITE);
		GroupLayout gl_topbar = new GroupLayout(topbar);
		gl_topbar.setHorizontalGroup(
			gl_topbar.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_topbar.createSequentialGroup()
					.addGap(23)
					.addComponent(lblNewLabel)
					.addContainerGap(589, Short.MAX_VALUE))
		);
		gl_topbar.setVerticalGroup(
			gl_topbar.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_topbar.createSequentialGroup()
					.addGap(22)
					.addComponent(lblNewLabel)
					.addContainerGap(24, Short.MAX_VALUE))
		);
		topbar.setLayout(gl_topbar);
		setLayout(groupLayout);
		right_panel.setPreferredSize(new Dimension(700, 486));
		
		JPanel graph_panel = new JPanel();
		
		JLabel traceLabel = new JLabel("Select a trace");
		traceLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		GroupLayout gl_right_panel = new GroupLayout(right_panel);
		gl_right_panel.setHorizontalGroup(
			gl_right_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_right_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_right_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(traceLabel)
						.addComponent(graph_panel, GroupLayout.PREFERRED_SIZE, 579, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(346, Short.MAX_VALUE))
		);
		gl_right_panel.setVerticalGroup(
			gl_right_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_right_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(traceLabel)
					.addPreferredGap(ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
					.addComponent(graph_panel, GroupLayout.PREFERRED_SIZE, 437, GroupLayout.PREFERRED_SIZE))
		);
		right_panel.setLayout(gl_right_panel);
		
	//  Compose panel
		JTextArea textField = new JTextArea();
		textField.setText(tL.toString());
		//this.add(textField, BorderLayout.CENTER);
		
		tracesList.addListSelectionListener(new ListSelectionListener() {		
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()) {
					graph_panel.removeAll();				
			    	InteractionGraph graph = new InteractionGraph(tL.traces.get(tracesList.getSelectedIndex()));
					mxGraphComponent graph_visual = createGraphPanel(graph);				
					GroupLayout gl_graph_panel = new GroupLayout(graph_panel);
					
					gl_graph_panel.setHorizontalGroup(
						gl_graph_panel.createParallelGroup(Alignment.LEADING)
							.addComponent(graph_visual, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
					);
					gl_graph_panel.setVerticalGroup(
						gl_graph_panel.createParallelGroup(Alignment.LEADING)
							.addComponent(graph_visual, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
					);
					graph_panel.setLayout(gl_graph_panel);
					splitPane.resetToPreferredSizes();
					DecimalFormat df2 = new DecimalFormat("#.##");
					String likelihood = df2.format(tL.traces.get(tracesList.getSelectedIndex()).getLikelihood());
					
					traceLabel.setText("Trace " + (tracesList.getSelectedIndex() + 1) + " | Computed Likelihood: " + likelihood);
					
				}	
			}
		});
		
		splitPane.resetToPreferredSizes();
		
		
		// Create graph
		/*InteractionGraph graph = new InteractionGraph(tL.traces.get(0));
		graph_panel.add(createGraphPanel(graph));*/
			
		
	}
	
	private mxGraphComponent createGraphPanel(DefaultDirectedGraph ddg) {
        // create a visualization using JGraph, via an adapter
        JGraphXAdapter jgxAdapter = new JGraphXAdapter<>(ddg);

        mxGraphComponent component = new mxGraphComponent(jgxAdapter);
        component.setConnectable(false);
        mxGraph graphVisual = component.getGraph();
        graphVisual.setAllowDanglingEdges(false);

        // positioning via jgraphx layouts
        mxFastOrganicLayout layout = new mxFastOrganicLayout(jgxAdapter);

        layout.execute(jgxAdapter.getDefaultParent());
        layout.setForceConstant(90); // the higher, the more separated
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
}

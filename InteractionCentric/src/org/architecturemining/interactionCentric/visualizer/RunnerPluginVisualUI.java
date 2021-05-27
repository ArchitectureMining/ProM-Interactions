package org.architecturemining.interactionCentric.visualizer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.architecturemining.interactionCentric.models.SingleLikelihood;
import org.architecturemining.interactionCentric.models.TracesLikelihood;
import org.architecturemining.interactionCentric.visualizer.graph.GraphEdge;
import org.architecturemining.interactionCentric.visualizer.graph.GraphNode;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.processmining.framework.plugin.PluginContext;
import org.python.icu.text.DecimalFormat;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;

public class RunnerPluginVisualUI extends JPanel {
	private final JPanel topbar = new JPanel();
	private boolean showEnd, showStart;
	public RunnerPluginVisualUI(PluginContext context, TracesLikelihood tL) {
		topbar.setBackground(Color.GRAY);
		this.setBackground(Color.white);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		JPanel contentPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(contentPanel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 1079, GroupLayout.PREFERRED_SIZE)
						.addComponent(topbar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1079, Short.MAX_VALUE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(topbar, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, 952, GroupLayout.PREFERRED_SIZE))
		);
		
		DefaultListModel<String> traceList = new DefaultListModel<String>();
		for(int cnt = 1; cnt <= tL.traces.size(); cnt++){
			traceList.addElement("Trace " + cnt);
		}
		
		JPanel left_list_panel = new JPanel();
		left_list_panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		JList<String> tracesList = new JList<String>(traceList);
		
		JLabel list_title = new JLabel("Inserted traces");
		list_title.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		JPanel LikelihoodCalculationPanel = new JPanel();
		GroupLayout gl_left_list_panel = new GroupLayout(left_list_panel);
		gl_left_list_panel.setHorizontalGroup(
			gl_left_list_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_left_list_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_left_list_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_left_list_panel.createSequentialGroup()
							.addComponent(tracesList, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(LikelihoodCalculationPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(list_title))
					.addContainerGap(16, Short.MAX_VALUE))
		);
		gl_left_list_panel.setVerticalGroup(
			gl_left_list_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_left_list_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(list_title)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_left_list_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(LikelihoodCalculationPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
						.addComponent(tracesList, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		JLabel lblNewLabel_1 = new JLabel("Likelihood Calculation");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel likelihoodCalculation = new JLabel("");
		likelihoodCalculation.setVerticalAlignment(SwingConstants.TOP);
		likelihoodCalculation.setFont(new Font("Tahoma", Font.ITALIC, 12));
		GroupLayout gl_LikelihoodCalculationPanel = new GroupLayout(LikelihoodCalculationPanel);
		gl_LikelihoodCalculationPanel.setHorizontalGroup(
			gl_LikelihoodCalculationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_LikelihoodCalculationPanel.createSequentialGroup()
					.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
					.addGap(20))
				.addGroup(gl_LikelihoodCalculationPanel.createSequentialGroup()
					.addGap(10)
					.addComponent(likelihoodCalculation, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_LikelihoodCalculationPanel.setVerticalGroup(
			gl_LikelihoodCalculationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_LikelihoodCalculationPanel.createSequentialGroup()
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(likelihoodCalculation, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
					.addContainerGap())
		);
		LikelihoodCalculationPanel.setLayout(gl_LikelihoodCalculationPanel);
		left_list_panel.setLayout(gl_left_list_panel);
		//this.add(textField, BorderLayout.CENTER);
		
		JPanel right_panel = new JPanel();
		right_panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		right_panel.setPreferredSize(new Dimension(700, 486));
		JPanel graph_panel = new JPanel();
		
		
		

		
		JLabel traceLabel = new JLabel("Select a trace");
		traceLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		JCheckBox removeEnd = new JCheckBox("Remove End node");
		removeEnd.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {			
				showEnd = removeEnd.isSelected();
				int index = tracesList.getSelectedIndex();
				tracesList.clearSelection();
				tracesList.setSelectedIndex(index);
			}
		});
		
		JCheckBox removeStart = new JCheckBox("Remove Start node");
		removeStart.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				showStart = removeStart.isSelected();
				int index = tracesList.getSelectedIndex();
				tracesList.clearSelection();
				tracesList.setSelectedIndex(index);
			}
		});
		GroupLayout gl_right_panel = new GroupLayout(right_panel);
		gl_right_panel.setHorizontalGroup(
			gl_right_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_right_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_right_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(graph_panel, GroupLayout.PREFERRED_SIZE, 652, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_right_panel.createSequentialGroup()
							.addComponent(traceLabel)
							.addPreferredGap(ComponentPlacement.RELATED, 388, Short.MAX_VALUE)
							.addGroup(gl_right_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(removeEnd, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
								.addComponent(removeStart, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		gl_right_panel.setVerticalGroup(
			gl_right_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_right_panel.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_right_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_right_panel.createSequentialGroup()
							.addComponent(removeEnd)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(removeStart)
							.addGap(6))
						.addGroup(gl_right_panel.createSequentialGroup()
							.addComponent(traceLabel)
							.addGap(18)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(graph_panel, GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
					.addContainerGap())
		);
		GroupLayout gl_graph_panel = new GroupLayout(graph_panel);
		gl_graph_panel.setHorizontalGroup(
			gl_graph_panel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 135, Short.MAX_VALUE)
		);
		gl_graph_panel.setVerticalGroup(
			gl_graph_panel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 437, Short.MAX_VALUE)
		);
		graph_panel.setLayout(gl_graph_panel);
		right_panel.setLayout(gl_right_panel);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(left_list_panel, GroupLayout.PREFERRED_SIZE, 382, GroupLayout.PREFERRED_SIZE)
					.addGap(15)
					.addComponent(right_panel, GroupLayout.PREFERRED_SIZE, 672, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(left_list_panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(right_panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE))
					.addGap(287))
		);

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
		
	//  Compose panel
		JTextArea textField = new JTextArea();
		textField.setText(tL.toString());		
		
		tracesList.addListSelectionListener(new ListSelectionListener() {		
			public void valueChanged(ListSelectionEvent e) {
				if(tracesList.getSelectedIndex() >= 0) {
					SingleLikelihood selection = tL.traces.get(tracesList.getSelectedIndex());
					graph_panel.removeAll();				
			    	InteractionGraph graph = new InteractionGraph(tL.traces.get(tracesList.getSelectedIndex()));
					mxGraphComponent graph_visual = createGraphPanel(graph, showEnd, showStart);				
					GroupLayout gl_graph_panel = new GroupLayout(graph_panel);
					gl_graph_panel.setHorizontalGroup(
						gl_graph_panel.createParallelGroup(Alignment.LEADING)
							.addComponent(graph_visual, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, right_panel.getWidth(), Short.MAX_VALUE)
					);
					gl_graph_panel.setVerticalGroup(
						gl_graph_panel.createParallelGroup(Alignment.LEADING)
							.addComponent(graph_visual, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, right_panel.getHeight(), Short.MAX_VALUE)
					);
					graph_panel.setLayout(gl_graph_panel);
		
					DecimalFormat df2 = new DecimalFormat("#.###");
					//String likelihood = df2.format(selection.getLikelihood(selectedLikelihoodFunction));
					
					
					StringBuilder sb = new StringBuilder();
					sb.append("<html>");
					for(Map.Entry<String, Double> ent: selection.getLikelihood().entrySet()) {						
						sb.append(ent.getKey());
						sb.append("<br/>");
						sb.append(df2.format(ent.getValue()));
						sb.append("<br/>");
					}
					sb.append("</html>");
					
					likelihoodCalculation.setText(sb.toString());
					traceLabel.setText("Trace " + (tracesList.getSelectedIndex() + 1));
				}
			}			
		});
		
		// Create graph
		/*InteractionGraph graph = new InteractionGraph(tL.traces.get(0));
		graph_panel.add(createGraphPanel(graph));*/
			
		
	}
	
	private mxGraphComponent createGraphPanel(DefaultDirectedGraph<GraphNode, GraphEdge> ddg, boolean withoutEnd, boolean withoutStart) {
        // create a visualization using JGraph, via an adapter		
		GraphNode endNode = null, startNode = null;
		if(withoutEnd) {		
			for(GraphNode x: ddg.vertexSet()) {
				if(x.fullName.equals("end")) {
					endNode = x;			
					break;
				}
			}			       	
        }
		
		if(withoutStart) {		
			for(GraphNode x: ddg.vertexSet()) {
				if(x.fullName.equals("start")) {
					startNode = x;
					break;
				}
			}			       	
        }
		
		ddg.removeVertex(startNode);
		ddg.removeVertex(endNode);
		
        JGraphXAdapter jgxAdapter = new JGraphXAdapter<>(ddg);

        mxGraphComponent component = new mxGraphComponent(jgxAdapter);
        component.setConnectable(false);
        mxGraph graphVisual = component.getGraph();
        graphVisual.setAllowDanglingEdges(false);
        graphVisual.setCellsEditable(true);
        
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

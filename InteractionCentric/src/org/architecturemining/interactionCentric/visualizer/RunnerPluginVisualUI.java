package org.architecturemining.interactionCentric.visualizer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
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
		
		DefaultListModel<String> traceList = new DefaultListModel<String>();
		for(int cnt = 1; cnt <= tL.traces.size(); cnt++){
			traceList.addElement("Trace " + cnt);
		}
		
		JPanel left_list_panel = new JPanel();
		left_list_panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JList<String> tracesList = new JList<String>(traceList);
		tracesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		
		JLabel list_title = new JLabel("Inserted traces");
		list_title.setFont(new Font("Tahoma", Font.BOLD, 18));
		list_title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JPanel LikelihoodCalculationPanel = new JPanel();
		LikelihoodCalculationPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
		
		
		JLabel lblNewLabel_1 = new JLabel("Likelihood Calculation");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel likelihoodCalculation = new JLabel("");
		likelihoodCalculation.setVerticalAlignment(SwingConstants.TOP);
		likelihoodCalculation.setFont(new Font("Tahoma", Font.ITALIC, 12));
		//this.add(textField, BorderLayout.CENTER);
		
		JPanel right_panel = new JPanel();
		right_panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JPanel graph_panel = new JPanel();
		
		
		

		
		JLabel traceLabel = new JLabel("Select a trace");
		traceLabel.setHorizontalAlignment(SwingConstants.LEFT);
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
		setLayout(new BorderLayout(5, 5));
		add(contentPanel);
		contentPanel.setLayout(new BorderLayout(5, 5));
		contentPanel.add(left_list_panel, BorderLayout.WEST);
		left_list_panel.setLayout(new BorderLayout(5, 5));
		left_list_panel.add(tracesList, BorderLayout.WEST);
		left_list_panel.add(new JScrollPane(tracesList));
		left_list_panel.add(LikelihoodCalculationPanel, BorderLayout.EAST);
		LikelihoodCalculationPanel.setLayout(new BorderLayout(5, 5));
		LikelihoodCalculationPanel.add(lblNewLabel_1, BorderLayout.NORTH);
		LikelihoodCalculationPanel.add(likelihoodCalculation);
		left_list_panel.add(list_title, BorderLayout.NORTH);
		contentPanel.add(right_panel);
		right_panel.setLayout(new BorderLayout(5, 5));
		right_panel.add(graph_panel, BorderLayout.CENTER);
		
		
		
		JPanel optionsPanel = new JPanel();
		right_panel.add(optionsPanel, BorderLayout.NORTH);
		optionsPanel.setLayout(new BorderLayout(5, 5));
		add(topbar, BorderLayout.NORTH);
		
		optionsPanel.add(traceLabel);
		
		JPanel checkBoxesPanel = new JPanel();
		optionsPanel.add(checkBoxesPanel, BorderLayout.EAST);
		checkBoxesPanel.add(removeEnd, BorderLayout.NORTH);
		checkBoxesPanel.add(removeStart, BorderLayout.SOUTH);
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
}

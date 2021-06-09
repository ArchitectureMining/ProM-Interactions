package org.architecturemining.interactionCentric.visualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.architecturemining.interactionCentric.models.InteractionModel;
import org.architecturemining.interactionCentric.util.HelperFunctions;
import org.processmining.framework.plugin.PluginContext;

import com.mxgraph.swing.mxGraphComponent;


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
		graphPanel = HelperFunctions.createGraphPanel(graph, hideStart, hideEnd);
			
		mainPanel.add(optionsPanel, BorderLayout.WEST);
		
		JCheckBox hideStartCheckbox = new JCheckBox("Hide 'start' node");
		hideStartCheckbox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				hideStart = e.getStateChange() == ItemEvent.SELECTED;
				graph_wrapper.removeAll();
				InteractionGraph graph = new InteractionGraph(iModel);
				graphPanel = HelperFunctions.createGraphPanel(graph, hideStart, hideEnd);			
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
				graphPanel = HelperFunctions.createGraphPanel(graph, hideStart, hideEnd);	
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
}

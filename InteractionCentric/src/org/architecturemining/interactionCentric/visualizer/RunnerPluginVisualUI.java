package org.architecturemining.interactionCentric.visualizer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.architecturemining.interactionCentric.models.SingleLikelihood;
import org.architecturemining.interactionCentric.models.TracesLikelihood;
import org.architecturemining.interactionCentric.util.HelperFunctions;
import org.processmining.framework.plugin.PluginContext;
import org.python.icu.text.DecimalFormat;

import com.mxgraph.swing.mxGraphComponent;

public class RunnerPluginVisualUI extends JPanel {
	private final JPanel topbar = new JPanel();
	private boolean showEnd, showStart;
	private String selectedCostFunction = "addedProbability";
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
		tracesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tracesList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                 Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                 SingleLikelihood listItem = tL.traces.get(index);
                 if (listItem != null) {
                      setText("Trace "+ (index + 1));
                      if (listItem.getBehaviour().get(selectedCostFunction)) {
                    	  setBackground(new Color(103, 194, 93)); // light green
                      } else {
                    	  setBackground(new Color(194, 74, 66)); // light red
                      }
                      if (isSelected) {
                           setBackground(getBackground().darker());
                      }
                 }
                 return c;
            }

       });
		Dimension listDim = tracesList.getPreferredSize();
		listDim.width = 150;
		tracesList.setPreferredSize(listDim);
		tracesList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		
		JLabel list_title = new JLabel("Inserted traces");
		list_title.setFont(new Font("Tahoma", Font.BOLD, 18));
		list_title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JPanel LikelihoodCalculationPanel = new JPanel();
		LikelihoodCalculationPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
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
		
		JPanel functionSelectionPanel = new JPanel();
		LikelihoodCalculationPanel.add(functionSelectionPanel, BorderLayout.NORTH);
		functionSelectionPanel.setLayout(new BorderLayout(10, 10));
		
		JLabel lblNewLabel_2 = new JLabel("Select cost function");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		functionSelectionPanel.add(lblNewLabel_2, BorderLayout.NORTH);
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {		
				if(e.getStateChange() == ItemEvent.SELECTED) {
					System.out.println(comboBox.getSelectedItem());
					selectedCostFunction = (String) comboBox.getSelectedItem();
					tracesList.repaint();
					tracesList.updateUI();
				}
			}
		});
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"addedProbability", "timesProbability", "customProbability"}));
		functionSelectionPanel.add(comboBox, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		functionSelectionPanel.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(10, 10));
		
		
		JLabel lblNewLabel_1 = new JLabel("Likelihood Calculation");
		panel.add(lblNewLabel_1, BorderLayout.NORTH);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel likelihoodCalculation = new JLabel("");
		panel.add(likelihoodCalculation, BorderLayout.SOUTH);
		likelihoodCalculation.setVerticalAlignment(SwingConstants.TOP);
		likelihoodCalculation.setFont(new Font("Tahoma", Font.ITALIC, 12));
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
					mxGraphComponent graph_visual = HelperFunctions.createGraphPanel(graph, showStart, showEnd);				
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
		
	}
}

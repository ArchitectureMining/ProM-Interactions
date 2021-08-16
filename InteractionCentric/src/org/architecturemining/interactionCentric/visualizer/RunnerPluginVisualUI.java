package org.architecturemining.interactionCentric.visualizer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.PlainDocument;

import org.architecturemining.interactionCentric.models.SingleLikelihood;
import org.architecturemining.interactionCentric.models.TracesLikelihood;
import org.architecturemining.interactionCentric.util.HelperFunctions;
import org.architecturemining.interactionCentric.util.IntegerFilter;
import org.processmining.framework.plugin.PluginContext;
import org.python.icu.text.DecimalFormat;

import com.mxgraph.swing.mxGraphComponent;

public class RunnerPluginVisualUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel topbar = new JPanel();
	private boolean showEnd, showStart;
	private boolean statisticalAnalysisSelected;
	private List<String> selectedCostFunctions = new ArrayList<String>(Arrays.asList("addedProbability"));
	private TracesLikelihood tL;
	private Double statisticalThresholdValue;
	private JTextField textField_1;
	private String[] costFunctions = new String[] {"addedProbability", "minimalProbability", "timesProbability", "customProbability"};
	public RunnerPluginVisualUI(PluginContext context, TracesLikelihood tL) {
		this.tL = tL;
		topbar.setBackground(Color.GRAY);		
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
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                 Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                 SingleLikelihood listItem = tL.traces.get(index);
                 if (listItem != null) {
                      setText("Trace "+ (index + 1));
                      
                      if(statisticalAnalysisSelected) {
                    	  if(listItem.getLikelihood(selectedCostFunctions.get(0)) > statisticalThresholdValue)
                    		  setBackground(new Color(103, 194, 93)); // light green
                    	  else{
                    		  setBackground(new Color(194, 74, 66)); // light red
                    	  }
                      }else {
                    	  boolean faulty = true;
                    	  for(String cFunc : selectedCostFunctions) {
                    		  if(listItem.getBehaviour().get(cFunc)) {
                    			  faulty = false;
                    		  }
                    	  }
                    	  
	                      if (!faulty) {
	                    	  setBackground(new Color(103, 194, 93)); // light green
	                      } else {
	                    	  setBackground(new Color(194, 74, 66)); // light red
	                      }
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
				
		JTextPane traceLabel = new JTextPane();
		traceLabel.setText("Select a trace");
		traceLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		traceLabel.setEditable(false);
		traceLabel.setBackground(this.getBackground());
		
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
		
		DefaultListModel<String> costfunc = new DefaultListModel<String>();
		costfunc.addAll(Arrays.asList(costFunctions));
		JList<String> functionsListSelection = new JList<String>(costfunc);
		functionsListSelection.setToolTipText("Multiple selected items will classify anomalies when all functions score below threshold.");
		functionsListSelection.setSelectedIndex(0);
		functionsListSelection.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				selectedCostFunctions.clear();
				for(int i : functionsListSelection.getSelectedIndices()) {				
					selectedCostFunctions.add(functionsListSelection.getModel().getElementAt(i));
					tracesList.repaint();
					tracesList.updateUI();
				}
			}
		});
		functionSelectionPanel.add(functionsListSelection, BorderLayout.CENTER);
		
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
		
		JPanel StatisticalPanel = new JPanel();
		LikelihoodCalculationPanel.add(StatisticalPanel, BorderLayout.CENTER);
		StatisticalPanel.setPreferredSize(new Dimension(200, 200));
		
		JLabel lblNewLabel_3 = new JLabel("Statistical Anomaly Detection");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Use statistical analysis");
		chckbxNewCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(chckbxNewCheckBox.isSelected()) {
					textField_1.setEditable(true);
					textField_1.setEnabled(true);
					statisticalAnalysisSelected = true;
					computeStatisticalAnalysis(Integer.parseInt(textField_1.getText()));
				}else {
					textField_1.setEditable(false);
					textField_1.setEnabled(false);
					statisticalAnalysisSelected = false;
				}
				
				tracesList.repaint();
				tracesList.updateUI();
			}
			
			
		});
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout(0, 0));
		
		textField_1 = new JTextField();
		PlainDocument doc = (PlainDocument) textField_1.getDocument();
		doc.setDocumentFilter(new IntegerFilter()); // custom filter only allows for integers.
		textField_1.setText("5");
		textField_1.setEditable(false);
		textField_1.setEnabled(false);
		panel_1.add(textField_1, BorderLayout.CENTER);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Percentage worst traces:");
		panel_1.add(lblNewLabel_4, BorderLayout.NORTH);
		
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				computeStatisticalAnalysis(Integer.parseInt(textField_1.getText()));
				tracesList.repaint();
				tracesList.updateUI();
			}
		});
		GroupLayout gl_StatisticalPanel = new GroupLayout(StatisticalPanel);
		gl_StatisticalPanel.setHorizontalGroup(
			gl_StatisticalPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_StatisticalPanel.createSequentialGroup()
					.addGroup(gl_StatisticalPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_3)
						.addComponent(chckbxNewCheckBox)
						.addGroup(gl_StatisticalPanel.createSequentialGroup()
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(applyButton)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_StatisticalPanel.setVerticalGroup(
			gl_StatisticalPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_StatisticalPanel.createSequentialGroup()
					.addGap(5)
					.addComponent(lblNewLabel_3)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxNewCheckBox)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_StatisticalPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(applyButton))
					.addGap(260))
		);
		StatisticalPanel.setLayout(gl_StatisticalPanel);
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

					//String likelihood = df2.format(selection.getEdgeProbabilities().get(likelihoodCalculation));
					
					
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
					String trackingID = selection.trackingID;
					traceLabel.setText("Trace " + (tracesList.getSelectedIndex() + 1) + " (" + trackingID + ")");
					
				}
			}			
		});
		
	}
	protected void computeStatisticalAnalysis(int parseInt) {
		if(statisticalAnalysisSelected) {
			List<Double> currentList = tL.traces.stream().map(sl -> sl.getLikelihood(selectedCostFunctions.get(0))).collect(Collectors.toList());
			Collections.sort(currentList);		
			int thresholdIndex = (int) Math.ceil((double)parseInt * currentList.size() / 100);
			thresholdIndex = thresholdIndex > currentList.size()-1 ? currentList.size()-1 : thresholdIndex; 
			statisticalThresholdValue = currentList.get(thresholdIndex - 1);
		}
	}
}

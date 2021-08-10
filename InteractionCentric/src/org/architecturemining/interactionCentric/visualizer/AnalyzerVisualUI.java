package org.architecturemining.interactionCentric.visualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.architecturemining.interactionCentric.models.RunnerAnalysis;
import org.architecturemining.interactionCentric.models.runnerAnalysis.SingleTraceVertexInfo;
import org.architecturemining.interactionCentric.models.runnerAnalysis.infoPerNode;
import org.processmining.framework.plugin.PluginContext;
import org.python.icu.text.DecimalFormat;

public class AnalyzerVisualUI extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable ResultTable;
	private Double thresholdValue = 0.2;
	private DefaultTableModel model;
	private JLabel currentThresholdLabel;

	public AnalyzerVisualUI(PluginContext context, RunnerAnalysis rAnalysis) {
		setMinimumSize(new Dimension(50, 50));
		setLayout(new BorderLayout(25, 25));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Analysis Result");
		lblNewLabel.setMaximumSize(new Dimension(500, 50));
		lblNewLabel.setPreferredSize(new Dimension(500, 50));
		lblNewLabel.setAlignmentY(50.0f);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setForeground(Color.WHITE);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(18)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 822, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(403, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		currentThresholdLabel = new JLabel("0.2");
		currentThresholdLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		panel.setLayout(gl_panel);
		model = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;
			Class<?>[] types = new Class [] {
	            java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
	        };	         
	        @Override
	        public Class<?> getColumnClass(int columnIndex) {
	            switch (columnIndex) {
	            	case 0: 
	            		return String.class;
	            	case 1:
	            		return Double.class;
	            	case 2: 
	            		return Double.class;
	            	case 3:
	            		return Double.class;
	            	case 4: 
	            		return Integer.class;
	            	case 5: 
	            		return String.class;
	            	default:
	            		return String.class;
	            	
	            }
	        }
		};	
		model.addColumn("<html><b>Node name</b></html>");
		model.addColumn("<html><b>Average Total</b></html>");
		model.addColumn("<html><b>Average Incoming interactions</b></html>");
		model.addColumn("<html><b>Average outgoing interactions</b></html>");
		model.addColumn("<html><b># Anomalous interactions</b></html>");
		model.addColumn("<html><b>% Anomalous interactions</b></html>");

		ResultTable = new JTable(model);
		ResultTable.setEnabled(false);
		ResultTable.setFillsViewportHeight(true);
		
		fillTable(rAnalysis);
		
		Comparator<String> percentageComparator = new Comparator<String>() {

			public int compare(String o1, String o2) {
				// Get rid of percentage signs and compare the integers.
				Integer i1 = Integer.parseInt(o1.substring(0, o1.length() - 2));
				Integer i2 = Integer.parseInt(o2.substring(0, o2.length() - 2));
				return i1.compareTo(i2);
			}
		};
		
		Comparator<Double> doubleComparator = new Comparator<Double>() {
			public int compare(Double o1, Double o2) {
				return Double.compare(o1, o2);
			}
		};
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(ResultTable.getModel());
		ResultTable.setRowSorter(sorter);
		sorter.setComparator(5, percentageComparator);
		sorter.setComparator(1, doubleComparator);
		sorter.setComparator(2, doubleComparator);
		
		List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
        sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
		//add(ResultTable, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane(ResultTable);
		add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.WEST);
		
		JSlider slider = new JSlider();
		slider.setToolTipText("Every interaction with a probability lower than this value will be denoted as anomalous");
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
		          if (!source.getValueIsAdjusting()) {
		        	  thresholdValue = (Double.valueOf(source.getValue()) / 100);
		        	  
		          }
		          currentThresholdLabel.setText(String.valueOf(thresholdValue));
		          
				
			}
		});
		slider.setPaintLabels(true);
		slider.setValue(20);
		
		Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(0, new JLabel(".00"));
        labels.put(20, new JLabel(".20"));
        labels.put(40, new JLabel(".40"));
        labels.put(60, new JLabel(".60"));
        labels.put(80, new JLabel(".80"));
        labels.put(100, new JLabel("1.00"));
        slider.setLabelTable(labels);
		
		JLabel lblNewLabel_1 = new JLabel("Analysis information");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblNewLabel_2 = new JLabel("Anomalous Threshold:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblNewLabel_3 = new JLabel("Total Traces");
		
		JLabel lblNewLabel_3_1 = new JLabel("Total Interactions");
		
		JLabel total_interactions_label = new JLabel(Integer.toString(rAnalysis.totalInteractions));
		total_interactions_label.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel total_traces_label = new JLabel(Integer.toString(rAnalysis.totalTraces));
		total_traces_label.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JButton reloadButton = new JButton("Reload Table");
		reloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillTable(rAnalysis);
			}
		});
		


		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(total_traces_label, GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_1)
								.addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(lblNewLabel_2)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(currentThresholdLabel, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblNewLabel_3)
								.addComponent(lblNewLabel_3_1, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
								.addComponent(total_interactions_label)
								.addComponent(reloadButton))
							.addContainerGap(99, Short.MAX_VALUE))))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_1)
					.addGap(18)
					.addComponent(lblNewLabel_3)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(total_traces_label)
					.addGap(18)
					.addComponent(lblNewLabel_3_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(total_interactions_label)
					.addGap(49)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(currentThresholdLabel, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(reloadButton)
					.addContainerGap(481, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
	}
	
	public void fillTable(RunnerAnalysis rAnalysis) {
		model.setRowCount(0);
		DecimalFormat df2 = new DecimalFormat("#.###");
		
		for( infoPerNode sa : rAnalysis.sortedAnalysis) {
			int allTracesCount = rAnalysis.vertexAnalysis.get(sa.nodeName).getIncomingTraces().size() + rAnalysis.vertexAnalysis.get(sa.nodeName).getOutgoingTraces().size();
			int total = 0;
			total += calculateValuesWithThreshold(thresholdValue, rAnalysis.vertexAnalysis.get(sa.nodeName).getIncomingTraces());
			total += calculateValuesWithThreshold(thresholdValue, rAnalysis.vertexAnalysis.get(sa.nodeName).getOutgoingTraces());
			
			model.addRow(new Object[] {sa.nodeName, sa.averageTotal, sa.averageIncoming, sa.averageOutgoing, total, df2.format((total * 100 / allTracesCount)) + " %"});
		}
		
		
	}
	
	public int calculateValuesWithThreshold(double threshold, List<SingleTraceVertexInfo> traces) {
		int result = traces.stream().reduce(0, (sub, elem) -> sub + (elem.likelihood > threshold ? 0 : 1), Integer::sum);
		return result;
	}
	
	
}

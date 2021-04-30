package org.architecturemining.interactionCentric.visualizer;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.architecturemining.interactionCentric.models.TracesLikelihood;
import org.processmining.framework.plugin.PluginContext;

public class LikelihoodVisualUI extends JPanel{

	private static final long serialVersionUID = 1L;
	
	public LikelihoodVisualUI(PluginContext context, TracesLikelihood tL) {
		this.setBackground(Color.white);
		this.setLayout(new BorderLayout());
		// Compose panel
		JTextArea textField = new JTextArea();
		textField.setText(tL.toString());
		this.add(textField, BorderLayout.CENTER);	
	}

}

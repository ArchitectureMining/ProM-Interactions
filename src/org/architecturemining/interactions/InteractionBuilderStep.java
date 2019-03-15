package org.architecturemining.interactions;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.architecturemining.interactions.model.InteractionBuilderSettings;
import org.architecturemining.interactions.model.InteractionLog;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;

public class InteractionBuilderStep implements ProMWizardStep<InteractionBuilderSettings> {
	public UIPluginContext context;
	private XLog log;
	
	public InteractionBuilderStep(InteractionBuilderSettings config, XLog log) {
		super();
		this.log = log;
	}

	// Once the 'Finish' button is pressed, the plugin executes this method. 
	// This method starts the business logic that converts the input of the plugin to its output
	public InteractionBuilderSettings apply(InteractionBuilderSettings config, JComponent component) {
		HierarchyPanel panel = (HierarchyPanel) component;
		
		config = panel.getConfig();
		
		InteractionLog iLog = new InteractionLog(config, log);
		config.setInteractionLog(iLog);
		
		return config;
	}

	public boolean canApply(InteractionBuilderSettings model, JComponent component) {
		// Make sure we get the data, this is a bit tricky
		HierarchyPanel panel = (HierarchyPanel) component;
		model = panel.getConfig();		
		
		String caller = model.getCaller();
		String callee = model.getCallee();
		String call = model.getCall();
		
		// We only validate the value of call if we are going to use it.
		// We will only use it if we are going to rename events
		if (model.shouldRename()) {
			if (caller != null && 
				callee != null &&
				call != null &&
				!caller.equals(callee) &&
				!call.equals(caller)) {
				return true;
			}		
		} else if (caller != null && callee != null && !caller.equals(callee)) {
			return true;
		}
		
		context.log("canApply failed");
		return false;
	}

	public JComponent getComponent(InteractionBuilderSettings config) {
		return new HierarchyPanel(config);
	}

	public String getTitle() {
		return "Define which fields to use to create a Hierarchical Interaction Log";
	}

	private class HierarchyPanel extends ProMPropertiesPanel {
		private static final long serialVersionUID = -3780071821898519751L;
		
		private ProMComboBox<String> caller;
		private ProMComboBox<String> callee;
		private InteractionBuilderSettings config;

		private JCheckBox rename;

		private ProMComboBox<String> call;
		
		public HierarchyPanel(InteractionBuilderSettings config) {
			super("Configure Hierarchy");
			this.caller = this.addComboBox("Caller", config.getPossibleCallers());
	        this.callee = this.addComboBox("Callee", config.getPossibleCallees());
	        this.rename = this.addCheckBox("Rename events", false);
	        this.call = this.addComboBox("Call", config.getPossibleCalls());
	        
	        // If the fields 'Caller' and 'Callee' exist in the log, set those as default selection
	        this.caller = setComboBoxValueIfExists(caller, "Caller");
	        this.callee = setComboBoxValueIfExists(callee, "Callee");
	        this.call = setComboBoxValueIfExists(call, "Call");
	        this.call.setEnabled(false);
	        // TODO: add listener that enables the box
	        this.rename.addItemListener(new RenameListener(this.rename, this.call));
	        
	        this.config = config;
		}
		
		private class RenameListener implements ItemListener {
			private JCheckBox check;
			private ProMComboBox<String> call;
			
			public RenameListener(JCheckBox check, ProMComboBox<String> call) {
				this.check = check;
				this.call = call;
			}

			public void itemStateChanged(ItemEvent arg0) {
				call.setEnabled(check.isSelected());
			}
			
		}
		
		private ProMComboBox<String> setComboBoxValueIfExists(ProMComboBox<String> box, String value) {
			// Assume that the list of options is not empty
			// You'd think Java has a ComboBox.getItemList/getItemArray. You'd be wrong...
			for (int i = 0; i < box.getItemCount(); i++ ) {
				if (box.getItemAt(i).equals(value)) {
					box.setSelectedItem(value);
					return box;
				}
			}
			return box;
		}
		
		public InteractionBuilderSettings getConfig() {
			config.setCaller((String) caller.getSelectedItem());
        	config.setCallee((String) callee.getSelectedItem());
        	config.setCall((String) call.getSelectedItem());
        	config.setRename(rename.isSelected());
        	
			return config;
		}
	}
}

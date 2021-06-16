package org.architecturemining.interactionCentric.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;

import org.architecturemining.interactionCentric.models.ParameterSettings;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;

/**
 * Author: Arnout Verhaar
 * email: w.d.verhaar@students.uu.nl
 * 
 * Class for implementing the wizard used by the ParameterSettingsPlugin.
 * @param <ProMCheckBox>
 *  
*/

public class ParameterSettingsWizardBuilder<ProMCheckBox> implements ProMWizardStep<ParameterSettings>{
	public UIPluginContext context;
	public boolean chooseEventType = true;
	
	public ParameterSettingsWizardBuilder(ParameterSettings config) {
		super();
	}

	public ParameterSettings apply(ParameterSettings model, JComponent component) {
		ParameterWizard panel = (ParameterWizard) component;		
		model = panel.getConfig();		
		return model;
	}

	public boolean canApply(ParameterSettings model, JComponent component) {
		ParameterWizard panel = (ParameterWizard) component;
		model = panel.getConfig();		
		
		String caller = model.getCallerTag();
		String callee = model.getCalleeTag();
		String caseField = model.getCaseID();
		String eventType = model.getEventTypeTag();
		return caller != null && callee != null && !caller.equals(callee) && caseField != null && eventType != null;
	}

	public JComponent getComponent(ParameterSettings config) {
		return new ParameterWizard(config);
	}

	public String getTitle() {
		return "Define the fields which provide the necessary information for interaction discovery";
	}

	
	public class ParameterWizard extends ProMPropertiesPanel{
		/**
		 * Generated serial id
		 */
		private static final long serialVersionUID = -9075293113478737296L;
		private ProMComboBox<String> caller;
		private ProMComboBox<String> callee;
		private ProMComboBox<String> caseField;
		private ProMComboBox<String> eventField;
		private ProMCheckBox f;
		private ParameterSettings config;


		public ParameterWizard(ParameterSettings config) {
			super(getTitle());
			this.caseField = this.addComboBox("Tracking ID", config.possibleOptions);
			this.caller = this.addComboBox("Caller", config.possibleOptions);
			this.callee = this.addComboBox("Callee", config.possibleOptions);				
			List<String> optionalOption = new ArrayList<String>(Arrays.asList(config.possibleOptions));
			optionalOption.add(0, "(empty)");
			
			this.eventField = this.addComboBox("Event Type (optional)", optionalOption);			
			this.caller = setComboBoxValueIfExists(caller, "Caller");
	        this.callee = setComboBoxValueIfExists(callee, "Callee");
	        
	        this.config = config;
			
		}
		
		public ParameterSettings getConfig() {
			config.setCallerTag((String) caller.getSelectedItem());
        	config.setCalleeTag((String) callee.getSelectedItem());  
        	config.setCaseID((String) caseField.getSelectedItem());
        	config.setEventTypeTag((String) eventField.getSelectedItem());

			return config;
		}

		private ProMComboBox<String> setComboBoxValueIfExists(ProMComboBox<String> box, String value) {
			// Assume that the list of options is not empty
			for (int i = 0; i < box.getItemCount(); i++ ) {
				if (box.getItemAt(i).equals(value)) {
					box.setSelectedItem(value);
					return box;
				}
			}
			return box;
		}

		
	}
	
}

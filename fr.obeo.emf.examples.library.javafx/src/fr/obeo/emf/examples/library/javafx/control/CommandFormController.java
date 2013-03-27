package fr.obeo.emf.examples.library.javafx.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.util.StringConverter;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.command.SetCommand;

import fr.obeo.emf.examples.library.javafx.model.LibraryManager;

/**
 * Command form controls.
 * 
 */
public class CommandFormController implements ChangeListener<Object> {

	@FXML
	Button setButton;
	@FXML
	TextField setValueField;
	@FXML
	ComboBox<EAttribute> setEAttributeCombo;
	@FXML
	ComboBox<EAttribute> unsetEAttributeCombo;
	@FXML
	Button unsetButton;
	@FXML
	Button complex1Button;
	@FXML
	Button complex2Button;

	/**
	 * Model access
	 */
	private LibraryManager libraryManager;

	/**
	 * Current selection
	 */
	private EObject currentEObject;

	private ObservableList<EAttribute> eAttributeList;

	/**
	 * Initializes the controls with the given model.
	 * 
	 * @param libraryManager
	 *            the library manager
	 */
	public void init(LibraryManager libraryManager) {
		this.libraryManager = libraryManager;

		// Displays EAttributes in a readable way
		StringConverter<EAttribute> stringConverter = new StringConverter<EAttribute>() {

			@Override
			public String toString(EAttribute attribute) {
				return attribute.getName();
			}

			@Override
			public EAttribute fromString(String attributeName) {
				return (EAttribute) currentEObject.eClass()
						.getEStructuralFeature(attributeName);
			}
		};

		eAttributeList = FXCollections.observableArrayList();

		// WORKAROUND bad combo refresh when there is nothing at startup
		EAttribute temp = EcoreFactory.eINSTANCE.createEAttribute();
		temp.setName("-------------------------------------------");
		eAttributeList.add(temp);
		// END WORKAROUND

		setEAttributeCombo.setItems(eAttributeList);
		unsetEAttributeCombo.setItems(eAttributeList);
		setEAttributeCombo.setConverter(stringConverter);
		unsetEAttributeCombo.setConverter(stringConverter);

		complex1Button.setTooltip(new Tooltip(ComplexCommand1.DESCRIPTION));
		complex2Button.setTooltip(new Tooltip(ComplexCommand2.DESCRIPTION));

		setDisable(true);
	}

	private void setDisable(boolean value) {
		setEAttributeCombo.setDisable(value);
		unsetEAttributeCombo.setDisable(value);
		setValueField.setDisable(value);
		setButton.setDisable(value);
		unsetButton.setDisable(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changed(ObservableValue<? extends Object> observable,
			Object oldValue, Object newValue) {
		int setIndex = setEAttributeCombo.getSelectionModel()
				.getSelectedIndex();
		int unsetIndex = unsetEAttributeCombo.getSelectionModel()
				.getSelectedIndex();
		if (newValue instanceof TreeItem<?>) {
			Object value = ((TreeItem<?>) newValue).getValue();
			if (value instanceof EObject) {
				currentEObject = (EObject) value;
				eAttributeList.clear();
				EList<EAttribute> eAllAttributes = currentEObject.eClass()
						.getEAllAttributes();
				eAttributeList.addAll(eAllAttributes);
				if (!eAllAttributes.isEmpty()) {
					setDisable(false);
					setEAttributeCombo.getSelectionModel().select(setIndex);
					unsetEAttributeCombo.getSelectionModel().select(unsetIndex);
				}
				return;
			}
		}
		currentEObject = null;
		setDisable(true);
	}

	/**
	 * Set, based on selection.
	 */
	@FXML
	public void clickedSet() {
		EAttribute attribute = setEAttributeCombo.getValue();
		if (attribute != null) {
			String valueAsString = setValueField.getText();
			Object value = EcoreUtil.createFromString(attribute.getEAttributeType(), valueAsString);
			libraryManager
					.getEditingDomain()
					.getCommandStack()
					.execute(
							new SetCommand(libraryManager.getEditingDomain(),
									currentEObject, attribute, value));
		}
	}

	/**
	 * Unset, based on selection.
	 */
	@FXML
	public void clickedUnset() {
		EAttribute attribute = unsetEAttributeCombo.getValue();
		if (attribute != null) {
			libraryManager
					.getEditingDomain()
					.getCommandStack()
					.execute(
							new SetCommand(libraryManager.getEditingDomain(),
									currentEObject, attribute, attribute
											.getDefaultValue()));
		}
	}

	/**
	 * Complex command 1.
	 */
	@FXML
	public void clickedCC1() {
		ChangeCommand command = new ComplexCommand1(
				libraryManager.getRootLibrary(),
				libraryManager.getChangeRecorder());
		libraryManager.getEditingDomain().getCommandStack().execute(command);
	}

	/**
	 * Complex command 2.
	 */
	@FXML
	public void clickedCC2() {
		ChangeCommand command = new ComplexCommand2(
				libraryManager.getRootLibrary(),
				libraryManager.getChangeRecorder());
		libraryManager.getEditingDomain().getCommandStack().execute(command);
	}

}

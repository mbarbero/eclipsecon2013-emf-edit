package fr.obeo.emf.examples.library.javafx.views;

import java.util.EventObject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

import at.bestsolution.efxclipse.runtime.emf.edit.ui.AdapterFactoryTreeCellFactory;
import at.bestsolution.efxclipse.runtime.emf.edit.ui.AdapterFactoryTreeItem;
import fr.obeo.emf.examples.library.javafx.model.LibraryManager;

/**
 * The main tree view.
 * 
 */
public class LibraryView implements CommandStackListener {
	private TreeView<Object> treeView;

	private VBox root;


	private ObservableList<String> properties;

	public LibraryView(final LibraryManager libraryManager) {
		libraryManager.getEditingDomain().getCommandStack()
				.addCommandStackListener(this);

		treeView = new TreeView<Object>();
		TreeItem<Object> rootItem = new AdapterFactoryTreeItem(
				libraryManager.getRootLibrary(), treeView,
				libraryManager.getAdapterFactory());

		treeView.setMinWidth(300);
		treeView.setRoot(rootItem);

		// connect to EMF.edit providers
		AdapterFactoryTreeCellFactory treeCellFactory = new AdapterFactoryTreeCellFactory(
				libraryManager.getAdapterFactory());
		treeView.setCellFactory(treeCellFactory);
		rootItem.setExpanded(true);
		treeView.setEditable(false);
		root = new VBox();
		root.getChildren().add(treeView);
		VBox.setVgrow(treeView, Priority.ALWAYS);

		/*
		 * Properties
		 */
		ListView<String> propertiesList = new ListView<String>();
		properties = FXCollections.observableArrayList();
		propertiesList.setItems(properties);

		treeView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Object>() {
					/**
					 * {@inheritDoc}
					 */
					@Override
					public void changed(
							ObservableValue<? extends Object> observable,
							Object oldValue, Object newValue) {
						updateProperties();
					}

				});

		TitledPane properties = new TitledPane("Properties", propertiesList);
		properties.setExpanded(false);
		properties.setAlignment(Pos.TOP_LEFT);
		properties.setMaxHeight(200);
		root.getChildren().add(properties);
	}

	private void updateProperties() {
		properties.clear();
		TreeItem<Object> selectedItem = treeView.getSelectionModel()
				.getSelectedItem();
		if (selectedItem != null) {
			Object itemValue = selectedItem.getValue();
			if (itemValue instanceof EObject) {
				EObject element = (EObject) itemValue;
				for (EAttribute attr : element.eClass().getEAllAttributes()) {
					properties.add(attr.getName() + ": " + element.eGet(attr));
				}
				return;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commandStackChanged(EventObject event) {
		updateProperties();
	}

	public TreeView<Object> getTreeView() {
		return treeView;
	}

	public Pane getRootPane() {
		return root;
	}

}

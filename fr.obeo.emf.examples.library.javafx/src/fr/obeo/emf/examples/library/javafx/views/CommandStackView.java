package fr.obeo.emf.examples.library.javafx.views;

import java.util.EventObject;
import java.util.LinkedList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

import com.google.common.collect.Lists;

import fr.obeo.emf.examples.library.javafx.Demo;
import fr.obeo.emf.examples.library.javafx.model.LibraryManager.CommandStackImpl;
import fr.obeo.emf.examples.library.javafx.views.utils.CommandListCellFactory;

/**
 * Displays the command stack content, allows to undo/redo commands.
 * 
 */
public class CommandStackView implements CommandStackListener {

	private CommandStackImpl commandStack;

	private ListView<Command> listView;

	private Button undo;
	private Button redo;
	private BorderPane root;

	public CommandStackView(AdapterFactoryEditingDomain editingDomain) {
		this.commandStack = (CommandStackImpl) editingDomain.getCommandStack();
		commandStack.addCommandStackListener(this);

		/*
		 * List
		 */
		listView = new ListView<Command>();
		listView.setMinWidth(300);
		listView.setCellFactory(new CommandListCellFactory(editingDomain
				.getAdapterFactory(), editingDomain.getCommandStack()));
		listView.setEditable(false);

		/*
		 * Buttons
		 */
		Image undoImage = new Image(getClass().getResourceAsStream("undo.gif"));
		undo = new Button("Undo", new ImageView(undoImage));
		undo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				commandStack.undo();
			}
		});

		Image redoImage = new Image(getClass().getResourceAsStream("redo.gif"));
		redo = new Button("Redo", new ImageView(redoImage));
		redo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				commandStack.redo();
			}
		});
		refreshButtons();

		/*
		 * Layout
		 */
		VBox top = new VBox();
		Label label = new Label("Command stack");
		label.idProperty().set(Demo.TITLE_STYLE_PROPERTY);
		top.getChildren().add(label);
		top.setAlignment(Pos.CENTER);

		HBox buttons = new HBox();
		buttons.setPadding(new Insets(15, 12, 15, 0));
		buttons.setSpacing(10);
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().add(undo);
		buttons.getChildren().add(redo);

		top.getChildren().add(buttons);

		root = new BorderPane();
		root.setTop(top);
		root.setCenter(listView);
	}

	private void refreshButtons() {
		undo.setDisable(!commandStack.canUndo());
		redo.setDisable(!commandStack.canRedo());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commandStackChanged(EventObject event) {
		refreshButtons();
		listView.getItems().clear();
		listView.getItems().addAll(Lists.reverse((commandStack.getCommandList())));
	}

	public BorderPane getRootPane() {
		return root;
	}

}

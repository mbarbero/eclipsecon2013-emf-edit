package fr.obeo.emf.examples.library.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

import fr.obeo.emf.examples.library.javafx.control.CommandFormController;
import fr.obeo.emf.examples.library.javafx.model.LibraryManager;
import fr.obeo.emf.examples.library.javafx.views.CommandStackView;
import fr.obeo.emf.examples.library.javafx.views.LibraryView;

/**
 * The Demo application.
 * 
 */
public class Demo extends Application {
	public static final String TITLE_STYLE_PROPERTY = "title";
	private static final String APP_TITLE = "EMF.edit demo";

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Model initialization
		LibraryManager libraryManager = new LibraryManager();

		// The command list
		CommandStackView commandStackView = new CommandStackView(
				(AdapterFactoryEditingDomain) libraryManager.getEditingDomain());

		// The tree view
		LibraryView libraryView = new LibraryView(libraryManager);

		// The command form
		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent commandForm = (Parent) fxmlLoader.load(getClass().getResource(
				"views/CommandForm.fxml").openStream());
		CommandFormController commandFormController = (CommandFormController) fxmlLoader
				.getController();
		commandFormController.init(libraryManager);
		libraryView.getTreeView().getSelectionModel().selectedItemProperty()
				.addListener(commandFormController);

		// App layout
		HBox rootPane = new HBox();
		rootPane.setSpacing(15);

		rootPane.getChildren().add(commandStackView.getRootPane());	
		rootPane.getChildren().add(libraryView.getRootPane());
		rootPane.getChildren().add(commandForm);

		HBox.setHgrow(commandStackView.getRootPane(), Priority.ALWAYS);
		HBox.setHgrow(libraryView.getRootPane(), Priority.ALWAYS);
		HBox.setHgrow(commandForm, Priority.NEVER);

		primaryStage.setTitle(APP_TITLE);
		Scene scene = new Scene(rootPane);
		scene.getStylesheets().add(
				getClass().getResource("stylesheet.css").toExternalForm());
		primaryStage.setScene(scene);

		primaryStage.show();
	}
}
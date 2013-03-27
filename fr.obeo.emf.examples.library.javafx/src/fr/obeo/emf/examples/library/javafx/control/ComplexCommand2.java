package fr.obeo.emf.examples.library.javafx.control;

import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.examples.extlibrary.Library;
import org.eclipse.emf.examples.extlibrary.Writer;

public class ComplexCommand2 extends ChangeCommand {
	public static final String DESCRIPTION = "Set every writer last name to upper case";
	private Library library;

	public ComplexCommand2(Library library, ChangeRecorder changeRecorder) {
		super(changeRecorder, library);
		this.library = library;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		for (Writer writer : library.getWriters()) {
			String lastName = writer.getLastName();
			if (lastName != null) {
				writer.setLastName(lastName.toUpperCase());
			}
		}
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
}

package fr.obeo.emf.examples.library.javafx.control;

import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.examples.extlibrary.Book;
import org.eclipse.emf.examples.extlibrary.EXTLibraryFactory;
import org.eclipse.emf.examples.extlibrary.Library;
import org.eclipse.emf.examples.extlibrary.Writer;

public class ComplexCommand1 extends ChangeCommand {
	public static final String DESCRIPTION = "Addition of The Lord of the Rings trilogy";
	private Library library;

	public ComplexCommand1(Library library, ChangeRecorder changeRecorder) {
		super(changeRecorder, library);
		this.library = library;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		Writer newWriter = EXTLibraryFactory.eINSTANCE.createWriter();
		newWriter.setFirstName("John Ronald Reuel");
		newWriter.setLastName("Tolkien");
		library.getWriters().add(newWriter);

		Book newBook1 = EXTLibraryFactory.eINSTANCE.createBook();
		newBook1.setTitle("The Fellowship of the Ring");
		newBook1.setAuthor(newWriter);
		library.getBooks().add(newBook1);

		Book newBook2 = EXTLibraryFactory.eINSTANCE.createBook();
		newBook2.setTitle("The Two Towers");
		newBook2.setAuthor(newWriter);
		library.getBooks().add(newBook2);

		Book newBook3 = EXTLibraryFactory.eINSTANCE.createBook();
		newBook3.setTitle("The Return of the King");
		newBook3.setAuthor(newWriter);
		library.getBooks().add(newBook3);
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
}

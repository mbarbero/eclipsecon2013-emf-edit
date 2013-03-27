package fr.obeo.emf.examples.library.javafx.model;

import java.util.List;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.examples.extlibrary.Book;
import org.eclipse.emf.examples.extlibrary.BookCategory;
import org.eclipse.emf.examples.extlibrary.EXTLibraryFactory;
import org.eclipse.emf.examples.extlibrary.Library;
import org.eclipse.emf.examples.extlibrary.Writer;
import org.eclipse.emf.examples.extlibrary.provider.EXTLibraryItemProviderAdapterFactory;

/**
 * The application model.
 * 
 */
public class LibraryManager {

	public static final class CommandStackImpl extends BasicCommandStack {
		public List<Command> getCommandList() {
			return commandList;
		}

		public int getTop() {
			return top;
		}
	}

	private Library library;

	private EditingDomain editingDomain;

	private ComposedAdapterFactory adapterFactory;

	private ChangeRecorder changeRecorder;

	public LibraryManager() {
		adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory
				.addAdapterFactory(new EXTLibraryItemProviderAdapterFactory());
		BasicCommandStack commandStack = new CommandStackImpl();
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory,
				commandStack);
		changeRecorder = new ChangeRecorder();
		init();
	}

	public ChangeRecorder getChangeRecorder() {
		return changeRecorder;
	}

	public AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	public Library getRootLibrary() {
		return library;
	}

	/**
	 * Initializes the library.
	 */
	protected void init() {
		library = EXTLibraryFactory.eINSTANCE.createLibrary();
		library.setName("My Library");

		Writer writer = EXTLibraryFactory.eINSTANCE.createWriter();
		writer.setFirstName("Victor");
		writer.setLastName("Hugo");
		library.getWriters().add(writer);

		Book book = EXTLibraryFactory.eINSTANCE.createBook();
		book.setTitle("Les Misérables");
		book.setCategory(BookCategory.BIOGRAPHY_LITERAL);
		book.setPages(100);
		library.getBooks().add(book);
	}

}

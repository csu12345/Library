package library.tests;

import java.util.Map;
import library.daos.BookHelper;
import library.daos.BookMapDAO;
import library.interfaces.entities.IBook;
import org.junit.Test;
import static org.junit.Assert.*;
import org.powermock.reflect.Whitebox;

public class BookMapDAOTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testInitWithoutHelper() {
        BookMapDAO bookMapDao = new BookMapDAO(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddBookWithoutDetails() {
        BookMapDAO bookMapDAO = new BookMapDAO(new BookHelper());
        bookMapDAO.addBook(null, null, null);
    }
    
    @Test
    public void testAddBookWithDetails() {
        BookMapDAO bookMapDAO = new BookMapDAO(new BookHelper());
        IBook book = bookMapDAO.addBook("author1", "title1", "callNo1");
        Map<Integer, IBook> bookMap = Whitebox.getInternalState(bookMapDAO, "bookMap");
        assertTrue(bookMap.containsValue(book));
        assertTrue(book instanceof IBook);
    }
    
}
package library.tests;

import library.daos.BookHelper;
import library.daos.BookMapDAO;
import library.entities.Book;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertTrue(book instanceof IBook);
    }
}
package library.tests;

import library.daos.LoanHelper;
import library.daos.LoanMapDAO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import library.entities.Book;
import library.entities.Member;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import static org.hamcrest.CoreMatchers.is;

public class BookTest {
    
    public BookTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookDetailEmpty() {
        Book book = new Book("", "", "", 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBookIDInvalid() {
        Book book = new Book("author1", "title1", "callNo1", -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBorrowWithoutLoan() {
        Book book = new Book("author1", "title1", "callNo1", 1);
        book.borrow(null);
    }
    
    @Test(expected = RuntimeException.class)
    public void testBookStateNotAvailable() {
        Book book = new Book("author1", "title1", "callNo1", 1);
        book.setState(EBookState.ON_LOAN);
        IMember borrower = new Member("fName0", "1Name0", "0001", "email0", 1);
        ILoanDAO loanDAO = new LoanMapDAO(new LoanHelper());
        ILoan loan = loanDAO.createLoan(borrower, book);
        book.borrow(loan);
    }
    
    @Test
    public void testBorrowWithLoan() {
        Book book = new Book("author1", "title1", "callNo1", 1);
        IMember borrower = new Member("fName0", "1Name0", "0001", "email0", 1);
        ILoanDAO loanDAO = new LoanMapDAO(new LoanHelper());
        ILoan loan = loanDAO.createLoan(borrower, book);
        book.borrow(loan);
        assertThat(book.getLoan(), is(equals(loan)));
        //assertThat(book.getState(), is(equals(EBookState.AVAILABLE)));
        System.out.println(book.getState());
    }
}
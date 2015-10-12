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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Book.class)
public class BookTest {
    
    @Mock(name = "state")
    EBookState stateMock;
    
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
        PowerMockito.when(stateMock).thenReturn(EBookState.ON_LOAN);
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
        System.out.println("Book State" + book.getState());
        System.out.println("Loan" + book.getLoan());
        assertEquals("Loan Assert Fails", loan, book.getLoan());
        assertEquals("Borrow book state check fails", EBookState.ON_LOAN, book.getState());
    }
}
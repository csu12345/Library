package library.tests.integration;

import library.BorrowUC_CTL;
import library.daos.BookHelper;
import library.daos.BookMapDAO;
import library.daos.LoanHelper;
import library.daos.LoanMapDAO;
import library.daos.MemberHelper;
import library.daos.MemberMapDAO;
import library.hardware.CardReader;
import library.hardware.Display;
import library.hardware.Printer;
import library.hardware.Scanner;
import library.interfaces.EBorrowState;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class BorrowInitializeTest {
    
    private BorrowUC_CTL ctl;
    
    @Before
    public void onSetUp() {
        // create hardware entities
        ICardReader cardReader = new CardReader();
        IScanner scanner = new Scanner();
        IPrinter printer = new Printer();
        IDisplay display = new Display();
        // create daos
        IBookDAO bookDAO = new BookMapDAO(new BookHelper());
        IMemberDAO memberDAO = new MemberMapDAO(new MemberHelper());
        ILoanDAO loanDAO = new LoanMapDAO(new LoanHelper());
        // create ctl 
        ctl = new BorrowUC_CTL(cardReader, scanner, printer, display, bookDAO, loanDAO, memberDAO);
    } 
    
    @Test
    public void testBorrowInitialization() {
        ctl.initialise();
        assertTrue(Whitebox.getInternalState(ctl, "state").equals(EBorrowState.INITIALIZED));
    }
}
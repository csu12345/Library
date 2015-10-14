package library.tests.uat;

import java.awt.Frame;
import javax.swing.JPanel;
import library.BorrowUC_CTL;
import library.Main;
import library.hardware.CardReader;
import library.hardware.Scanner;
import library.interfaces.EBorrowState;
import library.panels.MainPanel;
import library.panels.SwipeCardPanel;
import org.assertj.swing.core.BasicComponentFinder;
import org.assertj.swing.core.ComponentFinder;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.launcher.ApplicationLauncher;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.powermock.reflect.Whitebox;



public class BorrowInitializeTest extends AssertJSwingJUnitTestCase {

    private FrameFixture cardReaderWindow;
    private FrameFixture scannerWindow;
    private JPanelFixture mainWindow;
    private ComponentFinder finder;
    
    /**
     * Setup preconditions
     */
    @Override
    protected void onSetUp() {
        // check if BorrowBookCTL class exists
        assertTrue(this.isClass("library.BorrowUC_CTL"));
        ApplicationLauncher.application(Main.class).start();
        // check book, loan and member dao exists
        assertTrue(this.isClass("library.daos.BookMapDAO"));
        assertTrue(this.isClass("library.daos.LoanMapDAO"));
        assertTrue(this.isClass("library.daos.MemberMapDAO"));
        // check hardware entities exists
        assertTrue(this.isClass("library.hardware.CardReader"));
        assertTrue(this.isClass("library.hardware.Scanner"));
        // check BorrowBookCTL is added as listener to cardReader and scanner
        finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
        mainWindow = new JPanelFixture(robot(), (MainPanel) finder.findByType(MainPanel.class));
        cardReaderWindow = new FrameFixture(robot(), (Frame) finder.findByType(CardReader.class));
        assertTrue(Whitebox.getInternalState(cardReaderWindow.target(), "listener") instanceof BorrowUC_CTL);
        scannerWindow = new FrameFixture(robot(), (Frame) finder.findByType(Scanner.class));
        assertTrue(Whitebox.getInternalState(scannerWindow.target(), "listener") instanceof BorrowUC_CTL);
        // BorrowBookCTL state == CREATED
        assertEquals(Whitebox.getInternalState(Whitebox.getInternalState(scannerWindow.target(), "listener"), "state"), EBorrowState.CREATED);
    }
    
    @Test
    public void testBorrowInitialize() {
        // BorrowBookUI is displayed
        mainWindow.requireVisible();
        // click on borrow button
        mainWindow.button(JButtonMatcher.withText("Borrow Books")).click();
        // SwipeCard panel of BorrowBookUI is displayed
        JPanelFixture swipeCardPanel = new JPanelFixture(robot(), (JPanel) finder.findByType(SwipeCardPanel.class));
        swipeCardPanel.requireVisible();
        // cancel button enabled
        swipeCardPanel.button(JButtonMatcher.withText("Cancel")).requireEnabled();
        // card reader enabled
        cardReaderWindow.button().requireEnabled();
        cardReaderWindow.textBox().requireEditable();
        // scanner is disabled
        scannerWindow.button().requireDisabled();
        scannerWindow.textBox().requireNotEditable();
        // BorrowBookCTL state == INITIALIZED
        assertEquals(Whitebox.getInternalState(Whitebox.getInternalState(scannerWindow.target(), "listener"), "state"), EBorrowState.INITIALIZED);
    }
    
    /**
     * Check if class exists
     * 
     * @param className
     * @return boolean
     */
    protected boolean isClass(String className) {
        try  {
            Class.forName(className);
            return true;
        }  catch (final ClassNotFoundException e) {
            return false;
        }
    }
}
package library.tests.uat;

import java.awt.Frame;
import javax.swing.JPanel;
import library.BorrowUC_CTL;
import library.Main;
import library.hardware.CardReader;
import library.hardware.Scanner;
import library.interfaces.EBorrowState;
import library.panels.MainPanel;
import library.panels.RestrictedPanel;
import library.panels.ScanningPanel;
import org.assertj.swing.core.BasicComponentFinder;
import org.assertj.swing.core.ComponentFinder;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.launcher.ApplicationLauncher;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class SwipeCardTest extends AssertJSwingJUnitTestCase {
    
    private FrameFixture cardReaderWindow;
    private JPanelFixture mainWindow;
    private ComponentFinder finder;
    
    /**
     * Setup preconditions
     */
    @Override
    protected void onSetUp() {
        ApplicationLauncher.application(Main.class).start();
        finder = BasicComponentFinder.finderWithNewAwtHierarchy();
        mainWindow = new JPanelFixture(robot(), (MainPanel) finder.findByType(MainPanel.class));
        mainWindow.button(JButtonMatcher.withText("Borrow Books")).click();
        // card reader visible and enabled
        cardReaderWindow = new FrameFixture(robot(), (Frame) finder.findByType(CardReader.class));
        cardReaderWindow.requireVisible();
        cardReaderWindow.requireEnabled();
        // BorrowBookCTL added as listener to cardReader
        assertTrue(Whitebox.getInternalState(cardReaderWindow.target(), "listener") instanceof BorrowUC_CTL);
        // BorrowBookCTL state == INITIALIZED
        assertEquals(Whitebox.getInternalState(Whitebox.getInternalState(cardReaderWindow.target(), "listener"), "state"), EBorrowState.INITIALIZED);
        // memberDAO exists
        assertNotNull(Whitebox.getInternalState(Whitebox.getInternalState(cardReaderWindow.target(), "listener"), "memberDAO"));
    }
    
    @Test
    public void testSCMemberNotRestricted() {
        String memberId = "0006";
        String memberName = "fName5 lName5";
        cardReaderWindow.textBox().enterText(memberId);
        cardReaderWindow.button().click();
        // scanning panel is displayed
        JPanelFixture scanningPanel = new JPanelFixture(robot(), (JPanel) finder.findByType(ScanningPanel.class));
        scanningPanel.requireVisible();
        // complete and cancel buttons are enabled
        scanningPanel.button(JButtonMatcher.withText("Completed")).requireEnabled();
        scanningPanel.button(JButtonMatcher.withText("Cancel")).requireEnabled();
        // card reader disabled
        cardReaderWindow.button().requireDisabled();
        cardReaderWindow.textBox().requireNotEditable();
        // scanner is enabled
        FrameFixture scanBookWindow = new FrameFixture(robot(), (Scanner) finder.findByType(Scanner.class));
        scanBookWindow.requireVisible();
        scanBookWindow.requireEnabled();
        // borrower details are displayed
        scanningPanel.label(JLabelMatcher.withName("lblBorrowerName")).requireText(Integer.parseInt(memberId) + " " + memberName);
        scanningPanel.label(JLabelMatcher.withName("lblBorrowerContact")).requireText(memberId);
        // existing loans displayed
        JPanelFixture existingLoansPanel = scanningPanel.panel(new GenericTypeMatcher<JPanel>(JPanel.class) {
            @Override
            protected boolean isMatching(JPanel panel) {
              return "existingLoansPanel".equals(panel.getName());
            }
        });
        existingLoansPanel.requireVisible();
        // BorrowBookCTL state == SCANNING_BOOKS
        assertEquals(Whitebox.getInternalState(Whitebox.getInternalState(scanBookWindow.target(), "listener"), "state"), EBorrowState.SCANNING_BOOKS);        
    }
    
    @Test
    public void testSCMemberWithRestriction() {
        String memberId = "0002";
        String memberName = "fName1 lName1";
        cardReaderWindow.textBox().enterText(memberId);
        cardReaderWindow.button().click();
        // restricted panel of Borrow UI is displayed
        JPanelFixture restrictedPanel = new JPanelFixture(robot(), (JPanel) finder.findByType(RestrictedPanel.class));
        JPanelFixture borrowRestrictionsPanel = restrictedPanel.panel(new GenericTypeMatcher<JPanel>(JPanel.class) {
            @Override
            protected boolean isMatching(JPanel panel) {
              return "borrowRestrictionsPanel".equals(panel.getName());
            }
        });
        borrowRestrictionsPanel.requireVisible();
        // complete button disabled and cancel enabled
        restrictedPanel.button(JButtonMatcher.withText("Completed")).requireDisabled();
        restrictedPanel.button(JButtonMatcher.withText("Cancel")).requireEnabled();
        // card reader disabled
        cardReaderWindow.button().requireDisabled();
        cardReaderWindow.textBox().requireNotEditable();
        // scanner is disabled
        FrameFixture scanBookWindow = new FrameFixture(robot(), (Scanner) finder.findByType(Scanner.class));
        scanBookWindow.button().requireDisabled();
        scanBookWindow.textBox().requireNotEditable();
        // borrower details are displayed
        restrictedPanel.label(JLabelMatcher.withName("lblBorrowerName")).requireText(Integer.parseInt(memberId) + " " + memberName);
        restrictedPanel.label(JLabelMatcher.withName("lblBorrowerContact")).requireText(memberId);
        // existing loans displayed
        JPanelFixture existingLoansPanel = restrictedPanel.panel(new GenericTypeMatcher<JPanel>(JPanel.class) {
            @Override
            protected boolean isMatching(JPanel panel) {
              return "existingLoansPanel".equals(panel.getName());
            }
        });
        existingLoansPanel.requireVisible();
        // overdue message displayed if relevant
        borrowRestrictionsPanel.label(JLabelMatcher.withName("lblOverdue")).requireText("Borrower has overdue loans");
        restrictedPanel.label(JLabelMatcher.withName("lblErrMesg")).requireText("Member cannot borrow at this time.");
        // BorrowBookCTL state == BORROWING_RESTRICTED
        assertEquals(Whitebox.getInternalState(Whitebox.getInternalState(cardReaderWindow.target(), "listener"), "state"), EBorrowState.BORROWING_RESTRICTED);        
        System.out.println("Test");
    }
    
}
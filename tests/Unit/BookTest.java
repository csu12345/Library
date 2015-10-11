+package library.tests.unit;
+
+import java.util.logging.Level;
+import java.util.logging.Logger;
+import library.daos.LoanHelper;
+import library.daos.LoanMapDAO;
+import library.entities.Book;
+import library.entities.Member;
+import library.interfaces.daos.ILoanDAO;
+import library.interfaces.entities.EBookState;
+import library.interfaces.entities.ILoan;
+import library.interfaces.entities.IMember;
+import static org.junit.Assert.assertEquals;
+import org.junit.Test;
+import org.junit.runner.RunWith;
+import org.powermock.api.support.membermodification.MemberModifier;
+import org.powermock.core.classloader.annotations.PrepareForTest;
+import org.powermock.modules.junit4.PowerMockRunner;
+
+@RunWith(PowerMockRunner.class)
+@PrepareForTest(Book.class)
+public class BookTest {
+    
+    @Test(expected = IllegalArgumentException.class)
+    public void testBookDetailEmpty() {
+        Book book = new Book("", "", "", 1);
+    }
+    
+    @Test(expected = IllegalArgumentException.class)
+    public void testBookIDInvalid() {
+        Book book = new Book("author1", "title1", "callNo1", -1);
+    }
+    
+    @Test(expected = IllegalArgumentException.class)
+    public void testBorrowWithoutLoan() {
+        Book book = new Book("author1", "title1", "callNo1", 1);
+        book.borrow(null);
+    }
+    
+    @Test(expected = RuntimeException.class)
+    public void testBookNotAvailable() {
+        Book book = new Book("author1", "title1", "callNo1", 1);
+        try {
+            // Mock the private state variable
+            MemberModifier.field(Book.class, "state").set(book, EBookState.ON_LOAN);
+            IMember borrower = new Member("fName0", "1Name0", "0001", "email0", 1);
+            ILoanDAO loanDAO = new LoanMapDAO(new LoanHelper());
+            ILoan loan = loanDAO.createLoan(borrower, book);
+            book.borrow(loan);
+        } catch (IllegalArgumentException | IllegalAccessException ex) {
+            Logger.getLogger(BookTest.class.getName()).log(Level.SEVERE, null, ex);
+        } 
+    }
+    
+    @Test
+    public void testBorrowWithLoan() {
+        Book book = new Book("author1", "title1", "callNo1", 1);
+        IMember borrower = new Member("fName0", "1Name0", "0001", "email0", 1);
+        ILoanDAO loanDAO = new LoanMapDAO(new LoanHelper());
+        ILoan loan = loanDAO.createLoan(borrower, book);
+        book.borrow(loan);
+        assertEquals(loan, book.getLoan());
+        assertEquals(EBookState.ON_LOAN, book.getState());
+    }
+}
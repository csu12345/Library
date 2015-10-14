+package library.tests.unit;
+
+import library.daos.BookHelper;
+import library.interfaces.daos.IBookHelper;
+import library.interfaces.entities.IBook;
+import static org.junit.Assert.assertTrue;
+import org.junit.Test;
+
+public class BookHelperTest {
+   
+    @Test
+    public void testMakeBook() {
+        IBookHelper bookHelper = new BookHelper();
+        IBook book = bookHelper.makeBook("author1", "title1", "callNo1", 1);
+        assertTrue(book instanceof IBook);
+    }
+}
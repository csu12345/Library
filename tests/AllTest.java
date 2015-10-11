+package library.tests;
+
+import org.junit.runner.RunWith;
+import org.junit.runners.Suite;
+import org.junit.runners.Suite.SuiteClasses;
+
+@RunWith(Suite.class)
+@SuiteClasses({BookHelperTest.class, BookMapDAOTest.class, BookTest.class})
+public class AllTests {
+
+} 
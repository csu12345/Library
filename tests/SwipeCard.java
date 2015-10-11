package library.tests;
 
+import library.Main;
+import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
+import org.junit.Before;
+import org.junit.Test;
+import static org.assertj.swing.launcher.ApplicationLauncher.application;
+import static org.assertj.swing.fixture.JPanelFixture;
+
 // Class for testing the swipe card feature
-public class SwipeCardTest {
+public class SwipeCardTest extends AssertJSwingJUnitTestCase {
+    
+    @Override
+    protected void onSetUp() {
+        application(Main.class).start();
+    }
+    
+    @Test
+    public void testSCMemberNotRestricted() {
+        
+    }
     
 }
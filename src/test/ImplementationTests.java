package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.CheckAsserts;

/**
 * Runs all the implementation test files.
 */

@RunWith(Suite.class)
@SuiteClasses({ CampusTest.class, CheckAsserts.class, ConnectionTest.class, GenericsTest.class,
				GraphTest.class, LoadGraphTest.class, LocationTest.class, NodeTest.class,
				PointTest.class })
public final class ImplementationTests
{
	/**
	    * Checks that assertions are enabled. If they are not, an error 
	    * message is printed, and the system exits.
	    */
		public static void checkAssertsEnabled() {
			try {
				assert false;

				// assertions are not enabled
				System.err.println("Java Asserts are not currently enabled.");
				System.exit(1);

			} catch (AssertionError e) {
				// do nothing
				// assertions are enabled
			}
		}
}


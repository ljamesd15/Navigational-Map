package test.specTest1;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Runs all the specification test files in this package.
 **/
@RunWith(Suite.class)
@SuiteClasses({ ScriptFileTests.class })

public final class SpecificationTests {
	/**
	 * Checks that assertions are enabled. If they are not, an error message is
	 * printed, and the system exits.
	 */
	public static void checkAssertsEnabled() {
		try {
			assert false;

			// assertions are not enabled
			System.err.println(
					"Java Asserts are not currently enabled.");
			System.exit(1);

		} catch (AssertionError e) {
			// do nothing
			// assertions are enabled
		}
	}
}

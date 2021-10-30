package cz.juzna.intellij.neon.parser;

import org.junit.Assert;
import org.junit.Test;

public class IndentMatcherTest {

	public IndentMatcherTest() {}

	@Test
	public void testIndent() {
		NeonParserUtil.IndentMatcher matcher = createMatcher();
		matcher.addIfAbsent("  ");

		Assert.assertSame(1, matcher.getIndents().size());
		Assert.assertTrue(matcher.match(""));
		Assert.assertTrue(matcher.match("  "));
		Assert.assertFalse(matcher.match("    "));
	}

	private static NeonParserUtil.IndentMatcher createMatcher() {
		return new NeonParserUtil.IndentMatcher();
	}

}

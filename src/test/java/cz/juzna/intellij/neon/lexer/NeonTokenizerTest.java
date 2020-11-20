package cz.juzna.intellij.neon.lexer;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class NeonTokenizerTest {

	@Test
	public void testParse() {
		ArrayList<Pair<IElementType,String>> expected = new ArrayList<>();
		expected.add(new Pair<>(NeonTokenTypes.NEON_LITERAL, "key"));
		expected.add(new Pair<>(NeonTokenTypes.NEON_SYMBOL, ":"));
		expected.add(new Pair<>(NeonTokenTypes.NEON_WHITESPACE, " "));
		expected.add(new Pair<>(NeonTokenTypes.NEON_STRING, "'value'"));

		Assert.assertEquals(NeonTokenizer.parse("key: 'value'"), expected);
	}

}

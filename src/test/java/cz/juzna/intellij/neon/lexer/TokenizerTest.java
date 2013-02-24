package cz.juzna.intellij.neon.lexer;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.psi.tree.IElementType;
import com.intellij.testFramework.UsefulTestCase;
import com.sun.tools.javac.util.Pair;
import org.jetbrains.annotations.NonNls;
import org.junit.Test;
import cz.juzna.intellij.neon.lexer.NeonTokenizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cz.juzna.intellij.neon.lexer.NeonTokenTypes.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;


/**
 *
 */
public class TokenizerTest extends UsefulTestCase {

	@Test
	public void testParse() {
		ArrayList<IElementType> expected = new ArrayList<IElementType>();
		expected.add(NeonTokenTypes.NEON_STRING);

		assertOrderedEquals(NeonTokenizer.parse(""), expected);
	}

}

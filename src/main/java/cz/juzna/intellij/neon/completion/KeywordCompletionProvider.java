package cz.juzna.intellij.neon.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.PhpDocPropertyImpl;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonReference;
import cz.juzna.intellij.neon.psi.NeonScalarValue;
import cz.juzna.intellij.neon.psi.NeonValue;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jetbrains.annotations.NotNull;
import org.picocontainer.defaults.ComponentParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Complete keywords
 */
public class KeywordCompletionProvider extends CompletionProvider<CompletionParameters> {
	private static final String[] KEYWORDS = {
		// common
		"true", "false", "yes", "no", "null"
	};

	private static final String[] KNOWN_KEYS = {
		// sections
		"common", "production", "development", "test",

		// extensions
		"parameters", "nette", "services", "factories", "php"
	};


	// CompletionResultSet wants list of LookupElements
	private List<LookupElementBuilder> KEYWORD_LOOKUPS = new ArrayList();
	private List<LookupElementBuilder> KNOWN_KEYS_LOOKUPS = new ArrayList();


	public KeywordCompletionProvider() {
		super();
		for(String keyword: KEYWORDS) KEYWORD_LOOKUPS.add(LookupElementBuilder.create(keyword));
		for(String keyword: KNOWN_KEYS) KNOWN_KEYS_LOOKUPS.add(LookupElementBuilder.create(keyword));
	}

	@Override
	protected void addCompletions(@NotNull CompletionParameters params,
	                              ProcessingContext ctx,
	                              @NotNull CompletionResultSet results) {

		PsiElement curr = params.getPosition().getOriginalElement();

		if (curr.getParent() instanceof NeonKey) {
			for(LookupElementBuilder x: KNOWN_KEYS_LOOKUPS) results.addElement(x);
		}
		else if (curr.getParent() instanceof NeonScalarValue) {
			for(LookupElementBuilder x: KEYWORD_LOOKUPS) results.addElement(x);
		}
		else if (curr.getParent() instanceof NeonReference) {
			// Services from SystemContainer class
			PhpClass container = PhpIndex.getInstance(curr.getProject()).getClassByName("SystemContainer");
			if (container != null) {
				for (Field field : container.getFields()) {
					if (field instanceof PhpDocPropertyImpl) {
						results.addElement( LookupElementBuilder.create(field.getName()) );
					}
				}
			}
		}
		// TODO: more

	}

}

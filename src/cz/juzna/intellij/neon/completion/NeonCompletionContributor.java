package cz.juzna.intellij.neon.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import cz.juzna.intellij.neon.completion.providers.*;

/**
 * Provides code completion
 */
public class NeonCompletionContributor extends CompletionContributor {
	public NeonCompletionContributor() {
		extend(CompletionType.BASIC, StandardPatterns.instanceOf(PsiElement.class), new MethodCompletionProvider());
		extend(CompletionType.BASIC, StandardPatterns.instanceOf(PsiElement.class), new ParameterCompletionProvider());
		extend(CompletionType.BASIC, StandardPatterns.instanceOf(PsiElement.class), new ServiceCompletionProvider());
		extend(CompletionType.BASIC, StandardPatterns.instanceOf(PsiElement.class), new KeywordCompletionProvider());
		extend(CompletionType.BASIC, StandardPatterns.instanceOf(PsiElement.class), new ClassCompletionProvider());
		extend(CompletionType.BASIC, StandardPatterns.instanceOf(PsiElement.class), new NamespaceCompletionProvider());
	}
}

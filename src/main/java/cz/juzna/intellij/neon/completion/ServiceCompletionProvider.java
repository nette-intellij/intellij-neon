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
import cz.juzna.intellij.neon.psi.NeonFile;
import cz.juzna.intellij.neon.psi.NeonKey;
import cz.juzna.intellij.neon.psi.NeonReference;
import cz.juzna.intellij.neon.psi.NeonScalarValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Complete keywords
 */
public class ServiceCompletionProvider extends CompletionProvider<CompletionParameters> {
	// current element
	PsiElement curr;

	public ServiceCompletionProvider() {
		super();
	}

	@Override
	protected void addCompletions(@NotNull CompletionParameters params,
	                              ProcessingContext ctx,
	                              @NotNull CompletionResultSet results) {
		curr = params.getPosition().getOriginalElement();
		if (curr.getParent() instanceof NeonReference) {
			for (String service : getAvailableServices()) {
				results.addElement( LookupElementBuilder.create(service) );
			}
		}
	}


	/**
	 * Find all available services
	 */
	private List<String> getAvailableServices() {
		List<String> services = new LinkedList<String>();

		getAvailableServicesFromSystemContainer(services);

		return services;
	}


	/**
	 * Scans class SystemContainer to find all services in it
	 */
	private void getAvailableServicesFromSystemContainer(List<String> result) {
		PhpClass container = PhpIndex.getInstance(curr.getProject()).getClassByName("SystemContainer");
		if (container != null) {
			for (Field field : container.getFields()) {
				if (field instanceof PhpDocPropertyImpl) {
					result.add( field.getName() );
				}
			}
		}
	}

}

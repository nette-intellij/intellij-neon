package cz.juzna.intellij.neon.completion.providers;

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
import cz.juzna.intellij.neon.NeonIcons;
import cz.juzna.intellij.neon.completion.CompletionUtil;
import cz.juzna.intellij.neon.completion.insert.NeonServiceInsertHandler;
import cz.juzna.intellij.neon.config.NeonConfiguration;
import cz.juzna.intellij.neon.config.NeonService;
import cz.juzna.intellij.neon.psi.*;
import cz.juzna.intellij.neon.util.NeonPhpType;
import org.jetbrains.annotations.NotNull;

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
	protected void addCompletions(
		@NotNull CompletionParameters params,
		ProcessingContext ctx,
	    @NotNull CompletionResultSet results
	) {
		curr = params.getPosition().getOriginalElement();
		boolean incompleteKey = CompletionUtil.isIncompleteKey(curr);
		if (incompleteKey || (!(curr.getParent() instanceof NeonKeyUsage) && !(curr.getParent() instanceof NeonValue))) {
			return;
		}

		List<NeonService> definitions = NeonConfiguration.INSTANCE.findServices(curr.getProject());
		for (NeonService service : definitions) {
			LookupElementBuilder elementBuilder = createElementBuilder(LookupElementBuilder.create(service.getName()));
			elementBuilder = elementBuilder.withTypeText(service.getPhpType().toReadableString());
			results.addElement(elementBuilder);
		}

		List<NeonService> services = getAvailableServices();
		for (NeonService service : services) {
			String type = service.getPhpType().toReadableString();
			type = type.startsWith("\\") ? type.substring(1) : type;
			results.addElement(createElementBuilder(LookupElementBuilder.create(service.getName())).withTypeText(type));
		}
	}

	private static LookupElementBuilder createElementBuilder(LookupElementBuilder lookupElement) {
		lookupElement = lookupElement.withIcon(NeonIcons.SERVICE);
		lookupElement = lookupElement.withInsertHandler(NeonServiceInsertHandler.getInstance());
		return lookupElement;
	}

	/**
	 * Find all available services
	 */
	private List<NeonService> getAvailableServices() {
		List<NeonService> services = new LinkedList<NeonService>();

		getAvailableServicesFromSystemContainer(services);

		return services;
	}

	/**
	 * Scans class SystemContainer to find all services in it
	 */
	private void getAvailableServicesFromSystemContainer(List<NeonService> result) {
		PhpClass container = PhpIndex.getInstance(curr.getProject()).getClassByName("SystemContainer");
		if (container != null) {
			for (Field field : container.getFields()) {
				if (field instanceof PhpDocPropertyImpl) {
					result.add(new NeonService(field.getName(), NeonPhpType.create(field.getType().toString())));
				}
			}
		}
	}

}

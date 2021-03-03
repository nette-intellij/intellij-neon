package cz.juzna.intellij.neon.reference.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import cz.juzna.intellij.neon.indexes.NeonIndexUtil;
import cz.juzna.intellij.neon.psi.NeonPhpElementUsage;
import cz.juzna.intellij.neon.psi.NeonStaticVariable;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NeonPhpStaticVariableReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
	final private String variableName;
	final private Collection<PhpClass> phpClasses;

	public NeonPhpStaticVariableReference(@NotNull NeonStaticVariable element, TextRange textRange) {
		super(element, textRange);
		variableName = element.getVariableName();
		phpClasses = element.getPhpType().getPhpClasses(element.getProject());
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean b) {
		if (phpClasses.size() == 0) {
			return new ResolveResult[0];
		}

		final Collection<NeonStaticVariable> methods = NeonIndexUtil.findStaticVariablesByName(getElement().getProject(), variableName);
		List<ResolveResult> results = new ArrayList<>();
		for (NeonStaticVariable method : methods) {
			if (method.getPhpType().hasClass(phpClasses)) {
				results.add(new PsiElementResolveResult(method));
			}
		}

		List<Field> fields = NeonPhpUtil.getFieldsForPhpElement((NeonPhpElementUsage) getElement());
		String name = ((NeonPhpElementUsage) getElement()).getPhpElementName();
		for (Field field : fields) {
			if (field.getName().equals(name)) {
				results.add(new PsiElementResolveResult(field));
			}
		}

		return results.toArray(new ResolveResult[0]);
	}

	@Nullable
	@Override
	public PsiElement resolve() {
		List<Field> fields = NeonPhpUtil.getFieldsForPhpElement((NeonPhpElementUsage) getElement());
		return fields.size() > 0 ? fields.get(0) : null;
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		return new Object[0];
	}

	@NotNull
	@Override
	public String getCanonicalText() {
		return NeonPhpUtil.normalizePhpVariable(getElement().getText());
	}

	@Override
	public PsiElement handleElementRename(@NotNull String newName) {
		if (getElement() instanceof NeonStaticVariable) {
			((NeonStaticVariable) getElement()).setName(newName);
		}
		return getElement();
	}
/*
    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        if (element instanceof LattePhpStaticVariable) {
            PhpClass originalClass = ((LattePhpStaticVariable) element).getPhpType().getFirstPhpClass(element.getProject());
            if (originalClass != null) {
                if (LattePhpUtil.isReferenceTo(originalClass, multiResolve(false), element, ((LattePhpStaticVariable) element).getVariableName())) {
                    return true;
                }
            }
        }

        if (!(element instanceof Field)) {
            return false;
        }
        PhpClass originalClass = ((Field) element).getContainingClass();
        if (originalClass == null) {
            return false;
        }
        return LattePhpUtil.isReferenceTo(originalClass, multiResolve(false), element, ((Field) element).getName());
    }
*/
}
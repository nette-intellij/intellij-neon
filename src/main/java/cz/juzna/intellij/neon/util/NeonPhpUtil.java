package cz.juzna.intellij.neon.util;

import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import cz.juzna.intellij.neon.config.NeonConfiguration;
import cz.juzna.intellij.neon.config.NeonKeyChain;
import cz.juzna.intellij.neon.config.NeonService;
import cz.juzna.intellij.neon.file.NeonFileType;
import cz.juzna.intellij.neon.psi.*;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NeonPhpUtil {
	public static boolean isReferenceFor(@NotNull String originalClass, @NotNull PhpClass targetClass) {
		originalClass = NeonPhpUtil.normalizeClassName(originalClass);
		if (originalClass.equals(targetClass.getFQN())) {
			return true;
		}

		ExtendsList extendsList = targetClass.getExtendsList();
		for (ClassReference reference : extendsList.getReferenceElements()) {
			if (reference.getFQN() == null) {
				continue;
			}

			if (reference.getFQN().equals(originalClass)) {
				return true;
			}
		}
		return false;
	}

	public static List<PhpTypedElement> getReferencedElements(@NotNull NeonPhpType type, @NotNull Project project, @NotNull String constantName) {
		List<PhpTypedElement> results = new ArrayList<>();
		for (PhpClass phpClass : type.getPhpClasses(project)) {
			boolean hasConstant = false;
			for (Field field : phpClass.getFields()) {
				if (field.getModifier().isPublic() && field.isConstant() && field.getName().equals(constantName)) {
					results.add(field);
					hasConstant = true;
				}
			}

			if (!hasConstant) {
				for (Method method : phpClass.getMethods()) {
					if (method.getModifier().isPublic() && method.getName().equals(constantName)) { //todo: method.isStatic() - only if use as service usage
						results.add(method);
					}
				}
			}
		}
		return results;
	}

	public static List<Method> getReferencedMethods(@NotNull NeonPhpType type, @NotNull Project project, @NotNull String methodName) {
		List<Method> results = new ArrayList<>();
		for (PhpClass phpClass : type.getPhpClasses(project)) {
			for (Method method : phpClass.getMethods()) {
				if (method.getModifier().isPublic() && method.getName().equals(methodName)) {
					results.add(method);
				}
			}
		}
		return results;
	}

	public static boolean isReferenceFor(@NotNull PhpClass originalClass, @NotNull PhpClass targetClass) {
		return isReferenceFor(originalClass.getFQN(), targetClass);
	}

	public static boolean isReferenceTo(@NotNull PhpClass originalClass, @NotNull ResolveResult[] results, @NotNull PsiElement element, @NotNull String name) {
		for (ResolveResult result : results) {
			PsiElement psiElement = result.getElement();
			if (!(psiElement instanceof NeonPhpElementUsage)) {
				continue;
			}

			NeonPhpElementUsage usage = (NeonPhpElementUsage) result.getElement();
			if (!name.equals(usage.getPhpElementName())) {
				continue;
			}

			Collection<PhpClass> phpClasses = usage.getPhpType().getPhpClasses(element.getProject());
			if (phpClasses.size() == 0) {
				continue;
			}

			for (PhpClass phpClass : phpClasses) {
				if (isReferenceFor(originalClass, phpClass)) {
					return true;
				}
			}
		}
		return false;
	}

	public static Collection<String> getAllExistingClassNames(Project project, PrefixMatcher prefixMatcher) {
		return getPhpIndex(project).getAllClassNames(prefixMatcher);
	}

	public static Collection<PhpNamedElement> getAllClassNamesAndInterfaces(Project project, Collection<String> classNames, String namespace) {
		Collection<PhpNamedElement> variants = new THashSet<PhpNamedElement>();
		PhpIndex phpIndex = getPhpIndex(project);

		for (String name : classNames) {
			variants.addAll(filterClasses(phpIndex.getClassesByName(name), namespace));
			variants.addAll(filterClasses(phpIndex.getInterfacesByName(name), namespace));
		}
		return variants;
	}

	public static List<NeonClassUsage> findNeonPhpUsages(String searchedName, Project project) {
		return NeonPsiUtil.acceptAllFiles(NeonFileType.INSTANCE, project, NeonClassUsage.class, (NeonClassUsage reference) -> reference.getClassName().equals(searchedName));
	}

	public static List<NeonMethodUsage> findNeonMethodUsages(NeonPhpType phpType, String searchedName, Project project) {
		return NeonPsiUtil.acceptAllFiles(NeonFileType.INSTANCE, project, NeonMethodUsage.class, (NeonMethodUsage usage)
				-> usage.getMethodName().equals(searchedName) && phpType.hasClass(usage.getPhpType().getPhpClasses(project))
		);
	}

	public static List<NeonConstantUsage> findNeonConstantUsages(NeonPhpType phpType, String searchedName, Project project) {
		return NeonPsiUtil.acceptAllFiles(NeonFileType.INSTANCE, project, NeonConstantUsage.class, (NeonConstantUsage usage)
				-> usage.getConstantName().equals(searchedName) && phpType.hasClass(usage.getPhpType().getPhpClasses(project))
		);
	}

	public static void attachNeonPhpNamespaces(String searchedName, List<ResolveResult> results, Project project) {
		NeonPhpUtil.attachNeonKeyDefinitions(new PsiRecursiveElementVisitor() {
			@Override
			public void visitElement(PsiElement element) {
				if (element instanceof NeonNamespaceReference) {
					if(
							((NeonNamespaceReference) element).getNamespaceName().equals(searchedName)
					) {
						results.add(new PsiElementResolveResult(element));
					} else {
						super.visitElement(element);
					}
				} else {
					super.visitElement(element);
				}
			}
		}, project);
	}

	public static void findNeonKeyDefinitions(List<NeonKey> properties, PsiElement psiElement) {
		psiElement.acceptChildren(new PsiRecursiveElementVisitor() {
			@Override
			public void visitElement(PsiElement element) {
				if (element instanceof NeonKey) {
					properties.add((NeonKey) element);
				} else {
					super.visitElement(element);
				}
			}
		});
	}

	public static List<NeonKey> attachNeonKeyDefinitionsForParameters(String searchedName, Project project) {
		return NeonPsiUtil.acceptAllFiles(NeonFileType.INSTANCE, project, NeonKey.class, (NeonKey key)
				-> key.isParameterDefinition() && key.getKeyChain(false).withChildKey(key.getKeyText()).equalsWithoutMainKeyWithDots(searchedName)
		);
	}

	public static List<NeonKey> attachNeonKeyDefinitionsForServices(String searchedName, Project project) {
		return NeonPsiUtil.acceptAllFiles(NeonFileType.INSTANCE, project, NeonKey.class, key -> key.isServiceDefinition() && key.getKeyText().equals(searchedName));
	}

	public static List<NeonKeyValPair> attachNeonKeyDefinitionsForServicesByClasses(String className, Project project) {
		return NeonPsiUtil.acceptAllFiles(
				NeonFileType.INSTANCE,
				project,
				NeonKeyValPair.class,
				(NeonKeyValPair pair) -> pair.getKey() != null
						&& pair.getScalarValue() != null
						&& pair.getKey().isArrayBullet()
						&& pair.getKey().isServiceDefinition()
						&& pair.getScalarValue().getPhpType().hasClass(className)
		);
	}

	private static void attachNeonKeyDefinitions(PsiElementVisitor recursiveVisitor, Project project) {
		NeonPsiUtil.acceptAllFiles(NeonFileType.INSTANCE, recursiveVisitor, project);
	}

	public static List<NeonKey> findNeonParameterDefinitions(Project project) {
		return NeonPsiUtil.acceptAllFiles(NeonFileType.INSTANCE, project, NeonKey.class, NeonKey::isParameterDefinition);
	}

	@Nullable
	public static NeonKey findNeonParameterDefinition(String parameterName, @NotNull Project project) {
		return NeonPsiUtil.acceptAllFiles(NeonFileType.INSTANCE, project, NeonKey.class, (NeonKey key)
				-> key.isParameterDefinition() && key.getKeyChain(false).withChildKey(key.getKeyText()).equalsWithoutMainKeyWithDots(parameterName)
		).stream().findFirst().orElse(null);
	}

	@Nullable
	public static NeonService findNeonServiceDefinition(String serviceName, @NotNull Project project) {
		return findNeonServiceDefinitions(project, serviceName).stream().findFirst().orElse(null);
	}

	public static List<NeonService> findNeonServiceDefinitions(@NotNull Project project) {
		return findNeonServiceDefinitions(project, null);
	}

	private static List<NeonService> findNeonServiceDefinitions(@NotNull Project project, @Nullable String serviceName) {
		List<NeonService> services = new ArrayList<NeonService>();

		List<NeonKey> definitions = NeonPsiUtil.acceptAllFiles(NeonFileType.INSTANCE, project, NeonKey.class, (NeonKey key) -> {
					if (!key.isServiceDefinition() || !key.getKeyChain(false).isLevel(1)) {
						return false;
					}
					if (serviceName == null || (!key.isArrayBullet() && key.getKeyText().equals(serviceName))) {
						return true;
					}

					if (!(key.getParent() instanceof NeonKeyValPair)) {
						return false;
					}

					NeonKeyValPair pair = (NeonKeyValPair) key.getParent();

					return pair.getScalarValue() != null
						&& key.isArrayBullet()
						&& key.isServiceDefinition()
						&& pair.getScalarValue().getPhpType().hasClass(serviceName);
				}
		);
		for (NeonKey key : definitions) {
			if (!(key.getParent() instanceof NeonKeyValPair)) {
				continue;
			}

			NeonKeyValPair parent = ((NeonKeyValPair) key.getParent());
			boolean isArrayBullet = key.isArrayBullet();
			String name = null;
			NeonPhpType found = null;
			NeonScalarValue value = parent.getScalarValue();
			if (value != null) {
				NeonPhpType phpType = value.getPhpType();
				if (phpType.containsClasses()) {
					found = phpType;
					if (isArrayBullet) {
						name = found.toReadableString();
					}
				}
			}
			if (found == null && parent.getArray() != null) {
				for (NeonKeyValPair keyValPair : parent.getArray().getKeyValPairList()) {
					if (keyValPair.getKey() != null && keyValPair.getScalarValue() != null) {
						String keyText = keyValPair.getKey().getKeyText();
						NeonPhpType phpType = keyValPair.getScalarValue().getPhpType();

						if (keyText.equals(NeonConfiguration.KEY_CLASS)) {
							if (phpType.containsClasses()) {
								found = phpType;
							}

						} else if (keyText.equals(NeonConfiguration.KEY_FACTORY)) {
							if (phpType.containsClasses()) {
								for (PhpClass phpClass : phpType.getPhpClasses(project)) {
									Method factoryMethod = phpClass.findMethodByName("create");
									if (factoryMethod != null && factoryMethod.getModifier().isPublic()) {
										found = NeonPhpType.create(factoryMethod.getType().toString());
									}
								}
							} else {
								NeonScalarValue scalarValue = keyValPair.getScalarValue();
								if (scalarValue != null) {
									for (NeonValue neonValue : scalarValue.getValueList()) {
										if (neonValue.getPhpStatement() != null) {
											found = neonValue.getPhpStatement().getPhpType();
										} else if (neonValue.getServiceStatement() != null) {
											found = neonValue.getServiceStatement().getPhpType();
										}
									}
								}
							}
						}

						if (found != null && isArrayBullet) {
							name = found.toReadableString();
						}
					}
				}
			}

			if (name == null) {
				name = key.getKeyText();
			}

			services.add(new NeonService(name, found != null ? found : NeonPhpType.MIXED));
		}
		return services;
	}

	public static List<NeonKeyUsage> attachNeonKeyUsages(String searchedName, Project project) {
		return NeonPsiUtil.acceptAllFiles(
				NeonFileType.INSTANCE,
				project,
				NeonKeyUsage.class,
				(NeonKeyUsage usage) -> usage.getClassReference() == null && usage.getKeyText().equals(searchedName)
		);
	}

	@NotNull
	public static List<NeonKeyUsage> attachNeonKeyUsagesByTypes(NeonPhpType phpType, Project project) {
		return NeonPsiUtil.acceptAllFiles(
				NeonFileType.INSTANCE,
				project,
				NeonKeyUsage.class,
				(NeonKeyUsage usage) -> usage.getClassReference() != null && phpType.hasClass(usage.getClassReference().getClassName())
		);
	}

	@NotNull
	public static List<NeonParameterUsage> attachNeonParameterUsages(String searchedName, Project project) {
		return NeonPsiUtil.acceptAllFiles(
				NeonFileType.INSTANCE,
				project,
				NeonParameterUsage.class,
				(NeonParameterUsage usage) -> usage.getKeyText().equals(searchedName)
		);
	}

	@NotNull
	public static List<NeonParameterUsage> attachNeonParameterUsages(NeonKeyChain keyChain, Project project) {
		return NeonPsiUtil.acceptAllFiles(NeonFileType.INSTANCE, project, NeonParameterUsage.class, (NeonParameterUsage usage) -> {
				String fullName = "parameters." + usage.getKeyText();
				NeonKeyChain chain = NeonKeyChain.get(fullName.split("\\."));
				return keyChain.toDottedString().equals(fullName) || keyChain.isParentOf(chain);
			});
	}

	public static Collection<PhpClass> getClassesByFQN(Project project, String className) {
		return getPhpIndex(project).getAnyByFQN(className);
	}

	public static Collection<PhpNamespace> getNamespacesByName(Project project, String className) {
		return getPhpIndex(project).getNamespacesByName(className);
	}

	public static PhpIndex getPhpIndex(Project project) {
		return PhpIndex.getInstance(project);
	}

	@NotNull
	public static PsiElement replaceChildNode(@NotNull PsiElement psiElement, @NotNull PsiElement newElement, @Nullable ASTNode keyNode) {
		ASTNode newKeyNode = newElement.getFirstChild().getNode();
		if (newKeyNode == null) {
			return psiElement;
		}

		if (keyNode == null) {
			psiElement.getNode().addChild(newKeyNode);

		} else {
			psiElement.getNode().replaceChild(keyNode, newKeyNode);
		}
		return psiElement;
	}

	private static Collection<PhpClass> filterClasses(Collection<PhpClass> classes, String namespace) {
		if (namespace == null) {
			return classes;
		}
		namespace = NeonPhpUtil.normalizeClassName(namespace) + "\\";
		Collection<PhpClass> result = new ArrayList<PhpClass>();
		for (PhpClass cls : classes) {
			String classNs = cls.getNamespaceName();
			if (classNs.equals(namespace) || classNs.startsWith(namespace)) {
				result.add(cls);
			}
		}
		return result;
	}

	public static String normalizeClassName(@Nullable String className) {
		className = className == null ? "" : (className.startsWith("@") ? className.substring(1) : className);
		className = className.startsWith("\\") ? className : ("\\" + className);
		return (className.endsWith("\\") ? className.substring(0, className.length() - 1) : className).replace("\\\\", "\\");
	}
}

package cz.juzna.intellij.neon.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import cz.juzna.intellij.neon.completion.CompletionUtil;
import cz.juzna.intellij.neon.config.NeonConfiguration;
import cz.juzna.intellij.neon.config.NeonKeyChain;
import cz.juzna.intellij.neon.config.NeonService;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpMethodStub;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpClassStub;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpConstantStub;
import cz.juzna.intellij.neon.indexes.stubs.NeonPhpStaticVariableStub;
import cz.juzna.intellij.neon.lexer.NeonTokenTypes;
import cz.juzna.intellij.neon.psi.*;
import cz.juzna.intellij.neon.psi.elements.NeonArrayElement;
import cz.juzna.intellij.neon.psi.elements.NeonKeyElement;
import cz.juzna.intellij.neon.reference.NeonElementFactory;
import cz.juzna.intellij.neon.util.NeonPhpType;
import cz.juzna.intellij.neon.util.NeonPhpUtil;
import cz.juzna.intellij.neon.util.NeonUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;

public class NeonPsiImplUtil {

    private static final Pattern number = Pattern.compile("^\\d+$");

    public static String getKeyText(NeonKey key) {
        if (key.isArrayBullet()) {
            NeonArrayElement array = PsiTreeUtil.getParentOfType(key, NeonArrayElement.class);
            if (array == null) {
                return "-";
            }
            int offset = 0;
            for (NeonKey currentKey : array.getKeys()) {
                if (currentKey == key) {
                    break;
                } else if (currentKey.isArrayBullet()) {
                    offset++;
                }
            }
            return String.valueOf(offset);
        }

        NeonWholeString wholeString = key.getWholeString();
        if (wholeString != null) {
            return wholeString.getStringValue();
        }

        StringBuilder out = new StringBuilder();
        key.acceptChildren(new PsiRecursiveElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                IElementType type = element.getNode().getElementType();
                if (NeonTokenTypes.HIGHLIGHT_KEYWORD_ELEMENTS.contains(type)) {
                    super.visitElement(element);
                } else if (type != NeonTokenTypes.NEON_COLON) {
                    out.append(element.getText());
                }
            }
        });
        if (out.length() > 0) {
            return out.toString();
        }

        return NeonUtil.normalizeKeyName(key.getText().replaceAll("^[\\s\\t]+",""));
    }

    public static @NotNull NeonPhpType getPhpType(@NotNull PsiElement element) {
        PsiElement prev = PsiTreeUtil.skipWhitespacesBackward(element);

        PsiElement prevElement = PsiTreeUtil.skipWhitespacesBackward(prev);

        NeonPhpType type = null;
        if (prevElement instanceof NeonClassReference) {
            type = NeonPhpType.create(((NeonClassReference) prevElement).getClassName());
        } else if (prevElement instanceof NeonKeyUsage) {
            type = ((NeonKeyUsage) prevElement).getPhpType();
        } else if (prevElement instanceof NeonConstantUsage) {
            type = ((NeonConstantUsage) prevElement).getPhpType();
        }else if (prevElement instanceof NeonMethodUsage) {
            type = ((NeonMethodUsage) prevElement).getPhpType();
        }
        return type != null ? type : NeonPhpType.MIXED;
    }

    public static @NotNull NeonPhpType getReturnType(@NotNull NeonPhpElementUsage element) {
        NeonPhpType type = element.getPhpType();
        if (!type.containsClasses()) {
            return NeonPhpType.MIXED;
        }

        for (PhpTypedElement reference : NeonPhpUtil.getReferencedElements(type, element.getProject(), element.getPhpElementName())) {
            return NeonPhpType.create(reference.getType().toString());
        }
        return NeonPhpType.MIXED;
    }

    public static @Nullable NeonPhpStatementElement getPhpStatement(@NotNull PsiElement element) {
        return PsiTreeUtil.getParentOfType(element, NeonPhpStatementElement.class);
    }

    public static String getVariableName(@NotNull NeonStaticVariable element) {
        final NeonPhpStaticVariableStub stub = element.getStub();
        if (stub != null) {
            return stub.getVariableName();
        }

        PsiElement found = findFirstChildWithType(element, NeonTokenTypes.NEON_PHP_VARIABLE_USAGE);
        return found != null ? NeonPhpUtil.normalizePhpVariable(found.getText()) : null;
    }

    private static PsiElement findFirstChildWithType(PsiElement element, @NotNull IElementType type) {
        ASTNode keyNode = element.getNode().findChildByType(type);
        if (keyNode != null) {
            return keyNode.getPsi();
        } else {
            return null;
        }
    }

    public static boolean isStatic(@NotNull NeonPhpStatement element) {
        return true; //todo: add support non static methods
    }

    public static boolean isStatic(@NotNull NeonServiceStatement element) {
        return false;
    }

    public static @NotNull NeonPhpType getPhpType(@NotNull NeonPhpStatement element) {
        PsiElement lastChild = element.getLastChild();
        if (lastChild instanceof NeonPhpElementUsage) {
            return ((NeonPhpElementUsage) lastChild).getPhpType();
        }
        return NeonPhpType.MIXED;
    }

    public static @NotNull NeonPhpType getPhpType(@NotNull NeonServiceStatement element) {
        PsiElement lastChild = element.getLastChild();
       if (lastChild instanceof NeonPhpElementUsage) {
            return ((NeonPhpElementUsage) lastChild).getReturnType();
        }
        return NeonPhpType.MIXED;
    }

    public static List<PsiElement> getKeyTextElements(NeonKeyElement key) {
        List<PsiElement> out = new ArrayList<>();
        key.acceptChildren(new PsiRecursiveElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                IElementType type = element.getNode().getElementType();
                if (type == NeonTokenTypes.NEON_INDENT) {
                    super.visitElement(element);
                } else if (type != NeonTokenTypes.NEON_COLON) {
                    out.add(element);
                }
            }
        });
        if (out.size() > 0) {
            return out;
        }

        out.add(key);
        return out;
    }

    public static String getIndentCharacters(NeonIndent indent) {
        return indent.getText().replace("\n", "");
    }

    public static String getMethodName(NeonMethodUsage methodUsage) {
        final NeonPhpMethodStub stub = methodUsage.getStub();
        if (stub != null) {
            return stub.getMethodName();
        }
        return methodUsage.getText();
    }

    public static String getConstantName(NeonConstantUsage constantUsage) {
        final NeonPhpConstantStub stub = constantUsage.getStub();
        if (stub != null) {
            return stub.getConstantName();
        }
        return constantUsage.getText();
    }

    public static String getServiceName(PsiElement psiElement) {
        NeonKeyValPair keyValPair = PsiTreeUtil.getParentOfType(psiElement, NeonKeyValPair.class);
        if (keyValPair == null || keyValPair.getKey() == null || !keyValPair.getKey().isServiceDefinition()) {
            return "";
        }

        NeonKeyChain keyChain = keyValPair.getKey().getKeyChain(false);
        if (!keyChain.isLevel(3)) {
            return "";
        }

        String[] chain = keyChain.toArray();
        if (chain.length == 3 && chain[0].equals("services") && chain[2].equals(NeonConfiguration.KEY_SETUP)) {
            return chain[1];
        }
        return "";
    }

    @NotNull
    public static NeonPhpType getPhpType(NeonMethodUsage psiElement) {
        NeonService service = NeonConfiguration.INSTANCE.findService(psiElement.getServiceName(), psiElement.getProject());
        return service == null ? getPhpType((PsiElement) psiElement) : service.getPhpType();
    }

    @NotNull
    public static NeonPhpType getPhpType(NeonKeyElement key) {
        if (key.getParent() instanceof NeonKeyValPair && ((NeonKeyValPair) key.getParent()).getScalarValue() != null) {
            return ((NeonKeyValPair) key.getParent()).getScalarValue().getPhpType();
        }
        return NeonPhpType.MIXED;
    }

    public static boolean isSetupMethod(NeonMethodUsage methodUsage) {
        NeonKeyValPair keyValPair = PsiTreeUtil.getParentOfType(methodUsage, NeonKeyValPair.class);
        if (keyValPair == null || keyValPair.getKey() == null) {
            return false;
        }
        NeonKeyChain keyChain = keyValPair.getKey().getKeyChain(false);
        return keyChain.isLevel(2) && keyChain.equalsMainKey("services");
    }

    public static String getClassName(NeonClassReference classReference) {
        final NeonPhpClassStub stub = classReference.getStub();
        if (stub != null) {
            return stub.getClassName();
        }
        return classReference.getClassUsage().getClassName();
    }

    public static @NotNull NeonPhpType getPhpType(@NotNull NeonClassUsage element) {
        return NeonPhpType.create(element.getClassName());
    }

    public static @NotNull NeonPhpType getPhpType(@NotNull NeonClassReference element) {
        return element.getClassUsage().getPhpType();
    }

    public static String getClassName(NeonClassUsage classUsage) {
        StringBuilder out = new StringBuilder();
        classUsage.getParent().acceptChildren(new PsiRecursiveElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                IElementType type = element.getNode().getElementType();
                if (NeonTokenTypes.CLASS_USAGE_ELEMENTS.contains(type)) {
                    out.append(element.getText());
                } else {
                    super.visitElement(element);
                }
            }
        });
        return NeonPhpUtil.normalizeClassName(out.toString());
    }

    public static String getNamespaceName(NeonNamespaceReference namespaceReference) {
        StringBuilder out = new StringBuilder();
        for (PsiElement element : namespaceReference.getParent().getChildren()) {
            if (element instanceof NeonNamespaceReference) {
                out.append("\\").append(element.getText());
            }

            if (element == namespaceReference) {
                break;
            }
        }
        return NeonPhpUtil.normalizeClassName(out.toString());
    }

    public static String getStringValue(NeonWholeString wholeString) {
        StringBuilder out = new StringBuilder();
        wholeString.acceptChildren(new PsiRecursiveElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                IElementType type = element.getNode().getElementType();
                if (NeonTokenTypes.STRING_QUOTES.contains(type)) {
                    super.visitElement(element);
                } else {
                    out.append(element.getText());
                }
            }
        });
        return out.toString();
    }

    public static PsiElement getStringElement(NeonWholeString wholeString) {
        final PsiElement[] out = new PsiElement[1];
        wholeString.acceptChildren(new PsiRecursiveElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                IElementType type = element.getNode().getElementType();
                if (NeonTokenTypes.STRING_QUOTES.contains(type)) {
                    super.visitElement(element);
                } else {
                    out[0] = element;
                }
            }
        });
        return out[0];
    }

    public static boolean isServiceDefinition(NeonKeyElement key) {
        return key.getKeyChain(false).equalsMainKey("services");
    }

    public static boolean isParameterDefinition(NeonKeyElement key) {
        return key.getKeyChain(false).equalsMainKey("parameters");
    }

    public static boolean isArrayBullet(NeonKeyElement key) {
        return key.getFirstChild() != null && key.getFirstChild().getNode().getElementType() == NeonTokenTypes.NEON_ARRAY_BULLET;
    }

    public static boolean isMainKey(NeonKeyValPair keyValPair) {
        return keyValPair.getIndent() instanceof NeonMainArray;
    }

    @Nullable
    public static NeonIndent getIndent(NeonKeyValPair keyValPair) {
        return keyValPair.getIndentList().size() > 0 ? keyValPair.getIndentList().get(0) : null;
    }

    @Nullable
    public static NeonKey getKey(NeonKeyValPair keyValPair) {
        return keyValPair.getKeyList().size() > 0 ? keyValPair.getKeyList().get(0) : null;
    }

    public static boolean isLastKey(NeonKeyValPair keyValPair) {
        if (!(keyValPair.getPrevSibling() instanceof NeonKeyValPair) || keyValPair.getNextSibling() != null) {
            return false;
        }
        NeonKeyValPair prev = ((NeonKeyValPair) keyValPair.getPrevSibling());
        return prev.getScalarValueList().size() == 0;
    }

    @Nullable
    public static NeonScalarValue getScalarValue(NeonKeyValPair keyValPair) {
        return keyValPair.getScalarValueList().size() > 0 ? keyValPair.getScalarValueList().get(0) : null;
    }

    public static NeonKeyChain getKeyChain(NeonKeyElement key, boolean isIncomplete) {
        return CompletionUtil.getKeyChain(resolveKeyElementForChain(key, isIncomplete));
    }

    public static String getKeyText(NeonKeyUsage key) {
        if (key.getClassReference() != null) {
            return "";
        }
        return key.getText().trim().substring(1);
    }

    @Deprecated
    public static String getClassName(NeonKeyUsage key) {
        if (key.getClassReference() != null) {
            return key.getClassReference().getClassName();
        }
        PsiElement parent = key.getParent();
        if (parent instanceof NeonPhpStatementElement) {
            return ((NeonPhpStatementElement) parent).getPhpType().toReadableString();
        }

        NeonService service = NeonPhpUtil.findNeonServiceDefinition(key.getKeyText(), key.getProject());
        return service != null ? service.getPhpType().toString() : "";
    }

    public static NeonPhpType getPhpType(NeonKeyUsage key) {
        if (key.getClassReference() != null) {
            return NeonPhpType.create(key.getClassReference().getClassName());
        }
        //PsiElement parent = key.getParent();
        //if (parent instanceof NeonPhpStatementElement) {
        //    return ((NeonPhpStatementElement) parent).getPhpType();
        //}

        NeonService service = NeonPhpUtil.findNeonServiceDefinition(key.getKeyText(), key.getProject());
        return service != null ? NeonPhpType.create(service.getPhpType().toString()) : NeonPhpType.MIXED;
    }

    public static boolean isLastKeyUsed(NeonParameterUsage key) {
        PsiElement next = PsiTreeUtil.skipWhitespacesForward(key);
        return next == null || next.getNode().getElementType() == NeonTokenTypes.NEON_PARAMETER_RIGHT;
    }

    public static String getKeyText(NeonParameterUsage key) {
        PsiElement prev = key;
        NeonKeyChain out = NeonKeyChain.get();
        while (prev != null) {
            if (prev instanceof NeonParameterUsage) {
                out = out.withParent(prev.getText());
            }
            prev = PsiTreeUtil.skipWhitespacesBackward(prev);
        }
        return out.toDottedString();
    }

    public static String getKeyText(NeonKeyValPair key) {
        return key.getKey() != null ? key.getKey().getKeyText() : "";
    }

    @NotNull
    public static NeonPhpType getPhpType(NeonScalarValue value) {
        if (value != null) {
            for (NeonValue neonValue : value.getValueList()) {
                NeonClassReference classReference = neonValue.getClassReference();
                if (classReference == null) {
                    continue;
                }
                String type = classReference.getClassName();
                type = type.startsWith("\\") ? type.substring(1) : type;
                return NeonPhpType.create(type);
            }
        }
        return NeonPhpType.MIXED;
    }

    public static HashMap<String, NeonScalarValue> getMap(PsiElement array) {
        HashMap<String, NeonScalarValue> result = new LinkedHashMap<>();
        Integer key = 0;
        for (PsiElement el : array.getChildren()) {
            if (el instanceof NeonKeyValPair) {
                NeonKeyValPair keyVal = (NeonKeyValPair) el;
                String keyText = keyVal.getKeyText();
                result.put(keyText, ((NeonKeyValPair) el).getScalarValue());
                if (number.matcher(keyText).matches()) {
                    Integer keyInt = Integer.parseInt(keyText);
                    key = Math.max(keyInt + 1, key);
                }
            } else {
                String stringKey = (key++).toString();
                result.put(stringKey, el.getChildren().length > 0 ? (NeonScalarValue) el.getChildren()[0] : null);
            }
        }

        return result;
    }

    public static List<NeonKey> getKeys(PsiElement array) {
        ArrayList<NeonKey> result = new ArrayList<>();
        for (PsiElement el : array.getChildren()) {
            if (el instanceof NeonKeyValPair && ((NeonKeyValPair) el).getKey() != null) {
                result.add(((NeonKeyValPair) el).getKey());
            }
        }
        return result;
    }

    public static List<NeonKey> getKeys(NeonArrayOfValues array) {
        ArrayList<NeonKey> result = new ArrayList<>();
        for (PsiElement el : array.getChildren()) {
            if (el instanceof NeonKeyValPair && ((NeonKeyValPair) el).getKey() != null) {
                result.add(((NeonKeyValPair) el).getKey());
            }
        }
        return result;
    }

    public static String getKeyText(NeonArrayKeyValuePair keyValuePair) {
        return keyValuePair.getKey().getKeyText();
    }

    public static boolean isEmpty(NeonArrayElement array) {
        return array.getKeys().size() == 0;
    }

    public static String getName(NeonKeyElement element) {
        return element.getKeyText();
    }

    public static PsiElement setName(NeonKeyElement element, String newName) {
        ASTNode keyNode = element.getFirstChild().getNode();
        String quote = element.getWholeString() != null && NeonTokenTypes.STRING_QUOTES.contains(element.getWholeString().getNode().getElementType())
                ? element.getWholeString().getFirstChild().getText()
                : "";
        NeonKey key = NeonElementFactory.createKey(element.getProject(), quote + newName + quote);
        if (key == null) {
            return element;
        }
        return replaceKeyNode(element, key, keyNode);
    }

    public static PsiElement getNameIdentifier(NeonKeyElement element) {
        if (element.getWholeString() != null) {
            return element.getWholeString().getStringElement();
        }

        List<PsiElement> elements = element.getKeyTextElements();
        if (elements.size() > 0) {
            return elements.get(0);
        }
        if (element.getFirstChild() == null) {
            return element;
        }
        return element.getFirstChild().getNextSibling() != null ? element.getFirstChild().getNextSibling() : element.getFirstChild();
    }

    public static String getName(NeonClassUsage element) {
        return element.getClassName();
    }

    public static PsiElement setName(NeonClassUsage element, String newName) {
        ASTNode keyNode = element.getLastChild().getNode();
        PsiElement classUsage;
        if (keyNode.getElementType() == NeonTokenTypes.NEON_NAMESPACE_REFERENCE) {
            classUsage = NeonElementFactory.createClassRootUsage(element.getProject(), newName);
        } else if (keyNode.getElementType() == NeonTokenTypes.NEON_METHOD) {
            classUsage = NeonElementFactory.createClassConstructUsage(element.getProject(), newName);
        } else {
            classUsage = NeonElementFactory.createClassUsage(element.getProject(), newName);
        }

        if (classUsage == null) {
            return element;
        }
        return replaceLastNode(element, classUsage, keyNode);
    }

    public static PsiElement getNameIdentifier(NeonMethodUsage element) {
        return element.getFirstChild() != null ? element.getFirstChild() : element;
    }

    public static String getName(NeonMethodUsage element) {
        return element.getMethodName();
    }

    public static PsiElement setName(NeonMethodUsage element, String newName) {
        ASTNode keyNode = element.getNode();
        NeonMethodUsage methodUsage = NeonElementFactory.createMethodUsage(element.getProject(), newName);

        if (methodUsage == null) {
            return element;
        }
        return replaceNode(element.getParent(), methodUsage, keyNode);
    }

    public static PsiElement getNameIdentifier(NeonStaticVariable element) {
        return element.getFirstChild() != null ? element.getFirstChild() : element;
    }

    public static String getName(NeonStaticVariable element) {
        return element.getVariableName();
    }

    public static PsiElement setName(NeonStaticVariable element, String newName) {
        return element;
        /*
        ASTNode keyNode = element.getNode();
        NeonMethodUsage methodUsage = NeonElementFactory.createMethodUsage(element.getProject(), newName);

        if (methodUsage == null) {
            return element;
        }
        return replaceNode(element.getParent(), methodUsage, keyNode);*/
    }

    public static PsiElement getNameIdentifier(NeonClassUsage element) {
        return element.getFirstChild() != null ? element.getFirstChild() : element;
    }

    public static String getName(NeonNamespaceReference element) {
        return element.getNamespaceName();
    }

    public static PsiElement setName(NeonNamespaceReference element, String newName) {
        ASTNode keyNode = element.getNode();
        NeonNamespaceReference namespaceReference = NeonElementFactory.createNamespaceReference(element.getProject(), newName);
        if (namespaceReference == null) {
            return element;
        }
        return replaceNode(element.getParent(), namespaceReference, keyNode);
    }

    public static PsiElement getNameIdentifier(NeonNamespaceReference element) {
        return element;
    }

    public static String getName(NeonParameterUsage element) {
        return element.getText();
    }

    public static PsiElement setName(NeonParameterUsage element, String newName) {
        ASTNode keyNode = element.getNode();
        NeonParameterUsage parameterUsage = NeonElementFactory.createNeonParameterUsage(element.getProject(), newName);
        if (parameterUsage == null) {
            return element;
        }
        return replaceNode(element.getParent(), parameterUsage, keyNode);
    }

    public static PsiElement getNameIdentifier(NeonParameterUsage element) {
        return element;
    }

    private static PsiElement resolveKeyElementForChain(PsiElement element, boolean isIncomplete)
    {
        if (isIncomplete) {
            return element.getParent();
        } else if (element.getParent().getParent() instanceof NeonFile) {
            return element.getParent();
        } else  {
            // literal -> scalar -> [key ->] key-val pair -> any parent
            return element instanceof NeonKey ? element.getParent().getParent() : element.getParent();
        }
    }

    @NotNull
    private static PsiElement replaceKeyNode(@NotNull PsiElement psiElement, @NotNull PsiElement newElement, @Nullable ASTNode keyNode) {
        ASTNode[] newKeyNodes = newElement.getNode().getChildren(TokenSet.ANY);
        if (newKeyNodes.length == 0) {
            return psiElement;
        }

        if (keyNode == null) {
            for (ASTNode newKeyNode : newKeyNodes) {
                psiElement.getNode().addChild(newKeyNode);
            }

        } else {
            psiElement.getNode().replaceChild(keyNode, newKeyNodes[0]);
            if (newKeyNodes.length > 1) {
                for (ASTNode newKeyNode : Arrays.asList(newKeyNodes).subList(1, newKeyNodes.length)) {
                    psiElement.getNode().addChild(newKeyNode);
                }
            }
        }
        return psiElement;
    }

    @NotNull
    private static PsiElement replaceNode(@NotNull PsiElement psiElement, @NotNull PsiElement newElement, @Nullable ASTNode keyNode) {
        ASTNode newKeyNode = newElement.getNode();
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

    @NotNull
    private static PsiElement replaceLastNode(@NotNull PsiElement psiElement, @NotNull PsiElement newElement, @Nullable ASTNode keyNode) {
        ASTNode newKeyNode = newElement.getLastChild().getNode();
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

}

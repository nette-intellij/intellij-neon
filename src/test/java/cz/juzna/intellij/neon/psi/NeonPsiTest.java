package cz.juzna.intellij.neon.psi;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase;
import cz.juzna.intellij.neon.psi.elements.NeonPsiElement;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.*;


public class NeonPsiTest extends LightPlatformCodeInsightFixture4TestCase {

	@Override
	protected String getTestDataPath() {
		URL url = getClass().getClassLoader().getResource("data/psi");
		assert url != null;
		return url.getFile();
	}

	protected NeonPsiElement getPsiElement() {
		return ((NeonFile) myFixture.configureByFile(getTestDataPath() + "/" + getTestName(true) + ".neon")).getValue();
	}

	@Test
	public void testArrayKeys() {
		NeonArray array = (NeonArray) getPsiElement();
		List<NeonKey> keys = array.getKeys();
		assertSize(1, keys);
		Assert.assertEquals(keys.iterator().next().getKeyText(), "4");

		Set<String> keys2 = array.getMap().keySet();
		assertSize(4, keys2);
		assertContainsElements(Arrays.asList("0", "1", "4", "5"), keys2);
	}
/*
	@Test
	public void testArrayNull() {
		NeonArray array = (NeonArray) getPsiElement();

		List<NeonValue> values = array.getValues();

		assertSize(4, values);
		Assert.assertTrue(values.get(0) instanceof NeonScalar);
		Assert.assertEquals("null", values.get(0).getText());
		Assert.assertTrue(values.get(1) instanceof NeonScalar);
		Assert.assertNull(values.get(2));
		Assert.assertNull(values.get(3));
	}

	@Test
	public void testEntity() {
		NeonEntity entity = (NeonEntity) getPsiElement();
		Assert.assertEquals("foo", entity.getName());
		assertSize(1, entity.getArgs().getValues());
	}

	@Test
	public void testChainedEntity() {
		NeonChainedEntity chain = (NeonChainedEntity) getPsiElement();
		assertSize(2, chain.getValues());
		Assert.assertEquals("foo", chain.getValues().get(0).getName());
		Assert.assertEquals("bar", chain.getValues().get(1).getName());
		assertSize(1, chain.getValues().get(0).getArgs().getValues());
		assertSize(2, chain.getValues().get(1).getArgs().getValues());
	}*/

}

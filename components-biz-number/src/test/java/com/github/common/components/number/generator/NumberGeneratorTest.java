package com.github.common.components.number.generator;

import com.github.common.components.number.sequence.RandomSequenceGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/** Created by lenovo on 2018/9/15. */
public class NumberGeneratorTest {
  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void generate() throws Exception {
    NumberGenerator generator = new NumberGeneratorImpl();
    generator.setSequenceGenerator(new RandomSequenceGenerator());
    generator.setNumberRules(new StaticNumberRuleLoader().load());
    String number = generator.generate("SALE_ORDER");
    System.out.println(number);
    Assert.assertNotNull(number);
  }
}

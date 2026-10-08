package org.gzoltar.java17;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SwitchExpressionFeaturesTest {

  private final SwitchExpressionFeatures switchExpressionFeatures = new SwitchExpressionFeatures();

  @Test
  public void testDayType_Weekend() {
    assertEquals("weekend", switchExpressionFeatures.dayType(SwitchExpressionFeatures.Day.SATURDAY));
  }

  @Test
  public void testDayType_Weekday() {
    assertEquals("weekday", switchExpressionFeatures.dayType(SwitchExpressionFeatures.Day.MONDAY));
  }

  @Test
  public void testLettersInDayName_Monday() {
    assertEquals(6, switchExpressionFeatures.lettersInDayName(SwitchExpressionFeatures.Day.MONDAY));
  }

  @Test
  public void testLettersInDayName_Sunday_ExposesFault() {
    // "SUNDAY" has 6 letters; this exposes the injected FAULT (yield length + 1).
    assertEquals(6, switchExpressionFeatures.lettersInDayName(SwitchExpressionFeatures.Day.SUNDAY));
  }

}

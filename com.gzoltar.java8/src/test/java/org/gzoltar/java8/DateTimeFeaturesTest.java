package org.gzoltar.java8;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import org.junit.Test;

public class DateTimeFeaturesTest {

  private final DateTimeFeatures dateTimeFeatures = new DateTimeFeatures();

  @Test
  public void testCalculateAge() {
    LocalDate birthDate = LocalDate.of(2000, 1, 15);
    LocalDate today = LocalDate.of(2026, 7, 13);
    assertEquals(26, dateTimeFeatures.calculateAge(birthDate, today));
  }

  @Test
  public void testDaysBetween_ExclusiveEnd() {
    LocalDate start = LocalDate.of(2026, 1, 1);
    LocalDate end = LocalDate.of(2026, 1, 10);
    // 9 whole days separate the two dates; this exposes the injected FAULT (off-by-one).
    assertEquals(9, dateTimeFeatures.daysBetween(start, end));
  }

  @Test
  public void testIsLeapYear_True() {
    assertTrue(dateTimeFeatures.isLeapYear(LocalDate.of(2024, 3, 1)));
  }

  @Test
  public void testIsLeapYear_FalseCentury() {
    assertFalse(dateTimeFeatures.isLeapYear(LocalDate.of(1900, 3, 1)));
  }

  @Test
  public void testNextWorkingDay_FromFriday() {
    LocalDate friday = LocalDate.of(2026, 7, 10);
    assertEquals(DayOfWeek.MONDAY, dateTimeFeatures.nextWorkingDay(friday).getDayOfWeek());
  }

  @Test
  public void testNextWorkingDay_FromSaturday() {
    LocalDate saturday = LocalDate.of(2026, 7, 11);
    assertEquals(DayOfWeek.MONDAY, dateTimeFeatures.nextWorkingDay(saturday).getDayOfWeek());
  }

}

package org.gzoltar.java8;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Exercises the new java.time API (JSR-310): LocalDate, Period, ChronoUnit and the DayOfWeek
 * enum.
 */
public class DateTimeFeatures {

  public int calculateAge(LocalDate birthDate, LocalDate today) {
    Period period = Period.between(birthDate, today);
    return period.getYears();
  }

  public long daysBetween(LocalDate start, LocalDate end) {
    return ChronoUnit.DAYS.between(start, end) + 1; /* FAULT: should not add 1 */
  }

  public boolean isLeapYear(LocalDate date) {
    int year = date.getYear();
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
  }

  public LocalDate nextWorkingDay(LocalDate date) {
    LocalDate next = date.plusDays(1);
    switch (next.getDayOfWeek()) {
      case SATURDAY:
        return next.plusDays(2);
      case SUNDAY:
        return next.plusDays(1);
      default:
        return next;
    }
  }

}

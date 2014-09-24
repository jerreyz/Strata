/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.basics.schedule;

import static com.opengamma.basics.schedule.Frequency.P1D;
import static com.opengamma.basics.schedule.Frequency.P1W;
import static com.opengamma.basics.schedule.Frequency.P1Y;
import static com.opengamma.basics.schedule.Frequency.P3M;
import static com.opengamma.basics.schedule.Frequency.P6M;
import static com.opengamma.basics.schedule.Frequency.TERM;
import static com.opengamma.collect.TestHelper.assertJodaConvert;
import static com.opengamma.collect.TestHelper.assertSerialization;
import static com.opengamma.collect.TestHelper.assertThrows;
import static java.time.temporal.ChronoUnit.CENTURIES;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.testng.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.UnsupportedTemporalTypeException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.opengamma.basics.schedule.Frequency;

/**
 * Tests for the frequency class.
 */
@Test
public class FrequencyTest {

  @DataProvider(name = "create")
  static Object[][] data_create() {
    return new Object[][] {
        {Frequency.ofDays(1), Period.ofDays(1), "P1D"},
        {Frequency.ofDays(2), Period.ofDays(2), "P2D"},
        {Frequency.ofDays(6), Period.ofDays(6), "P6D"},
        {Frequency.ofDays(7), Period.ofDays(7), "P1W"},
        {Frequency.ofDays(91), Period.ofDays(91), "P13W"},
        {Frequency.ofWeeks(1), Period.ofDays(7), "P1W"},
        {Frequency.ofWeeks(3), Period.ofDays(21), "P3W"},
        {Frequency.ofMonths(8), Period.ofMonths(8), "P8M"},
        {Frequency.ofMonths(12), Period.ofYears(1), "P1Y"},
        {Frequency.ofMonths(18), Period.ofMonths(18), "P18M"},
        {Frequency.ofMonths(24), Period.ofYears(2), "P2Y"},
        {Frequency.of(Period.of(1, 2, 3)), Period.of(1, 2, 3), "P1Y2M3D"},
        {Frequency.P1D, Period.ofDays(1), "P1D"},
        {Frequency.P1W, Period.ofWeeks(1), "P1W"},
        {Frequency.P2W, Period.ofWeeks(2), "P2W"},
        {Frequency.P4W, Period.ofWeeks(4), "P4W"},
        {Frequency.P13W, Period.ofWeeks(13), "P13W"},
        {Frequency.P26W, Period.ofWeeks(26), "P26W"},
        {Frequency.P52W, Period.ofWeeks(52), "P52W"},
        {Frequency.P1M, Period.ofMonths(1), "P1M"},
        {Frequency.P2M, Period.ofMonths(2), "P2M"},
        {Frequency.P3M, Period.ofMonths(3), "P3M"},
        {Frequency.P4M, Period.ofMonths(4), "P4M"},
        {Frequency.P6M, Period.ofMonths(6), "P6M"},
        {Frequency.P1Y, Period.ofYears(1), "P1Y"},
        {Frequency.TERM, Period.ofYears(10000), "Term"},
    };
  }

  @Test(dataProvider = "create")
  public void test_of_int(Frequency test, Period period, String toString) {
    assertEquals(test.getPeriod(), period);
    assertEquals(test.toString(), toString);
    assertEquals(test.isTerm(), test.equals(TERM));
  }

  @Test(dataProvider = "create")
  public void test_of_Period(Frequency test, Period period, String toString) {
    assertEquals(Frequency.of(period), test);
    assertEquals(Frequency.of(period).getPeriod(), period);
  }

  @Test(dataProvider = "create")
  public void test_parse(Frequency test, Period period, String toString) {
    assertEquals(Frequency.parse(toString), test);
    assertEquals(Frequency.parse(toString).getPeriod(), period);
  }

  public void test_of_notZero() {
    assertThrows(() -> Frequency.of(Period.ofDays(0)), IllegalArgumentException.class);
    assertThrows(() -> Frequency.ofDays(0), IllegalArgumentException.class);
    assertThrows(() -> Frequency.ofWeeks(0), IllegalArgumentException.class);
    assertThrows(() -> Frequency.ofMonths(0), IllegalArgumentException.class);
    assertThrows(() -> Frequency.ofYears(0), IllegalArgumentException.class);
  }

  public void test_of_notNegative() {
    assertThrows(() -> Frequency.of(Period.ofDays(-1)), IllegalArgumentException.class);
    assertThrows(() -> Frequency.of(Period.ofMonths(-1)), IllegalArgumentException.class);
    assertThrows(() -> Frequency.of(Period.of(0, -1, -1)), IllegalArgumentException.class);
    assertThrows(() -> Frequency.of(Period.of(0, -1, 1)), IllegalArgumentException.class);
    assertThrows(() -> Frequency.of(Period.of(0, 1, -1)), IllegalArgumentException.class);
    assertThrows(() -> Frequency.ofDays(-1), IllegalArgumentException.class);
    assertThrows(() -> Frequency.ofWeeks(-1), IllegalArgumentException.class);
    assertThrows(() -> Frequency.ofMonths(-1), IllegalArgumentException.class);
    assertThrows(() -> Frequency.ofYears(-1), IllegalArgumentException.class);
  }

  public void test_of_tooBig() {
    assertThrows(() -> Frequency.of(Period.ofMonths(12001)), IllegalArgumentException.class);
    assertThrows(() -> Frequency.of(Period.ofMonths(Integer.MAX_VALUE)), IllegalArgumentException.class);
    
    assertThrows(() -> Frequency.of(Period.ofYears(1001)), IllegalArgumentException.class);
    assertThrows(() -> Frequency.of(Period.ofYears(Integer.MAX_VALUE)), IllegalArgumentException.class);
    
    assertThrows(() -> Frequency.ofMonths(12001), IllegalArgumentException.class);
    assertThrows(() -> Frequency.ofMonths(Integer.MAX_VALUE), IllegalArgumentException.class);
    
    assertThrows(() -> Frequency.ofYears(1001), IllegalArgumentException.class);
    assertThrows(() -> Frequency.ofYears(Integer.MAX_VALUE), IllegalArgumentException.class);
    
    assertThrows(() -> Frequency.of(Period.of(10000, 0, 1)), IllegalArgumentException.class);
  }

  //-------------------------------------------------------------------------
  @DataProvider(name = "based")
  static Object[][] data_based() {
    return new Object[][] {
        {Frequency.ofDays(1), false, false},
        {Frequency.ofDays(2), false, false},
        {Frequency.ofDays(6), false, false},
        {Frequency.ofDays(7), true, false},
        {Frequency.ofWeeks(1), true, false},
        {Frequency.ofWeeks(3), true, false},
        {Frequency.ofMonths(1), false, true},
        {Frequency.ofMonths(3), false, true},
        {Frequency.ofYears(1), false, true},
        {Frequency.ofYears(3), false, true},
        {Frequency.of(Period.of(1, 2, 3)), false, false},
        {Frequency.TERM, false, false},
    };
  }

  @Test(dataProvider = "based")
  public void test_isWeekBased(Frequency test, boolean weekBased, boolean monthBased) {
    assertEquals(test.isWeekBased(), weekBased);
  }

  @Test(dataProvider = "based")
  public void test_isMonthBased(Frequency test, boolean weekBased, boolean monthBased) {
    assertEquals(test.isMonthBased(), monthBased);
  }

  //-------------------------------------------------------------------------
  @DataProvider(name = "events")
  static Object[][] data_events() {
    return new Object[][] {
        {Frequency.P1D, 364},
        {Frequency.P1W, 52},
        {Frequency.P2W, 26},
        {Frequency.P4W, 13},
        {Frequency.P13W, 4},
        {Frequency.P26W, 2},
        {Frequency.P52W, 1},
        {Frequency.P1M, 12},
        {Frequency.P2M, 6},
        {Frequency.P3M, 4},
        {Frequency.P4M, 3},
        {Frequency.P6M, 2},
        {Frequency.P1Y, 1},
        {Frequency.TERM, 0},
    };
  }

  @Test(dataProvider = "events")
  public void test_eventsPerYear(Frequency test, int expected) {
    assertEquals(test.eventsPerYear(), expected);
  }

  public void test_eventsPerYear_bad() {
    assertThrows(() -> Frequency.ofDays(3).eventsPerYear(), IllegalArgumentException.class);
    assertThrows(() -> Frequency.ofWeeks(3).eventsPerYear(), IllegalArgumentException.class);
    assertThrows(() -> Frequency.ofWeeks(104).eventsPerYear(), IllegalArgumentException.class);
    assertThrows(() -> Frequency.ofMonths(5).eventsPerYear(), IllegalArgumentException.class);
    assertThrows(() -> Frequency.ofMonths(24).eventsPerYear(), IllegalArgumentException.class);
  }

  //-------------------------------------------------------------------------
  public void test_parse_String_roundTrip() {
    assertEquals(Frequency.parse(P6M.toString()), P6M);
  }

  @DataProvider(name = "parseGood")
  static Object[][] data_parseGood() {
    return new Object[][] {
        {"1D", Frequency.ofDays(1)},
        {"2D", Frequency.ofDays(2)},
        {"91D", Frequency.ofDays(91)},
        {"2W", Frequency.ofWeeks(2)},
        {"6W", Frequency.ofWeeks(6)},
        {"2M", Frequency.ofMonths(2)},
        {"12M", Frequency.ofYears(1)},
        {"1Y", Frequency.ofYears(1)},
    };
  }

  @Test(dataProvider = "parseGood")
  public void test_parse_String_good_noP(String input, Frequency expected) {
    assertEquals(Frequency.parse(input), expected);
  }

  @Test(dataProvider = "parseGood")
  public void test_parse_String_good_withP(String input, Frequency expected) {
    assertEquals(Frequency.parse("P" + input), expected);
  }

  public void test_parse_String_term() {
    assertEquals(Frequency.parse("Term"), Frequency.TERM);
  }

  @DataProvider(name = "parseBad")
  static Object[][] data_parseBad() {
    return new Object[][] {
      {""},
      {"2"},
      {"2K"},
      {"-2D"},
      {"PTerm"},
      {null},
    };
  }

  @Test(dataProvider = "parseBad", expectedExceptions = IllegalArgumentException.class)
  public void test_parse_String_bad(String input) {
    Frequency.parse(input);
  }

  //-------------------------------------------------------------------------
  public void test_temporalAmount() {
    assertEquals(P3M.getUnits(), ImmutableList.of(YEARS, MONTHS, DAYS));
    assertEquals(P3M.get(MONTHS), 3);
    assertEquals(P1D.addTo(LocalDate.of(2014, 6, 30)), LocalDate.of(2014, 7, 1));
    assertEquals(P1D.subtractFrom(LocalDate.of(2014, 6, 30)), LocalDate.of(2014, 6, 29));
    assertEquals(LocalDate.of(2014, 6, 30).plus(P1W), LocalDate.of(2014, 7, 7));
    assertEquals(LocalDate.of(2014, 6, 30).minus(P1W), LocalDate.of(2014, 6, 23));
    assertThrows(() -> P3M.get(CENTURIES), UnsupportedTemporalTypeException.class);
  }

  //-------------------------------------------------------------------------
  public void test_equals_hashCode() {
    Frequency a1 = P1D;
    Frequency a2 = Frequency.ofDays(1);
    Frequency b = P3M;
    assertEquals(a1.equals(a1), true);
    assertEquals(a1.equals(b), false);
    assertEquals(a1.equals(a2), true);
    
    assertEquals(a2.equals(a1), true);
    assertEquals(a2.equals(a2), true);
    assertEquals(a2.equals(b), false);
    
    assertEquals(b.equals(a1), false);
    assertEquals(b.equals(a2), false);
    assertEquals(b.equals(b), true);
    
    assertEquals(a1.hashCode(), a2.hashCode());
  }

  public void test_equals_bad() {
    assertEquals(P3M.equals(null), false);
    assertEquals(P3M.equals("String"), false);
    assertEquals(P3M.equals(new Object()), false);
  }

  //-----------------------------------------------------------------------
  public void test_serialization() {
    assertSerialization(P1D);
    assertSerialization(P3M);
    assertSerialization(P1Y);
    assertSerialization(TERM);
  }

  public void test_jodaConvert() {
    assertJodaConvert(Frequency.class, P1D);
    assertJodaConvert(Frequency.class, P3M);
    assertJodaConvert(Frequency.class, P1Y);
    assertJodaConvert(Frequency.class, TERM);
  }

}

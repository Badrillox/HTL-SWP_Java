/**
 * Copyright 2010 Sven Diedrichsen
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jollyday.jollyday.src.test.java.de.jollyday.tests;

import de.jollyday.*;
import de.jollyday.tests.base.AbstractCountryTestBase;
import de.jollyday.util.CalendarUtil;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class HolidayDETest extends AbstractCountryTestBase {

    private static final int YEAR = 2010;
    private static final String ISO_CODE = "de";

    private CalendarUtil calendarUtil = new CalendarUtil();

    @Test
    public void testManagerDEStructure() {
        validateCalendarData(ISO_CODE, YEAR);
    }

    @Test
    public void testManagerDEInterval() {
        try {
            HolidayManager instance = HolidayManager.getInstance(HolidayCalendar.GERMANY);
            Set<Holiday> holidays = instance.getHolidays(calendarUtil.create(2010, 10, 1), calendarUtil
                    .create(2011, 1, 31));
            List<LocalDate> expected = Arrays.asList(calendarUtil.create(2010, 12, 25),
                    calendarUtil.create(2010, 12, 26), calendarUtil.create(2010, 10, 3),
                    calendarUtil.create(2011, 1, 1));
            assertEquals("Wrong number of holidays", expected.size(), holidays.size());
            for (LocalDate d : expected) {
                Assert.assertTrue("Expected date " + d + " missing.", calendarUtil.contains(holidays, d));
            }
        } catch (Exception e) {
            Assert.fail("Unexpected error occurred: " + e.getClass().getName() + " - " + e.getMessage());
        }
    }

    @Test
    public void testManagerSameInstance() {
        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.GERMANY);
        try {
            HolidayManager defaultManager = HolidayManager.getInstance();
            HolidayManager germanManager = HolidayManager.getInstance(HolidayCalendar.GERMANY);
            assertEquals("Unexpected manager found", defaultManager, germanManager);
        } catch (Exception e) {
            Assert.fail("Unexpected error occurred: " + e.getClass().getName() + " - " + e.getMessage());
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }

    @Test
    public void testManagerDifferentInstance() {
        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
        try {
            HolidayManager defaultManager = HolidayManager.getInstance();
            HolidayManager germanManager = HolidayManager.getInstance(HolidayCalendar.GERMANY);
            Assert.assertNotSame("Unexpected manager found", defaultManager, germanManager);
        } catch (Exception e) {
            Assert.fail("Unexpected error occurred: " + e.getClass().getName() + " - " + e.getMessage());
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }

    @Test
    public void testSystemLocaleInfluence() {
        Set<Holiday> french = getUsingSystemLocale(Locale.FRANCE);
        Set<Holiday> german = getUsingSystemLocale(Locale.GERMANY);
        assertEquals("Holidays differ.", german, french);
    }

    private Set<Holiday> getUsingSystemLocale(Locale systemLocale) {
        Locale defaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(systemLocale);
            ManagerParameter parameters = ManagerParameters.create(Locale.GERMAN);
            HolidayManager mgr = HolidayManager.getInstance(parameters);
            return mgr.getHolidays(2018);
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }
}

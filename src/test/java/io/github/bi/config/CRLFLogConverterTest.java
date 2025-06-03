package io.github.bi.config;

/*-
 * Money Market Bi - BI Microservice for Money Market Bi deals is part of the Granular Bi System
 * Copyright © 2025 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiElement;

class CRLFLogConverterTest {

    @Test
    void transformShouldReturnInputStringWhenMarkerListIsEmpty() {
        ILoggingEvent event = mock(ILoggingEvent.class);
        when(event.getMarkerList()).thenReturn(null);
        when(event.getLoggerName()).thenReturn("org.hibernate.example.Logger");
        String input = "Test input string";
        CRLFLogConverter converter = new CRLFLogConverter();

        String result = converter.transform(event, input);

        assertEquals(input, result);
    }

    @Test
    void transformShouldReturnInputStringWhenMarkersContainCRLFSafeMarker() {
        ILoggingEvent event = mock(ILoggingEvent.class);
        Marker marker = MarkerFactory.getMarker("CRLF_SAFE");
        List<Marker> markers = Collections.singletonList(marker);
        when(event.getMarkerList()).thenReturn(markers);
        String input = "Test input string";
        CRLFLogConverter converter = new CRLFLogConverter();

        String result = converter.transform(event, input);

        assertEquals(input, result);
    }

    @Test
    void transformShouldReturnInputStringWhenMarkersNotContainCRLFSafeMarker() {
        ILoggingEvent event = mock(ILoggingEvent.class);
        Marker marker = MarkerFactory.getMarker("CRLF_NOT_SAFE");
        List<Marker> markers = Collections.singletonList(marker);
        when(event.getMarkerList()).thenReturn(markers);
        when(event.getLoggerName()).thenReturn("org.hibernate.example.Logger");
        String input = "Test input string";
        CRLFLogConverter converter = new CRLFLogConverter();

        String result = converter.transform(event, input);

        assertEquals(input, result);
    }

    @Test
    void transformShouldReturnInputStringWhenLoggerIsSafe() {
        ILoggingEvent event = mock(ILoggingEvent.class);
        when(event.getLoggerName()).thenReturn("org.hibernate.example.Logger");
        String input = "Test input string";
        CRLFLogConverter converter = new CRLFLogConverter();

        String result = converter.transform(event, input);

        assertEquals(input, result);
    }

    @Test
    void transformShouldReplaceNewlinesAndCarriageReturnsWithUnderscoreWhenMarkersDoNotContainCRLFSafeMarkerAndLoggerIsNotSafe() {
        ILoggingEvent event = mock(ILoggingEvent.class);
        List<Marker> markers = Collections.emptyList();
        when(event.getMarkerList()).thenReturn(markers);
        when(event.getLoggerName()).thenReturn("com.mycompany.myapp.example.Logger");
        String input = "Test\ninput\rstring";
        CRLFLogConverter converter = new CRLFLogConverter();

        String result = converter.transform(event, input);

        assertEquals("Test_input_string", result);
    }

    @Test
    void transformShouldReplaceNewlinesAndCarriageReturnsWithAnsiStringWhenMarkersDoNotContainCRLFSafeMarkerAndLoggerIsNotSafeAndAnsiElementIsNotNull() {
        ILoggingEvent event = mock(ILoggingEvent.class);
        List<Marker> markers = Collections.emptyList();
        when(event.getMarkerList()).thenReturn(markers);
        when(event.getLoggerName()).thenReturn("com.mycompany.myapp.example.Logger");
        String input = "Test\ninput\rstring";
        CRLFLogConverter converter = new CRLFLogConverter();
        converter.setOptionList(List.of("red"));

        String result = converter.transform(event, input);

        assertEquals("Test_input_string", result);
    }

    @Test
    void isLoggerSafeShouldReturnTrueWhenLoggerNameStartsWithSafeLogger() {
        ILoggingEvent event = mock(ILoggingEvent.class);
        when(event.getLoggerName()).thenReturn("org.springframework.boot.autoconfigure.example.Logger");
        CRLFLogConverter converter = new CRLFLogConverter();

        boolean result = converter.isLoggerSafe(event);

        assertTrue(result);
    }

    @Test
    void isLoggerSafeShouldReturnFalseWhenLoggerNameDoesNotStartWithSafeLogger() {
        ILoggingEvent event = mock(ILoggingEvent.class);
        when(event.getLoggerName()).thenReturn("com.mycompany.myapp.example.Logger");
        CRLFLogConverter converter = new CRLFLogConverter();

        boolean result = converter.isLoggerSafe(event);

        assertFalse(result);
    }

    @Test
    void testToAnsiString() {
        CRLFLogConverter cut = new CRLFLogConverter();
        AnsiElement ansiElement = AnsiColor.RED;

        String result = cut.toAnsiString("input", ansiElement);

        assertThat(result).isEqualTo("input");
    }
}

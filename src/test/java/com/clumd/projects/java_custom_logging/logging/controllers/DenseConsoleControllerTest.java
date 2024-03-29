package com.clumd.projects.java_custom_logging.logging.controllers;

import com.clumd.projects.java_custom_logging.logging.api.LoggableData;
import com.clumd.projects.java_custom_logging.logging.common.CustomLevel;
import com.clumd.projects.java_custom_logging.logging.common.ExtendedLogRecord;
import com.clumd.projects.javajson.core.BasicJsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DenseConsoleControllerTest {

    private DenseConsoleController controller;

    private static class CustomLogFormattedObject implements LoggableData {
        @Override
        public String getFormattedLogData() {
            return "text 12 with \n symbols {\" \": true} ";
        }
    }

    private static class NotALevel extends Level {

        protected NotALevel() {
            super("not a level", 10);
        }
    }

    @BeforeEach
    void setup() {
        controller = new DenseConsoleController();
        controller.acceptLogRootRefs(UUID.randomUUID(), "system id", new HashMap<>());
    }

    @Test
    void test_message_format_without_extras_or_colour() {
        String message = "blah";
        String formattedString = controller
                .getFormatter()
                .format(new LogRecord(new NotALevel(), message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", not a level] blah\n"));
    }

    @Test
    void test_jul_level_converted_to_custom_message_format() {
        String message = "blah";
        String formattedString = controller
                .getFormatter()
                .format(new LogRecord(Level.INFO, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", INFO" + CustomLevel.COLOUR_RESET + "] blah"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(Level.FINEST, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", FINEST" + CustomLevel.COLOUR_RESET + "] blah"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(Level.SEVERE, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", SEVERE" + CustomLevel.COLOUR_RESET + "] blah"));
    }

    @Test
    void test_message_format_without_extras() {
        String message = "blah";
        String formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.FAILURE, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", FAILURE" + CustomLevel.COLOUR_RESET + "] blah"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.INFO, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", INFO" + CustomLevel.COLOUR_RESET + "] blah"));

        formattedString = controller
                .getFormatter()
                .format(new LogRecord(CustomLevel.DEBUG, message));

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", DEBUG" + CustomLevel.COLOUR_RESET + "] blah"));
    }

    @Test
    void test_message_format_with_throwables_and_data() {
        Throwable quadNestedThrowable = new Throwable(
                "1st reason",
                new RuntimeException("2nd reason",
                        new IOException("3rd IO",
                                new NullPointerException("4th NPE!")))
        );
        Object[] logParams = new Object[]{
                1337,
                BasicJsonBuilder
                        .getBuilder()
                        .addString("top", "1")
                        .addLong("t.s", 2)
                        .addLong("array[]", 1)
                        .addLong("array[]", 2)
                        .addLong("array[]", 3)
                        .addString("t.second.third.fourth.fifth", "value")
                        .build(),
                null,
                "String",
                new CustomLogFormattedObject()
        };

        String message = "Here is some warning due to the attached";
        LogRecord logRecord = new LogRecord(CustomLevel.WARNING, message);
        logRecord.setThrown(quadNestedThrowable);
        logRecord.setParameters(logParams);

        String formattedString = controller
                .getFormatter()
                .format(logRecord);

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", WARNING" + CustomLevel.COLOUR_RESET + "] Here is some warning due to the attached\n" +
                "Error:  (Throwable) 1st reason\n  "));
        assertTrue(formattedString.contains("\nNested Reason:  (RuntimeException) 2nd reason\n"));
        assertTrue(formattedString.contains("\nNested Reason:  (IOException) 3rd IO\n"));
        assertTrue(formattedString.contains("\nNested Reason:  (NullPointerException) 4th NPE!\n"));
        assertTrue(formattedString.endsWith("""

                Metadata:  <5> item(s)
                { 1337 }
                {"top":"1","t":{"s":2,"second":{<third>}},"array":[1,2,3]}
                { NULL }
                { String }
                { text 12 with\s
                 symbols {" ": true}  }
                """));
    }

    @Test
    void test_message_format_with_tags() {
        String message = "msg";
        ExtendedLogRecord logRecord = new ExtendedLogRecord(CustomLevel.WARNING, message, Set.of("tag1", "tag2"));

        String formattedString = controller
                .getFormatter()
                .format(logRecord);

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", WARNING, [tag2, tag1]" + CustomLevel.COLOUR_RESET + "] msg\n")
                || formattedString.contains(", WARNING, [tag1, tag2]" + CustomLevel.COLOUR_RESET + "] msg\n"));
    }

    @Test
    void test_message_format_with_baked_in_tags() {
        String message = "msg";
        ExtendedLogRecord logRecord = new ExtendedLogRecord(CustomLevel.WARNING, message)
                .referencingBakedInTags(Set.of("baked"));

        String formattedString = controller
                .getFormatter()
                .format(logRecord);

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", WARNING, [baked]" + CustomLevel.COLOUR_RESET + "] msg\n"));
    }

    @Test
    void test_message_format_with_regular_and_baked_in_tags() {
        String message = "msg";
        ExtendedLogRecord logRecord = new ExtendedLogRecord(CustomLevel.WARNING, message, Set.of("tag1"))
                .referencingBakedInTags(Set.of("baked"));

        String formattedString = controller
                .getFormatter()
                .format(logRecord);

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", WARNING, [baked], [tag1]" + CustomLevel.COLOUR_RESET + "] msg\n"));
    }

    @Test
    void test_regular_log_messages_are_loggable() {
        assertTrue(controller.isLoggable(new LogRecord(Level.WARNING, "jul warn")));
        assertTrue(controller.isLoggable(new LogRecord(CustomLevel.WARNING, "custom warn 1")));
        assertTrue(controller.isLoggable(new ExtendedLogRecord(CustomLevel.WARNING, "custom warn 2")));
    }

    @Test
    void test_log_messages_ignored_but_by_other_class() {
        assertTrue(controller.isLoggable(
                new ExtendedLogRecord(CustomLevel.WARNING, "custom warn")
                        .withControllersWhichShouldIgnore(Set.of(ConsoleController.class))
        ));
    }

    @Test
    void test_specific_log_message_should_be_ignored() {
        assertFalse(controller.isLoggable(
                new ExtendedLogRecord(CustomLevel.WARNING, "custom warn")
                        .withControllersWhichShouldIgnore(Set.of(DenseConsoleController.class))
        ));
    }

    @Test
    void test_cannot_override_already_set_formatter() {
        Formatter initiallySetFormatter = controller.getFormatter();
        Formatter imposterFormatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                return null;
            }
        };

        controller.setFormatter(imposterFormatter);
        assertSame(initiallySetFormatter, controller.getFormatter());
    }
}

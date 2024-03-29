package com.clumd.projects.java_custom_logging.logging.controllers;

import com.clumd.projects.java_custom_logging.logging.TestFileUtils;
import com.clumd.projects.java_custom_logging.logging.api.LoggableData;
import com.clumd.projects.java_custom_logging.logging.common.CustomLevel;
import com.clumd.projects.java_custom_logging.logging.common.ExtendedLogRecord;
import com.clumd.projects.javajson.core.BasicJsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DenseFileControllerTest {

    private static final String LOGGING_TEST_PATH = "src/test/resources/logging/testLog.log";

    private DenseFileController controller;
    private Map<Long, String> overriddenThreadNames;

    private LogRecord testLogRecord;

    private static class CustomLogFormattedObject implements LoggableData {
        @Override
        public String getFormattedLogData() {
            return "text 12 with \n symbols {\" \": true} ";
        }
    }

    private static class NullCustomLogFormattedObject implements LoggableData {
        @Override
        public String getFormattedLogData() {
            return null;
        }
    }

    @BeforeEach
    void setup() throws IOException {
        TestFileUtils.makeContainingDirs(LOGGING_TEST_PATH);
        controller = new DenseFileController(LOGGING_TEST_PATH, 1000000, 1, false);
        overriddenThreadNames = new HashMap<>();
        controller.acceptLogRootRefs(UUID.randomUUID(), "system id", overriddenThreadNames);
    }

    @AfterEach
    void tearDown() {
        controller.close();
    }

    @Test
    void test_message_format_without_extras_or_colour() {
        String message = "blah";
        testLogRecord = new LogRecord(Level.INFO, message);
        testLogRecord.setLoggerName("test_message_format_without_extras_or_colour");
        String formattedString = controller
                .getFormatter()
                .format(testLogRecord);

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", INFO test_message_format_without_extras_or_colour]  " + message));
        assertTrue(formattedString.endsWith("\n"));

        testLogRecord = new LogRecord(Level.FINEST, message);
        testLogRecord.setLoggerName("test_message_format_without_extras_or_colour");
        formattedString = controller
                .getFormatter()
                .format(testLogRecord);

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", FINEST test_message_format_without_extras_or_colour]  " + message));
        assertTrue(formattedString.endsWith("\n"));

        testLogRecord = new LogRecord(Level.SEVERE, message);
        testLogRecord.setLoggerName("test_message_format_without_extras_or_colour");
        formattedString = controller
                .getFormatter()
                .format(testLogRecord);

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", SEVERE test_message_format_without_extras_or_colour]  " + message));
        assertTrue(formattedString.endsWith("\n"));
    }

    @Test
    void test_message_format_without_extras() {
        String message = "blah";
        testLogRecord = new LogRecord(CustomLevel.FAILURE, message);
        testLogRecord.setLoggerName("test_message_format_without_extras");
        overriddenThreadNames.put(1L, "some name");
        String formattedString = controller
                .getFormatter()
                .format(testLogRecord);

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", FAILURE test_message_format_without_extras]  " + message));
        assertTrue(formattedString.endsWith("\n"));

        testLogRecord = new LogRecord(CustomLevel.INFO, message);
        testLogRecord.setLoggerName("test_message_format_without_extras");
        formattedString = controller
                .getFormatter()
                .format(testLogRecord);

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", INFO test_message_format_without_extras]  " + message));
        assertTrue(formattedString.endsWith("\n"));

        testLogRecord = new LogRecord(CustomLevel.DEBUG, message);
        testLogRecord.setLoggerName("test_message_format_without_extras");
        formattedString = controller
                .getFormatter()
                .format(testLogRecord);

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", DEBUG test_message_format_without_extras]  " + message));
        assertTrue(formattedString.endsWith("\n"));
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
                new CustomLogFormattedObject(),
                new NullCustomLogFormattedObject()
        };

        String message = "Here is some warning due to the attached";
        LogRecord logRecord = new LogRecord(CustomLevel.WARNING, message);
        logRecord.setThrown(quadNestedThrowable);
        logRecord.setParameters(logParams);
        logRecord.setLoggerName("test_message_format_with_throwables_and_data");

        String formattedString = controller
                .getFormatter()
                .format(logRecord);

        assertTrue(formattedString.startsWith("["));
        assertTrue(formattedString.contains(", WARNING test_message_format_with_throwables_and_data]  " + message));
        assertTrue(formattedString.contains("\nError:  (Throwable) 1st reason\n"));
        assertTrue(formattedString.contains("\nNested Reason:  (RuntimeException) 2nd reason\n"));
        assertTrue(formattedString.contains("\nNested Reason:  (IOException) 3rd IO\n"));
        assertTrue(formattedString.contains("\nNested Reason:  (NullPointerException) 4th NPE!\n"));
        assertTrue(formattedString.contains("\n[1337, {\"top\":\"1\",\"t\":{\"s\":2,\"second\":{<third>}},\"array\":[1,2,3]}, NULL, String, text 12 with    symbols {\" \": true} , NULL]\n"));
        assertTrue(formattedString.endsWith("\n"));
    }

    @Test
    void test_log_actually_written_to_file() throws IOException {
        String message = "blah";
        testLogRecord = new LogRecord(Level.INFO, message);
        testLogRecord.setLoggerName("test_log_actually_written_to_file");
        controller.publish(testLogRecord);

        List<String> fileContents = TestFileUtils.getFileAsStrings(LOGGING_TEST_PATH);

        assertEquals(1, fileContents.size(), 0);
        assertTrue(fileContents.getFirst().startsWith("["));
        assertTrue(fileContents.getFirst().contains(", INFO test_log_actually_written_to_file]  " + message));
        assertTrue(fileContents.getFirst().endsWith("\n"));
    }

    @Test
    void test_log_tags_to_file_with_only_regular() throws IOException {
        String message = "blah";
        testLogRecord = new ExtendedLogRecord(Level.INFO, message, Set.of("tag1", "tag2"));
        testLogRecord.setLoggerName("test_log_tags_to_file_with_only_regular");
        controller.publish(testLogRecord);

        List<String> fileContents = TestFileUtils.getFileAsStrings(LOGGING_TEST_PATH);

        assertEquals(1, fileContents.size(), 0);
        assertTrue(fileContents.getFirst().startsWith("["));
        assertTrue(fileContents.getFirst().contains(", INFO, [tag1, tag2] test_log_tags_to_file_with_only_regular]  " + message) || fileContents.getFirst().contains(", INFO, [tag2, tag1] test_log_tags_to_file_with_only_regular]  " + message) );
        assertTrue(fileContents.getFirst().endsWith("\n"));
    }


    @Test
    void test_log_tags_to_file_with_baked_in_tags() throws IOException {
        String message = "blah";
        testLogRecord = new ExtendedLogRecord(Level.INFO, message).referencingBakedInTags(Set.of("baked"));
        testLogRecord.setLoggerName("test_log_tags_to_file_with_baked_in_tags");
        controller.publish(testLogRecord);

        List<String> fileContents = TestFileUtils.getFileAsStrings(LOGGING_TEST_PATH);

        assertEquals(1, fileContents.size(), 0);
        assertTrue(fileContents.getFirst().startsWith("["));
        assertTrue(fileContents.getFirst().contains(", INFO, [baked] test_log_tags_to_file_with_baked_in_tags]  " + message));
        assertTrue(fileContents.getFirst().endsWith("\n"));
    }

    @Test
    void test_log_tags_to_file_with_regular_and_baked_in_tags() throws IOException {
        String message = "blah";
        testLogRecord = new ExtendedLogRecord(Level.INFO, message, Set.of("tag1")).referencingBakedInTags(Set.of("baked"));
        testLogRecord.setLoggerName("test_log_tags_to_file_with_regular_and_baked_in_tags");
        controller.publish(testLogRecord);

        List<String> fileContents = TestFileUtils.getFileAsStrings(LOGGING_TEST_PATH);

        assertEquals(1, fileContents.size(), 0);
        assertTrue(fileContents.getFirst().startsWith("["));
        assertTrue(fileContents.getFirst().contains(", INFO, [baked], [tag1] test_log_tags_to_file_with_regular_and_baked_in_tags]  " + message));
        assertTrue(fileContents.getFirst().endsWith("\n"));
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
                        .withControllersWhichShouldIgnore(Set.of(FileController.class))
        ));
    }

    @Test
    void test_specific_log_message_should_be_ignored() {
        assertFalse(controller.isLoggable(
                new ExtendedLogRecord(CustomLevel.WARNING, "custom warn")
                        .withControllersWhichShouldIgnore(Set.of(DenseFileController.class))
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

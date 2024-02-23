package com.clumd.projects.java_custom_logging.logging;

import com.clumd.projects.java_custom_logging.logging.common.CustomLevel;
import com.clumd.projects.java_custom_logging.logging.common.ExtendedLogRecord;
import com.clumd.projects.java_custom_logging.logging.controllers.ConsoleController;
import com.clumd.projects.java_custom_logging.logging.controllers.FileController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExtendedLoggerTest {

    private ExtendedLogRecord capturedLogRecord;

    private ExtendedLogger extendedLogger;

    @Test
    void test_LevelString() {
        extendedLogger.log(CustomLevel.TESTING, "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @BeforeEach
    void setup() {
        extendedLogger = new ExtendedLoggerVerification("test.extended.logger");
        extendedLogger.setLevel(Level.ALL);
    }

    @Test
    void test_LevelTagString() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelTagsString() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelSupplier() {
        extendedLogger.log(CustomLevel.TESTING, () -> "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelTagSupplier() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", () -> "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelTagsSupplier() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), () -> "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelStringParam() {
        extendedLogger.log(CustomLevel.TESTING, "msg", 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagStringParam() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", "msg", 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagsStringParam() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), "msg", 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelSupplierParam() {
        extendedLogger.log(CustomLevel.TESTING, () -> "msg", 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagSupplierParam() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", () -> "msg", 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagsSupplierParam() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), () -> "msg", 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelStringParams() {
        extendedLogger.log(CustomLevel.TESTING, "msg", new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagStringParams() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", "msg", new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagsStringParams() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), "msg", new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelSupplierParams() {
        extendedLogger.log(CustomLevel.TESTING, () -> "msg", new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagSupplierParams() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", () -> "msg", new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagsSupplierParams() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), () -> "msg", new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelStringThrow() {
        extendedLogger.log(CustomLevel.TESTING, "msg", new NullPointerException("test"));

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNotNull(capturedLogRecord.getThrown());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelTagStringThrow() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", "msg", new NullPointerException("test"));

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelTagsStringThrow() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), "msg", new NullPointerException("test"));

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelSupplierThrow() {
        extendedLogger.log(CustomLevel.TESTING, () -> "msg", new NullPointerException("test"));

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelTagSupplierThrow() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", () -> "msg", new NullPointerException("test"));

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelTagsSupplierThrow() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), () -> "msg", new NullPointerException("test"));

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_LevelStringThrowParam() {
        extendedLogger.log(CustomLevel.TESTING, "msg", new NullPointerException("test"), 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagStringThrowParam() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", "msg", new NullPointerException("test"), 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagsStringThrowParam() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), "msg", new NullPointerException("test"), 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelSupplierThrowParam() {
        extendedLogger.log(CustomLevel.TESTING, () -> "msg", new NullPointerException("test"), 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagSupplierThrowParam() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", () -> "msg", new NullPointerException("test"), 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelTagsSupplierThrowParam() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), () -> "msg", new NullPointerException("test"), 123);

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(1, capturedLogRecord.getParameters().length, 0);
        assertEquals(123, capturedLogRecord.getParameters()[0]);
    }

    @Test
    void test_LevelStringThrowParams() {
        extendedLogger.log(CustomLevel.TESTING, "msg", new NullPointerException("test"), new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagStringThrowParams() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", "msg", new NullPointerException("test"), new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagsStringThrowParams() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), "msg", new NullPointerException("test"), new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelSupplierThrowParams() {
        extendedLogger.log(CustomLevel.TESTING, () -> "msg", new NullPointerException("test"), new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagSupplierThrowParams() {
        extendedLogger.log(CustomLevel.TESTING, "tag1", () -> "msg", new NullPointerException("test"), new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("tag1"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_LevelTagsSupplierThrowParams() {
        extendedLogger.log(CustomLevel.TESTING, Set.of("tag1", "tag2"), () -> "msg", new NullPointerException("test"), new Object[]{1, 2, 3});

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertTrue(capturedLogRecord.getTags().equals(Set.of("tag1", "tag2")) || capturedLogRecord.getTags().equals(Set.of("tag2", "tag1")));
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNotNull(capturedLogRecord.getParameters());
        assertEquals(3, capturedLogRecord.getParameters().length, 0);
        assertEquals(1, capturedLogRecord.getParameters()[0]);
        assertEquals(2, capturedLogRecord.getParameters()[1]);
        assertEquals(3, capturedLogRecord.getParameters()[2]);
    }

    @Test
    void test_verifyThatBakedInLogsAreAppliedToAMessageWithNoOtherTags() {
        Set<String> expectedTags = Set.of("baked", "in", "tags");
        extendedLogger = new ExtendedLoggerVerification("test.extended.logger", expectedTags);
        extendedLogger.setLevel(Level.ALL);

        extendedLogger.log(CustomLevel.TESTING, "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertEquals(expectedTags, capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_verifyThatBakedInLogsAreAppliedToAMessageWithOtherTags() {
        Set<String> expectedTags = Set.of("baked");
        extendedLogger = new ExtendedLoggerVerification("test.extended.logger", expectedTags);
        extendedLogger.setLevel(Level.ALL);

        extendedLogger.log(CustomLevel.TESTING, Set.of("case-by-case"), "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertEquals(Set.of("case-by-case"), capturedLogRecord.getTags());
        assertEquals(expectedTags, capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_verifyThatClassesWhichShouldIgnoreThisLoggerAreSet() {
        extendedLogger.log(CustomLevel.TESTING, "msg");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());

        extendedLogger.withControllersWhichShouldIgnore(Set.of(ConsoleController.class, FileController.class));
        extendedLogger.log(CustomLevel.TESTING, "msg");

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());

        assertEquals(
                Set.of(ConsoleController.class, FileController.class),
                capturedLogRecord.getControllersWhichShouldDisregardThisMessage()
        );
    }

    @Test
    void test_verifyThatClassesWhichShouldIgnoreThisLoggerMutatesInline() {
        extendedLogger.log(CustomLevel.TESTING, "msg");
        assertNotNull(capturedLogRecord);

        assertEquals(
                extendedLogger,
                extendedLogger.withControllersWhichShouldIgnore(Set.of(ConsoleController.class, FileController.class))
        );
    }

    @Test
    void test_loggingWithSlf4jFormattingPlain() {
        extendedLogger.format(CustomLevel.TESTING, "msg {}, {}, {}", "val1", 2, "val3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg val1, 2, val3", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithSlf4jFormattingPlainMissingValues() {
        extendedLogger.format(CustomLevel.TESTING, "msg {}, {}, {}", "val1");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg val1, {}, {}", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithSlf4jFormattingTags() {
        extendedLogger.format(CustomLevel.TESTING, Set.of("case-by-case"), "msg {}, {}, {}", "val1", 2, "val3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg val1, 2, val3", capturedLogRecord.getMessage());
        assertEquals(Set.of("case-by-case"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithSlf4jFormattingThrowable() {
        extendedLogger.format(CustomLevel.TESTING, new NullPointerException("deliberate"), "msg {}, {}, {}", "val1", 2, "val3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TESTING, capturedLogRecord.getLevel());
        assertEquals("msg val1, 2, val3", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("deliberate", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodDebug() {
        extendedLogger.debug("message");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals("message", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodDebugFormat() {
        extendedLogger.debug("message {}, {}, {}", "v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodDebugFormatThrows() {
        extendedLogger.debug("message {}, {}, {}", new NullPointerException("test"), "v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodDebugTags() {
        extendedLogger.debug(Set.of("testing"), "message");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals("message", capturedLogRecord.getMessage());
        assertEquals(Set.of("testing"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodDebugTagsFormat() {
        extendedLogger.debug(Set.of("testing"), "message {}, {}, {}", "v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertEquals(Set.of("testing"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodDebugTagsFormatThrows() {
        extendedLogger.debug(Set.of("testing"), "message {}, {}, {}", new NullPointerException("test"),"v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.DEBUG, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertEquals(Set.of("testing"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodInfo() {
        extendedLogger.info("message");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals("message", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodInfoFormat() {
        extendedLogger.info("message {}, {}, {}", "v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodInfoFormatThrows() {
        extendedLogger.info("message {}, {}, {}", new NullPointerException("test"), "v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodInfoTags() {
        extendedLogger.info(Set.of("testing"), "message");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals("message", capturedLogRecord.getMessage());
        assertEquals(Set.of("testing"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodInfoTagsFormat() {
        extendedLogger.info(Set.of("testing"), "message {}, {}, {}", "v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertEquals(Set.of("testing"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodInfoTagsFormatThrows() {
        extendedLogger.info(Set.of("testing"), "message {}, {}, {}", new NullPointerException("test"),"v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.INFO, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertEquals(Set.of("testing"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodWarn() {
        extendedLogger.warn("message");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals("message", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodWarnFormat() {
        extendedLogger.warn("message {}, {}, {}", "v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodWarnFormatThrows() {
        extendedLogger.warn("message {}, {}, {}", new NullPointerException("test"), "v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodWarnTags() {
        extendedLogger.warn(Set.of("testing"), "message");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals("message", capturedLogRecord.getMessage());
        assertEquals(Set.of("testing"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodWarnTagsFormat() {
        extendedLogger.warn(Set.of("testing"), "message {}, {}, {}", "v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertEquals(Set.of("testing"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodWarnTagsFormatThrows() {
        extendedLogger.warn(Set.of("testing"), "message {}, {}, {}", new NullPointerException("test"),"v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.WARNING, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertEquals(Set.of("testing"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodError() {
        extendedLogger.error("message");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals("message", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodErrorFormat() {
        extendedLogger.error("message {}, {}, {}", "v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodErrorFormatThrows() {
        extendedLogger.error("message {}, {}, {}", new NullPointerException("test"), "v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodErrorTags() {
        extendedLogger.error(Set.of("testing"), "message");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals("message", capturedLogRecord.getMessage());
        assertEquals(Set.of("testing"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodErrorTagsFormat() {
        extendedLogger.error(Set.of("testing"), "message {}, {}, {}", "v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertEquals(Set.of("testing"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_loggingWithConvenienceMethodErrorTagsFormatThrows() {
        extendedLogger.error(Set.of("testing"), "message {}, {}, {}", new NullPointerException("test"),"v1", 2, "v3");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.ERROR, capturedLogRecord.getLevel());
        assertEquals("message v1, 2, v3", capturedLogRecord.getMessage());
        assertEquals(Set.of("testing"), capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertEquals("test", capturedLogRecord.getThrown().getMessage());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_enterCanDeduceMethodIndependently() {
        extendedLogger.enter();

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(" >> ENTERING code point: com.clumd.projects.java_custom_logging.logging.ExtendedLoggerTest.test_enterCanDeduceMethodIndependently(ExtendedLoggerTest.java:1145)", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_enterMustHaveCodeLocator() {
        assertThrows(IllegalArgumentException.class, () -> extendedLogger.enter(null));
    }

    @Test
    void test_enterLogsWithNoParams() {
        extendedLogger.enter("ExtendedLoggerTest#test_enterLogsWithNoParams");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(" >> ENTERING code point: ExtendedLoggerTest#test_enterLogsWithNoParams", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertEquals(0, capturedLogRecord.getParameters().length, 0);
    }

    @Test
    void test_enterLogsWithParams() {
        extendedLogger.enter("ExtendedLoggerTest#test_enterLogsWithParams", "one", 2, "three");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(" >> ENTERING code point: ExtendedLoggerTest#test_enterLogsWithParams", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertArrayEquals(new Object[]{"one", 2, "three"}, capturedLogRecord.getParameters());
    }

    @Test
    void test_exitCanDeduceMethodIndependently() {
        extendedLogger.exit();

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(" << EXITING code point: com.clumd.projects.java_custom_logging.logging.ExtendedLoggerTest.test_exitCanDeduceMethodIndependently(ExtendedLoggerTest.java:1195)", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_exitMustHaveCodeLocator() {
        assertThrows(IllegalArgumentException.class, () -> extendedLogger.exit(null));
    }

    @Test
    void test_exitLogsWithNoParams() {
        extendedLogger.exit("ExtendedLoggerTest#test_exitLogsWithNoParams");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(" << EXITING code point: ExtendedLoggerTest#test_exitLogsWithNoParams", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertEquals(0, capturedLogRecord.getParameters().length, 0);
    }

    @Test
    void test_exitLogsWithParams() {
        extendedLogger.exit("ExtendedLoggerTest#test_exitLogsWithParams", "one", 2, "three");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.TRACE, capturedLogRecord.getLevel());
        assertEquals(" << EXITING code point: ExtendedLoggerTest#test_exitLogsWithParams", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertArrayEquals(new Object[]{"one", 2, "three"}, capturedLogRecord.getParameters());
    }

    @Test
    void test_hereCanLogIndependently() {
        extendedLogger.here();

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.CRITICAL, capturedLogRecord.getLevel());
        assertEquals("Code-flow has reached this point.", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_hereMustHaveCodeLocator() {
        assertThrows(IllegalArgumentException.class, () -> extendedLogger.here(null));
    }

    @Test
    void test_hereLogsWithLocator() {
        extendedLogger.here("some thing useful to user");

        assertNotNull(capturedLogRecord);

        assertEquals(CustomLevel.CRITICAL, capturedLogRecord.getLevel());
        assertEquals("Code-flow has reached this point: some thing useful to user", capturedLogRecord.getMessage());
        assertNull(capturedLogRecord.getTags());
        assertNull(capturedLogRecord.getBakedInTags());
        assertNull(capturedLogRecord.getControllersWhichShouldDisregardThisMessage());
        assertNull(capturedLogRecord.getThrown());
        assertNull(capturedLogRecord.getParameters());
    }

    @Test
    void test_constructingWithNullBakedInTags() {
        extendedLogger = new ExtendedLoggerVerification("test.extended.logger", Set.of());
        assertNull(extendedLogger.getBakedInTags());
    }

    private class ExtendedLoggerVerification extends ExtendedLogger {
        protected ExtendedLoggerVerification(String name) {
            super(name);
        }
        protected ExtendedLoggerVerification(String name, Set<String> bakedInTags) {
            super(name, bakedInTags);
        }

        @Override
        public void log(LogRecord record) {
            capturedLogRecord = (ExtendedLogRecord) record;
        }
    }
}

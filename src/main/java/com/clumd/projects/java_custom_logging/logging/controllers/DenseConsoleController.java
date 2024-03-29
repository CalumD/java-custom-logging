package com.clumd.projects.java_custom_logging.logging.controllers;

import com.clumd.projects.java_custom_logging.logging.api.CustomLogHandler;
import com.clumd.projects.java_custom_logging.logging.api.LogLevel;
import com.clumd.projects.java_custom_logging.logging.api.LoggableData;
import com.clumd.projects.java_custom_logging.logging.common.CustomLevel;
import com.clumd.projects.java_custom_logging.logging.common.ExtendedLogRecord;
import com.clumd.projects.java_custom_logging.logging.common.Format;
import com.clumd.projects.javajson.api.Json;
import lombok.NonNull;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class DenseConsoleController extends ConsoleHandler implements CustomLogHandler {

    public final SimpleDateFormat denseConsoleDateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public DenseConsoleController() {
        super();
        super.setFormatter(new ConsoleFormat());
        this.setLevel(CustomLevel.ALL);
    }

    @Override
    public void acceptLogRootRefs(@NonNull UUID specificRunID, @NonNull String systemID, @NonNull Map<Long, String> overriddenThreadNames) {
        // Unused params as this console logger is for basic facts only
    }

    @Override
    public boolean isLoggable(LogRecord logRecord) {
        if (logRecord instanceof ExtendedLogRecord elr && elr.getControllersWhichShouldDisregardThisMessage() != null) {
            for (Class<? extends StreamHandler> controllerWhichShouldDisregard : elr.getControllersWhichShouldDisregardThisMessage()) {
                if (controllerWhichShouldDisregard.isAssignableFrom(this.getClass())) {
                    return false;
                }
            }
        }
        return super.isLoggable(logRecord);
    }

    @Override
    public void setFormatter(Formatter newFormatter) throws SecurityException {
        // EXPLICIT DENY OTHER THINGS SETTING OUR FORMATTER - GOSH DARN IT SPRING / EMBEDDED TOMCAT
        if (getFormatter() != null) {
            System.err.println("WARNING: Attempt to reset " + DenseConsoleController.class.getName() + " formatter.");
        }
    }

    private final class ConsoleFormat extends Formatter {

        @Override
        public String format(LogRecord logRecord) {
            if (logRecord.getLevel() instanceof LogLevel logLevel) {
                return formatWithColour(logRecord, logLevel);
            } else {
                Optional<CustomLevel> maybeCustomLevel = CustomLevel.convertJulEquivalent(logRecord.getLevel());
                return maybeCustomLevel.isPresent()
                        ? formatWithColour(logRecord, maybeCustomLevel.get())
                        : formatWithoutColour(logRecord);
            }
        }

        private String formatWithoutColour(LogRecord logRecord) {
            StringBuilder ret = new StringBuilder();

            // Provide meta data info
            ret.append("[");
            formatMetadata(ret, logRecord);
            ret.append("] ");

            // Give the main message to log
            ret.append(logRecord.getMessage());
            ret.append('\n');

            // Check if the log contains an error
            return formatThrowablesAndData(ret, logRecord);
        }

        private String formatWithColour(LogRecord logRecord, LogLevel level) {
            StringBuilder ret = new StringBuilder();

            // Apply colour
            ret.append('[');
            ret.append(level.getLevelFormat());

            // Provide meta data info
            formatMetadata(ret, logRecord);
            ret.append(Format.RESET.getFormatString());
            ret.append("] ");

            // Normalise Colours and give the main message to log
            ret.append(logRecord.getMessage());
            ret.append('\n');

            // Check if the log contains an error
            return formatThrowablesAndData(ret, logRecord);
        }

        private void formatMetadata(StringBuilder ret, LogRecord logRecord) {
            ret.append(denseConsoleDateTimeFormatter.format(logRecord.getMillis()));
            ret.append(", ");
            ret.append(logRecord.getLevel().getName());
            if (logRecord instanceof ExtendedLogRecord elr) {
                if (elr.getBakedInTags() != null) {
                    ret.append(", ").append(elr.getBakedInTags());
                }
                if (elr.getTags() != null) {
                    ret.append(", ").append(elr.getTags());
                }
            }
        }

        private String formatThrowablesAndData(StringBuilder ret, LogRecord logRecord) {
            if (logRecord.getThrown() != null) {
                boolean isTopReason = true;
                Throwable throwable = logRecord.getThrown();
                do {
                    if (isTopReason) {
                        ret.append("Error:  ");
                        isTopReason = false;
                    } else {
                        ret.append("Nested Reason:  ");
                    }
                    ret.append('(').append(throwable.getClass().getSimpleName()).append(") ");
                    ret.append(throwable.getMessage()).append('\n');
                    for (Object stackTraceLine : throwable.getStackTrace()) {
                        ret.append("  ").append(stackTraceLine.toString()).append('\n');
                    }
                    throwable = throwable.getCause();
                } while (throwable != null && throwable != throwable.getCause());
            }

            //check for additional metadata about the log entry.
            if (logRecord.getParameters() != null && logRecord.getParameters().length > 0) {
                ret.append("Metadata:  <").append(logRecord.getParameters().length).append("> item(s)\n");
                for (Object item : logRecord.getParameters()) {
                    switch (item) {
                        case LoggableData loggableData -> 
                                ret.append("{ ").append(loggableData.getFormattedLogData()).append(" }");
                        case Json jsonItem -> 
                                ret.append((jsonItem).asString(2));
                        case null -> 
                                ret.append("{ ").append("NULL").append(" }");
                        default -> 
                                ret.append("{ ").append(item).append(" }");
                    }
                    ret.append('\n');
                }
            }

            return ret.toString();
        }
    }
}

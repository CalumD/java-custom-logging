package com.clumd.projects.java_custom_logging.logging;

import com.clumd.projects.java_custom_logging.logging.api.CustomLogHandler;
import com.clumd.projects.java_custom_logging.logging.common.CustomLevel;
import com.clumd.projects.java_custom_logging.logging.controllers.ConsoleController;
import com.clumd.projects.java_custom_logging.logging.controllers.DenseConsoleController;
import com.clumd.projects.java_custom_logging.logging.controllers.DenseFileController;
import com.clumd.projects.java_custom_logging.logging.controllers.FileController;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * This class is a de-facto root for all Extended Logging functionality.
 * <p>
 * All methods are Static and this class cannot be instantiated.
 * <p>
 * The INIT method MUST be called before this LogRoot can be used to generate Loggers
 */
public final class LogRoot {

    private static final Map<Long, String> OVERRIDDEN_THREAD_NAME_MAPPINGS = new HashMap<>();
    private static final UUID SPECIFIC_RUN_ID = UUID.randomUUID(); // The ID for a specific run, of a specific machine.
    private static final int SINGLE_FILE_LOG_SIZE = 10000000; //~10MB in bytes.
    private static final int LOG_FILE_ROTATIONS = 3; // max files to keep track of before re-writing old logs.
    public static final String TAB = "    ";
    public static final String ANON_THREAD = "Anon/Unknown Thread";

    private static String discardablePackageId;
    private static String loggingRootId;
    private static String staticSystemName;

    private LogRoot() {
        // Don't allow this class to be instantiated. It should be used for static method calls only.
    }

    /**
     * Used to initialise the Log Root with some base parameters.
     *
     * @param discardablePackageIdEndingInDot The package name of the application using the logger, which must end in a
     *                                        'dot' as this is how the package structure in Java is defined. For example
     *                                        'com.x.y.z.' what this will allow is that any logger messages coming from
     *                                        WITHIN your defined package, will be able to truncate the often
     *                                        unnecessary base package definition in each log message.
     * @param loggingRootID                   The ID which we should use as a core prefix, comparable to an 'App
     *                                        reference' for every Log Message created in this application.
     * @param systemID                        The ID of the system running this instance of the program using this
     *                                        logger API.
     * @return A LogRoot instance, which can then be enhanced with instances of Log Handlers.
     */
    public static LogRoot init(
            @NonNull final String discardablePackageIdEndingInDot,
            @NonNull final String loggingRootID,
            final String systemID
    ) {
        LogRoot.discardablePackageId = discardablePackageIdEndingInDot;
        LogRoot.loggingRootId = loggingRootID;
        // Obtain the local system's name to identify its logfile in a distributed system.
        try {
            staticSystemName = systemID == null
                    ? InetAddress
                    .getLocalHost()
                    .toString()
                    .replace("/", "-")
                    .replace("\\\\", "-")
                    : systemID;
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Failed to obtain the local system's name.", e);
        }

        // Remove all parent chaining
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) {
            root.removeHandler(h);
        }
        root = Logger.getLogger(loggingRootId);
        for (Handler h : root.getHandlers()) {
            root.removeHandler(h);
        }
        root.setLevel(CustomLevel.ALL);

        // Return an instance for self reference to the 'withHandlers' method for nice constructor fluency.
        // This method also ensures any default CustomLogControllers used have initialised static variables to reference
        return new LogRoot();
    }

    /**
     * As {@link LogRoot#init(String, String, String)}, but with systemID defaulting to null, which will use some
     * programmatic ID, involving system hostname.
     */
    public static LogRoot init(
            @NonNull final String discardablePackageIdEndingInDot,
            @NonNull final String loggingRootID
    ) {
        return init(discardablePackageIdEndingInDot, loggingRootID, null);
    }

    /**
     * Used to enhance the LogRoot with instances of Log Handler which will be used to capture all LogRecords written
     * during the runtime of the application using this API.
     *
     * @param wantedLogHandlers The Collection of handlers which we want to be given each message in this app.
     */
    public void withHandlers(Collection<CustomLogHandler> wantedLogHandlers) {

        Logger root = Logger.getLogger("");

        // init each wanted handler
        for (CustomLogHandler handler : wantedLogHandlers) {
            handler.acceptLogRootRefs(SPECIFIC_RUN_ID, staticSystemName, OVERRIDDEN_THREAD_NAME_MAPPINGS);
            if (handler instanceof StreamHandler streamHandler) {
                root.addHandler(streamHandler);
            } else {
                throw new IllegalArgumentException("Every custom log controller MUST extend java.util.logging.StreamHandler.");
            }
        }
    }

    /**
     * Create a basic instance of a Console Handler with various defaults set.
     *
     * @param useSpacerLines Used to decide whether we want to add additional spacer lines to messages in the console.
     * @return The instantiated ConsoleHandler instance.
     */
    public static CustomLogHandler basicConsoleHandler(boolean useSpacerLines) {
        return new ConsoleController(useSpacerLines);
    }

    /**
     * Creates a sparse no-fluff Console Handler with dense message output.
     *
     * @return The instantiated ConsoleHandler instance.
     */
    public static CustomLogHandler basicDenseConsoleHandler() {
        return new DenseConsoleController();
    }

    /**
     * Create a basic instance of a File Handler with various defaults set.
     *
     * @param atDir The Directory where the system should write its log files to.
     * @return The instantiated FileHandler instance.
     * @throws IOException Thrown if there was a problem creating or writing to the directory/file you intended.
     */
    public static CustomLogHandler basicFileHandler(@NonNull String atDir) throws IOException {
        Files.createDirectories(new File(atDir).toPath());
        return new FileController(
                atDir + "/" + loggingRootId + "_" + staticSystemName + "_%g.log",
                SINGLE_FILE_LOG_SIZE,
                LOG_FILE_ROTATIONS,
                true
        );
    }

    /**
     * Creates a sparse no-fluff File Handler with dense message output and other defaults set.
     *
     * @param atDir The Directory where the system should write its log files to.
     * @return The instantiated FileHandler instance.
     * @throws IOException Thrown if there was a problem creating or writing to the directory/file you intended.
     */
    public static CustomLogHandler basicDenseFileHandler(@NonNull String atDir) throws IOException {
        Files.createDirectories(new File(atDir).toPath());
        return new DenseFileController(
                atDir + "/" + loggingRootId + "_" + staticSystemName + "_%g.log",
                SINGLE_FILE_LOG_SIZE,
                LOG_FILE_ROTATIONS,
                true
        );
    }

    /**
     * As {@link LogRoot#basicFileHandler(String)} but the directory is defaulted to the CURRENT WORKING DIRECTORY WHEN
     * THE JAVA PROCESS WAS STARTED.
     *
     * @return The instantiated FileHandler instance.
     * @throws IOException Thrown if there was a problem creating or writing to the directory/file you intended.
     */
    public static CustomLogHandler basicFileHandler() throws IOException {
        return basicFileHandler(
                new File(
                        System.getProperty("user.dir")
                ).getAbsolutePath()
        );
    }

    /**
     * As {@link LogRoot#basicDenseFileHandler(String)} but the directory is defaulted to the CURRENT WORKING DIRECTORY WHEN
     * THE JAVA PROCESS WAS STARTED.
     *
     * @return The instantiated FileHandler instance.
     * @throws IOException Thrown if there was a problem creating or writing to the directory/file you intended.
     */
    public static CustomLogHandler basicDenseFileHandler() throws IOException {
        return basicDenseFileHandler(
                new File(
                        System.getProperty("user.dir")
                ).getAbsolutePath()
        );
    }

    /**
     * Used to create a Logger instance by referencing the Class you want the logger for.
     *
     * @param forClass The Class you would like the logger to be created for.
     * @return The instantiated ExtendedLogger
     */
    public static ExtendedLogger createLogger(@NonNull final Class<?> forClass) {
        return createLogger(null, forClass.getName());
    }

    /**
     * Used to create a Logger instance by referencing the Class you want the logger for.
     * This version of construction also takes a Set of Strings, which will be added to the tags section on every log message executed by the
     * returned logger.
     * This approach is targeted for things like distributed trace IDs.
     *
     * @param forClass    The Class you would like the logger to be created for.
     * @param bakedInTags A collection of tags which should be applied to every single log message that the Logger returned by this method  will
     *                    generate.
     * @return The instantiated ExtendedLogger
     */
    public static ExtendedLogger createLogger(@NonNull final Class<?> forClass, @NonNull final Set<String> bakedInTags) {
        return createLogger(null, forClass.getName(), bakedInTags);
    }

    /**
     * Used to create a Logger instance by a string name.
     * <p>
     * If there is some sort of hierarchy in these loggers, such as going into various sub-packages, then these should
     * be dot-separated.
     *
     * @param loggerIdentifier The String name of the class you would like the Logger of.
     * @return The instantiated ExtendedLogger
     */
    public static ExtendedLogger createLogger(@NonNull String loggerIdentifier) {
        return createLogger(null, loggerIdentifier);
    }

    /**
     * Used to create a Logger instance by a string name.
     * <p>
     * If there is some sort of hierarchy in these loggers, such as going into various sub-packages, then these should
     * be dot-separated.
     * <p>
     * This version of construction also takes a Set of Strings, which will be added to the tags section on every log message executed by the
     * returned logger.
     * This approach is targeted for things like distributed trace IDs
     *
     * @param loggerIdentifier The String name of the class you would like the Logger of.
     * @param bakedInTags      A collection of tags which should be applied to every single log message that the Logger returned by this method  will
     *                         generate.
     * @return The instantiated ExtendedLogger
     */
    public static ExtendedLogger createLogger(@NonNull String loggerIdentifier, @NonNull final Set<String> bakedInTags) {
        return createLogger(null, loggerIdentifier, bakedInTags);
    }

    @SuppressWarnings("java:S106") // in the middle of creating a log name, so probably best to user System.err.
    static String buildLogName(final String prefix, @NonNull final String loggerIdentifier) {
        if (LogRoot.loggingRootId == null) {
            System.err.println("Warning, Logging Root ID is not set, have you called the \"LogRoot.init\" method yet? Going to create a really crude default...");
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            /* call LogRoot.init() with a default package of the first class with a calling package in the stack trace
               which is not inside of this utility lib.
               We will then use all but that last two elements of that classes package.
               This is based on the assumption that the first class to call us should be the Main class in the using app.
               And we assume that the app structure is such that the PSVM is inside of only one package.
               So by dropping the last, we drop the class name, and dropping the second last we drop the main enclosing
               folder, leaving us at the root of the using apps source code.
               As mentioned in the above s.err, this is very crude - but then again you should be manually
               calling LogRoot.init() if you want to use this anyway.
            */
            for (int stackTraceIndex = 1; stackTraceIndex < stackTrace.length; stackTraceIndex++) {
                if (!stackTrace[stackTraceIndex].getClassName().startsWith("com.clumd.projects.java_custom_logging.logging.")) {
                    String[] packageStructure = stackTrace[stackTraceIndex].getClassName().split("\\.");
                    StringBuilder discardablePackageIdEndingInDot = new StringBuilder(packageStructure[0]);
                    for (int packageIndex = 1; packageIndex < packageStructure.length - 2; packageIndex++) {
                        discardablePackageIdEndingInDot.append('.').append(packageStructure[packageIndex]);
                    }
                    discardablePackageIdEndingInDot.append('.');
                    LogRoot.init(discardablePackageIdEndingInDot.toString(), "LogRoot");
                    break;
                }
            }
            if (LogRoot.loggingRootId == null) {
                throw new ExceptionInInitializerError("Logging Root ID is not set, have you called the \"LogRoot.init\" method yet?");
            }
        }

        return loggingRootId
                + '.' + (prefix == null ? "" : prefix + ":")
                + (
                loggerIdentifier.startsWith(discardablePackageId)
                        ? loggerIdentifier.substring(discardablePackageId.length())
                        : loggerIdentifier
        );
    }

    /**
     * Used to create a Logger instance by a string name, with a custom prefix.
     * <p>
     * This may be useful for classes who require Multiple instances of the same logger, but for slightly different
     * purposes.
     * <p>
     * If there is some sort of hierarchy in these loggers, such as going into various sub-packages, then these should
     * be dot-separated on the loggerIdentifier, NOT on the prefix.
     *
     * @param prefix           The name of the prefix for this Logger instance.
     * @param loggerIdentifier The String name of the class you would like the Logger of.
     * @return The instantiated ExtendedLogger
     */
    public static ExtendedLogger createLogger(final String prefix, final String loggerIdentifier) {
        return createLogger(prefix, loggerIdentifier, null);
    }

    /**
     * Used to create a Logger instance by a string name, with a custom prefix.
     * <p>
     * This may be useful for classes who require Multiple instances of the same logger, but for slightly different
     * purposes.
     * <p>
     * If there is some sort of hierarchy in these loggers, such as going into various sub-packages, then these should
     * be dot-separated on the loggerIdentifier, NOT on the prefix.
     * <p>
     * This version of construction also takes a Set of Strings, which will be added to the tags section on every log message executed by the
     * returned logger.
     * This approach is targeted for things like distributed trace IDs
     *
     * @param prefix           The name of the prefix for this Logger instance.
     * @param loggerIdentifier The String name of the class you would like the Logger of.
     * @param bakedInTags      Collection of tags which should be applied to every single log message that the Logger returned by this method  will
     *                         generate.
     * @return The instantiated ExtendedLogger
     */
    public static ExtendedLogger createLogger(final String prefix, final String loggerIdentifier, final Set<String> bakedInTags) {
        String loggerName = buildLogName(prefix, loggerIdentifier);
        Logger extLog = LogManager.getLogManager().getLogger(loggerName);
        if (extLog == null) {
            extLog = new ExtendedLogger(loggerName, bakedInTags);
            LogManager.getLogManager().addLogger(extLog);
        } else if (extLog instanceof ExtendedLogger existingExtendedLogger) {

            Set<String> existingBakedTags = existingExtendedLogger.getBakedInTags();

            if ((existingBakedTags == null && bakedInTags != null && !bakedInTags.isEmpty())
                    || (existingBakedTags != null && !existingBakedTags.equals(bakedInTags))
            ) {
                Logger distinctExtLogger = new ExtendedLogger(loggerName, bakedInTags);
                distinctExtLogger.setParent(existingExtendedLogger.getParent());
                extLog = distinctExtLogger;
            }
        }

        if (Thread.currentThread().threadId() > 1) {
            updateThreadIdName(Thread.currentThread().threadId(), Thread.currentThread().getName());
        }

        assert extLog instanceof ExtendedLogger;
        return (ExtendedLogger) extLog;
    }

    /**
     * Used to set the logging level of the indicated branch of package hierarchy.
     * <p>
     * This will be SPECIFIC to loggers created within this API.
     *
     * @param selectedLevel The level you would like to set the indicated branch to.
     * @param viaLogger     The Logger we should use to determine the Branch of logging hierarchy to update the log
     *                      level to.
     */
    public static void setBranchLoggingLevel(@NonNull final CustomLevel selectedLevel, @NonNull final Logger viaLogger) {
        setGivenLoggersToLevel(
                getAllLoggerNames(viaLogger.getName()),
                selectedLevel
        );
    }

    /**
     * Used to set the logging level of the indicated branch of package hierarchy.
     * <p>
     * This will be SPECIFIC to loggers created within this API.
     *
     * @param selectedLevel    The level you would like to set the indicated branch to.
     * @param viaLogIdentifier The Logger reference we should use to determine the Branch of logging hierarchy to update
     *                         the log level to.
     */
    public static void setBranchLoggingLevel(@NonNull final CustomLevel selectedLevel, @NonNull final String viaLogIdentifier) {
        String loggerName = buildLogName(null, viaLogIdentifier);
        setGivenLoggersToLevel(
                getAllLoggerNames(loggerName),
                selectedLevel
        );
    }

    /**
     * Used to set the logging level of the indicated branch of package hierarchy.
     * <p>
     * This operation can apply ACROSS ALL LOGGERS KNOWN TO THE JVM.
     *
     * @param selectedLevel    The level you would like to set the indicated branch to.
     * @param viaLogIdentifier The Logger reference we should use to determine the Branch of logging hierarchy to update
     *                         the log level to.
     */
    public static void setGlobalBranchLoggingLevel(@NonNull final CustomLevel selectedLevel, @NonNull final String viaLogIdentifier) {
        setGivenLoggersToLevel(
                getAllLoggerNames(viaLogIdentifier),
                selectedLevel
        );
    }

    /**
     * Used to set the logging level of the indicated branch of package hierarchy.
     * <p>
     * This will be SPECIFIC to loggers created within this API.
     *
     * @param selectedLevel    The level you would like to set the indicated branch to.
     * @param viaLogPrefix     The specific prefix necessary to determine an exact Logger instance.
     * @param viaLogIdentifier The Logger reference we should use to determine the Branch of logging hierarchy to update
     *                         the log level to.
     */
    public static void setBranchLoggingLevel(@NonNull final CustomLevel selectedLevel, final String viaLogPrefix, @NonNull final String viaLogIdentifier) {
        String loggerName = buildLogName(viaLogPrefix, viaLogIdentifier);
        setGivenLoggersToLevel(
                getAllLoggerNames(loggerName),
                selectedLevel
        );
    }

    /**
     * Set the log level across the entire JVM to the given Custom Level.
     * <p>
     * Please note, CUSTOM log levels DO NOT necessarily overlap with the baked in JUL log levels.
     *
     * @param selectedLevel The LogLevel you would like to set across the whole application.
     */
    public static void setGlobalLoggingLevel(@NonNull final CustomLevel selectedLevel) {
        setGivenLoggersToLevel(
                getAllLoggerNames(null),
                selectedLevel
        );
    }

    /**
     * Set the log level across every Logger created within this API to the given Custom Level, but do NOT touch loggers
     * used by 3rd party dependencies.
     *
     * @param selectedLevel The LogLevel you would like to set for the indicated loggers.
     */
    public static void setApplicationGlobalLevel(@NonNull final CustomLevel selectedLevel) {
        setGivenLoggersToLevel(
                getAllLoggerNames(loggingRootId),
                selectedLevel
        );
    }

    public static void updateThreadIdName(long threadID, @NonNull String threadName) {
        OVERRIDDEN_THREAD_NAME_MAPPINGS.put(threadID, threadName);
    }

    private static List<String> getAllLoggerNames(String filteredBy) {
        List<String> names = Collections.list(
                LogManager
                        .getLogManager()
                        .getLoggerNames()
        );
        if (filteredBy != null) {
            List<String> filteredNames = names
                    .stream()
                    .filter(n -> n.startsWith(filteredBy))
                    .toList();
            if (filteredNames.isEmpty()) {
                // no existing loggers match this filter, so try to create a new logger with this, such that if it will be created later in
                // application lifecycle the level should already be controlled.
                try {
                    createLogger(filteredBy.startsWith(loggingRootId) ? filteredBy.substring(loggingRootId.length() + 1) : filteredBy);
                    return Collections.singletonList(filteredBy);
                } catch (Exception e) {
                    return Collections.emptyList();
                }
            }
            return filteredNames;
        }
        return names;
    }

    private static void setGivenLoggersToLevel(final Collection<String> givenLoggers, final CustomLevel selectedLevel) {
        givenLoggers
                .forEach(logName -> Logger
                        .getLogger(logName)
                        .setLevel(selectedLevel)
                );
    }
}

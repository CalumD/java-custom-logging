package com.clumd.projects.java_custom_logging.logging;

import com.clumd.projects.java_custom_logging.logging.common.ExtendedLogRecord;

import java.util.MissingResourceException;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * This class is a very basic extension of the built-in JUL {@link Logger}, but with an expansive selection of 'log'
 * methods for all the combinations of operations the average user might require.
 * <p>
 * There is also support for the 'tags' paradigm for log messages, explained as part of {@link ExtendedLogRecord}
 * documentation. Other than the method signatures, basically the rest is a pass-through to the underling Logger, but
 * using the aforementioned Extended Log Record in place of basic Log Record.
 */
public class ExtendedLogger extends Logger {

    private final Set<String> bakedInTags;
    private Set<Class<? extends StreamHandler>> controllersIgnoringThisLogger;

    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level and with useParentHandlers set to true.
     *
     * @param name A name for the logger. This should be a dot-separated name and should normally be based on the
     *             package name or class name of the subsystem, such as java.net or javax.swing. It may be null for
     *             anonymous Loggers.
     * @throws MissingResourceException if the resourceBundleName is non-null and no corresponding resource can be
     *                                  found.
     */
    protected ExtendedLogger(final String name) {
        super(name, null);
        this.bakedInTags = null;
    }

    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level and with useParentHandlers set to true.
     *
     * @param name        A name for the logger. This should be a dot-separated name and should normally be based on the
     *                    package name or class name of the subsystem, such as java.net or javax.swing. It may be null for
     *                    anonymous Loggers.
     * @param bakedInTags A collection of tags which should be applied to every single log message that this Logger will generate. This is mostly
     *                    for uses such as distributed computing trace IDs or things such as concrete / unchanging environment variables.
     */
    protected ExtendedLogger(final String name, final Set<String> bakedInTags) {
        super(name, null);
        this.bakedInTags = bakedInTags;
    }

    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level and with useParentHandlers set to true.
     *
     * @param controllersIgnoringThisLogger A collection of
     *                                      {@link com.clumd.projects.java_custom_logging.logging.controllers Log Controllers}
     *                                      (Which must extend a {@link StreamHandler}), which in an ideal implementation
     *                                      will IGNORE all log messages from this logger. This is because by default, all
     *                                      log messages will be published to ALL handlers across our JUL implementation.
     *                                      If you had particular logs (idk, say something silly like passwords) which
     *                                      you WANT to log to 'temporary' console-out, but never to a permanent file-out
     *                                      Log Handler, then you could pass FileController classes into this method, and a
     *                                      well-respecting java-common-utils compatible log controller <i>should</i> ignore
     *                                      messages this Logger will pass through.
     * @return This logger, after having applied these properties. This is to allow for nice method chaining when
     * constructing this logger.
     * Such as LogRoot.createLogger(name, bakedTags).withControllersWhichShouldIgnore(Set.of(FileController.class));
     */
    public ExtendedLogger withControllersWhichShouldIgnore(final Set<Class<? extends StreamHandler>> controllersIgnoringThisLogger) {
        this.controllersIgnoringThisLogger = controllersIgnoringThisLogger;
        return this;
    }

    @Override
    public void log(Level level, String msg) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        doLog(lr);
    }

    public void log(Level level, String tag, String msg) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg, tag)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        doLog(lr);
    }

    public void log(Level level, Set<String> tags, String msg) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg, tags)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        doLog(lr);
    }


    @Override
    public void log(Level level, Supplier<String> msgSupplier) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get())
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        doLog(lr);
    }

    public void log(Level level, String tag, Supplier<String> msgSupplier) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get(), tag)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        doLog(lr);
    }

    public void log(Level level, Set<String> tags, Supplier<String> msgSupplier) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get(), tags)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        doLog(lr);
    }


    @Override
    public void log(Level level, String msg, Object param1) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setParameters(new Object[]{param1});
        doLog(lr);
    }

    public void log(Level level, String tag, String msg, Object param1) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg, tag)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setParameters(new Object[]{param1});
        doLog(lr);
    }

    public void log(Level level, Set<String> tags, String msg, Object param1) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg, tags)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setParameters(new Object[]{param1});
        doLog(lr);
    }

    public void log(Level level, Supplier<String> msgSupplier, Object param1) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get())
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setParameters(new Object[]{param1});
        doLog(lr);
    }

    public void log(Level level, String tag, Supplier<String> msgSupplier, Object param1) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get(), tag)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setParameters(new Object[]{param1});
        doLog(lr);
    }

    public void log(Level level, Set<String> tags, Supplier<String> msgSupplier, Object param1) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get(), tags)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setParameters(new Object[]{param1});
        doLog(lr);
    }


    @Override
    public void log(Level level, String msg, Object[] params) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setParameters(params);
        doLog(lr);
    }

    public void log(Level level, String tag, String msg, Object[] params) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg, tag)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setParameters(params);
        doLog(lr);
    }

    public void log(Level level, Set<String> tags, String msg, Object[] params) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg, tags)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setParameters(params);
        doLog(lr);
    }

    public void log(Level level, Supplier<String> msgSupplier, Object[] params) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get())
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setParameters(params);
        doLog(lr);
    }

    public void log(Level level, String tag, Supplier<String> msgSupplier, Object[] params) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get(), tag)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setParameters(params);
        doLog(lr);
    }

    public void log(Level level, Set<String> tags, Supplier<String> msgSupplier, Object[] params) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get(), tags)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setParameters(params);
        doLog(lr);
    }


    @Override
    public void log(Level level, String msg, Throwable thrown) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        doLog(lr);
    }

    public void log(Level level, String tag, String msg, Throwable thrown) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg, tag)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        doLog(lr);
    }

    public void log(Level level, Set<String> tags, String msg, Throwable thrown) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg, tags)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        doLog(lr);
    }

    public void log(Level level, Supplier<String> msgSupplier, Throwable thrown) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get())
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        doLog(lr);
    }

    public void log(Level level, String tag, Supplier<String> msgSupplier, Throwable thrown) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get(), tag)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        doLog(lr);
    }

    public void log(Level level, Set<String> tags, Supplier<String> msgSupplier, Throwable thrown) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get(), tags)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        doLog(lr);
    }


    public void log(Level level, String msg, Throwable thrown, Object param1) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        lr.setParameters(new Object[]{param1});
        doLog(lr);
    }

    public void log(Level level, String tag, String msg, Throwable thrown, Object param1) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg, tag)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        lr.setParameters(new Object[]{param1});
        doLog(lr);
    }

    public void log(Level level, Set<String> tags, String msg, Throwable thrown, Object param1) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg, tags)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        lr.setParameters(new Object[]{param1});
        doLog(lr);
    }

    public void log(Level level, Supplier<String> msgSupplier, Throwable thrown, Object param1) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get())
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        lr.setParameters(new Object[]{param1});
        doLog(lr);
    }

    public void log(Level level, String tag, Supplier<String> msgSupplier, Throwable thrown, Object param1) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get(), tag)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        lr.setParameters(new Object[]{param1});
        doLog(lr);
    }

    public void log(Level level, Set<String> tags, Supplier<String> msgSupplier, Throwable thrown, Object param1) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get(), tags)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        lr.setParameters(new Object[]{param1});
        doLog(lr);
    }


    public void log(Level level, String msg, Throwable thrown, Object[] params) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        lr.setParameters(params);
        doLog(lr);
    }

    public void log(Level level, String tag, String msg, Throwable thrown, Object[] params) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg, tag)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        lr.setParameters(params);
        doLog(lr);
    }

    public void log(Level level, Set<String> tags, String msg, Throwable thrown, Object[] params) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msg, tags)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        lr.setParameters(params);
        doLog(lr);
    }

    public void log(Level level, Supplier<String> msgSupplier, Throwable thrown, Object[] params) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get())
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        lr.setParameters(params);
        doLog(lr);
    }

    public void log(Level level, String tag, Supplier<String> msgSupplier, Throwable thrown, Object[] params) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get(), tag)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        lr.setParameters(params);
        doLog(lr);
    }

    public void log(Level level, Set<String> tags, Supplier<String> msgSupplier, Throwable thrown, Object[] params) {
        if (!isLoggable(level)) {
            return;
        }

        ExtendedLogRecord lr = new ExtendedLogRecord(level, msgSupplier.get(), tags)
                .referencingBakedInTags(bakedInTags)
                .withControllersWhichShouldIgnore(controllersIgnoringThisLogger);
        lr.setThrown(thrown);
        lr.setParameters(params);
        doLog(lr);
    }


    private void doLog(ExtendedLogRecord elr) {
        elr.setLoggerName(getName());
        log(elr);
    }
}
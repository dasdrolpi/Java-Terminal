package de.drolpi.terminal.client;

import de.natrox.common.Shutdownable;
import de.natrox.common.Startable;
import de.natrox.console.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TerminalClient implements Startable, Shutdownable {

    private final static Logger LOGGER = LoggerFactory.getLogger(TerminalClient.class);

    protected final Console console;

    protected TerminalClient(Console console) {
        this.console = console;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}

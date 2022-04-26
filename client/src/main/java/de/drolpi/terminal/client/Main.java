package de.drolpi.terminal.client;

import de.natrox.console.Console;
import de.natrox.console.jline3.JLine3Console;

public class Main {

    public static void main(String[] args) throws Exception {
        Console console = JLine3Console
            .builder()
            .prompt(() -> "> ")
            .build();

        TerminalClient client = new TerminalClient(console);
        client.start();
    }

}

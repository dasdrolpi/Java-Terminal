/*
 * Copyright 2020-2022 dasdrolpi & gabl22
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

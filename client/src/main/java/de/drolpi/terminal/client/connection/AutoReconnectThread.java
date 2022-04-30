/*
 * Copyright 2022 dasdrolpi & gabl22
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

package de.drolpi.terminal.client.connection;

import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AutoReconnectThread extends Thread {

    private final static Logger LOGGER = LoggerFactory.getLogger(AutoReconnectThread.class);
    private final Client client;

    public AutoReconnectThread(@NotNull Client client) {
        Check.notNull(client, "client");
        this.client = client;
    }

    @Override
    public void run() {
        while (!Thread.interrupted() && this.client.connected()) {
            // currently connected
        }

        try {
            LOGGER.debug("Connecting...");
            this.client.connect();
        } catch (Exception exception) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.run();
    }
}

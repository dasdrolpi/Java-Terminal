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

package de.drolpi.terminal.server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerClientManager extends Thread {

    private final ServerSocket socket;
    private final Map<UUID, ConnectedClient> connectionSet = new HashMap<>();

    public ServerClientManager(int port) throws IOException {
        this.socket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket client = this.socket.accept();
                UUID uniqueId = UUID.randomUUID();
                ConnectedClient connection = new ConnectedClient(client, uniqueId);
                this.connectionSet.put(uniqueId, connection);
            } catch (IOException ignored) {

            }
        }
    }
}

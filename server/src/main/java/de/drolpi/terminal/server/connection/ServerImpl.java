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

import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

final class ServerImpl extends Thread implements Server {

    private final ServerSocket socket;

    private final Map<UUID, ConnectedClient> connectionSet = new HashMap<>();
    private final Map<UUID, ServerReceiveListener> listeners = new HashMap<>();

    public ServerImpl(int port) throws IOException {
        this.socket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket client = this.socket.accept();
                System.out.println("Client accepted");
                UUID uniqueId = UUID.randomUUID();
                ConnectedClient connection = new ConnectedClient(this, client, uniqueId);
                this.connectionSet.put(uniqueId, connection);
            } catch (IOException ignored) {

            }
        }
    }

    @Override
    public void close() {
        this.interrupt();
        try {
            this.socket.close();
            for (ConnectedClient client : this.connectionSet.values()) {
                client.close();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void registerListener(@NotNull UUID uniqueId, @NotNull ServerReceiveListener listener) {
        Check.notNull(uniqueId, "uniqueId");
        Check.notNull(listener, "listener");
        this.listeners.put(uniqueId, listener);
    }

    @Override
    public void unregisterListener(@NotNull UUID uniqueId) {
        Check.notNull(uniqueId, "uniqueId");
        this.listeners.remove(uniqueId);
    }

    @Override
    public @NotNull ServerSocket socket() {
        return this.socket;
    }

    @Override
    public @NotNull Set<UUID> listeners() {
        return listeners.keySet();
    }

    protected void callListeners(ConnectedClient connectedClient, String input) {
        connectedClient.callListeners(input);
        for (ServerReceiveListener listener : this.listeners.values()) {
            listener.accept(connectedClient, input);
        }
    }
}

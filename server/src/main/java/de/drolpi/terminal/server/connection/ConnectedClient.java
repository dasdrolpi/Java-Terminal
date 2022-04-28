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

import de.drolpi.terminal.common.connection.Connection;
import de.drolpi.terminal.common.connection.ConnectionListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ConnectedClient implements Connection {

    private final Socket client;
    private final Map<UUID, ConnectionListener> listeners = new HashMap<>();
    private final UUID uniqueId;

    private PrintWriter out;

    ConnectedClient(Socket client, UUID uniqueId) {
        this.client = client;
        this.uniqueId = uniqueId;
    }

    @Override
    public void establish() throws IOException {
        this.out = new PrintWriter(this.client.getOutputStream(), true);
        ServerInputReceiveThread receiver = new ServerInputReceiveThread(this);
        receiver.start();
    }

    @Override
    public void close() {
        try {
            if (!this.connected())
                return;
            this.out.close();
            this.client.close();
        } catch (Exception ignored) {

        }
    }

    @Override
    public void write(String message) {
        this.out.println(message);
    }

    @Override
    public UUID registerListener(ConnectionListener listener) {
        UUID uniqueId = UUID.randomUUID();
        this.registerListener(uniqueId, listener);
        return uniqueId;
    }

    @Override
    public void registerListener(UUID uniqueId, ConnectionListener listener) {
        this.listeners.put(uniqueId, listener);
    }

    @Override
    public void unregisterListener(UUID uniqueId) {
        this.listeners.remove(uniqueId);
    }

    @Override
    public boolean connected() {
        return this.client.isConnected();
    }

    @Override
    public Set<UUID> listeners() {
        return this.listeners.keySet();
    }

    @Override
    public Socket socket() {
        return this.client;
    }

    public void callHandlers(String input) {
        for (ConnectionListener listener : this.listeners.values()) {
            listener.accept(input);
        }
    }

    public UUID uniqueId() {
        return this.uniqueId;
    }
}

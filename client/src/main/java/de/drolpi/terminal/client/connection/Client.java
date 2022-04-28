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

import de.drolpi.terminal.common.connection.Connection;
import de.drolpi.terminal.common.connection.ConnectionListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Client implements Connection {

    private final String host;
    private final int port;
    private final Map<UUID, ConnectionListener> listeners = new HashMap<>();

    private Socket socket;
    private PrintWriter out;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void establish() throws IOException {
        if (this.connected())
            throw new IllegalStateException("Client connection is already established");
        this.socket = new Socket(this.host, this.port);
        this.out = new PrintWriter(this.socket.getOutputStream());
        ClientInputReceiveThread receiver = new ClientInputReceiveThread(this);
        receiver.start();
    }

    @Override
    public void close() {
        try {
            if (!this.connected())
                return;
            this.out.close();
            this.socket.close();
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
        return this.socket != null && this.socket.isConnected();
    }

    @Override
    public Set<UUID> listeners() {
        return this.listeners.keySet();
    }

    @Override
    public Socket socket() {
        return this.socket;
    }

    protected void callListeners(String input) {
        for (ConnectionListener listener : this.listeners.values()) {
            listener.accept(input);
        }
    }
}

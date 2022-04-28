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

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ClientConnection implements Connection {

    private final String host;
    private final int port;
    private final Map<UUID, ConnectionListener> listeners = new HashMap<>();
    private final String id;
    private Socket socket;

    private ClientConnectionReciever reciever;
    private PrintWriter out;

    public ClientConnection(String host, int port) {
        this.host = host;
        this.port = port;
        id = host+':'+port;
    }

    @Override
    public void write(String s) throws IOException {
        out.println(s);
    }

    @Override
    public void close() {
        try {
            if(connected()) {
                out.close();
                socket.close();
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void establish() throws IOException {
        if (!connected()) {
            socket = new Socket(host, port);
            reciever = new ClientConnectionReciever(this);
            reciever.start();
            out = new PrintWriter(socket.getOutputStream());
        }
    }

    public boolean connected() {
        return socket != null && socket.isConnected();
    }

    @Override
    public void callHandlers(String s) {
        for (ConnectionListener listener : listeners.values())
            listener.accept(s);
    }

    @Override
    public UUID registerHandler(ConnectionListener c) {
        UUID uniqueId = UUID.randomUUID();
        this.registerHandler(uniqueId, c);
        return uniqueId;
    }

    @Override
    public void registerHandler(UUID uniqueId, ConnectionListener c) {
        listeners.put(uniqueId, c);
    }

    @Override
    public void unregisterHandler(UUID uniqueId) {
        listeners.remove(uniqueId);
    }

    @Override
    public Set<UUID> handlers() {
        return null;
    }

    @Override
    public Socket socket() {
        return socket;
    }

    @Override
    public String id() {
        return id;
    }
}

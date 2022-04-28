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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class ServerConnection implements Connection {

    private final Socket client;
    private final Map<UUID, ConnectionListener> listeners = new HashMap<>();
    private final String id;

    private PrintWriter out;
    private ServerConnectionReciever reciever;

    public ServerConnection(Socket client, String id) {
        this.client = client;
        this.id = id;
    }

    public void close() {
        try {
            if(connected()) {
                out.close();
                client.close();
            }
        } catch (Exception ignored) {}
    }

    public void establish() throws IOException {
        this.out = new PrintWriter(client.getOutputStream(), true);
        this.reciever = new ServerConnectionReciever(this);
        reciever.start();
    }

    public void write(String s) throws IOException {
        out.println(s);
    }

    @Override
    public boolean connected() {
        return client.isConnected();
    }

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
        return listeners.keySet();
    }

    @Override
    public Socket socket() {
        return client;
    }

    @Override
    public String id() {
        return id;
    }
}

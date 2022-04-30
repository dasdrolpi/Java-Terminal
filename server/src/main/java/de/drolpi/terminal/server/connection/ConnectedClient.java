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

import de.drolpi.terminal.common.connection.Connectable;
import de.drolpi.terminal.common.connection.ListenerRegistrable;
import de.drolpi.terminal.common.connection.ReceiveListener;
import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class ConnectedClient implements Connectable, ListenerRegistrable<ReceiveListener> {

    private final UUID uniqueId;
    private final Socket client;
    private final PrintWriter out;

    private final Map<UUID, ReceiveListener> listeners = new HashMap<>();

    ConnectedClient(ServerImpl server, Socket client, UUID uniqueId) throws IOException {
        this.client = client;
        this.uniqueId = uniqueId;
        this.out = new PrintWriter(this.client.getOutputStream(), true);
        ServerInputReceiveThread receiver = new ServerInputReceiveThread(server, this);
        receiver.start();
    }

    @Override
    public void close() {
        try {
            if (!this.connected())
                return;
            System.out.println("Close2");
            this.out.close();
            this.client.close();
        } catch (Exception ignored) {

        }
    }

    @Override
    public void write(@NotNull String message) {
        Check.notNull(message, "message");
        this.out.println(message);
    }

    @Override
    public @NotNull UUID registerListener(@NotNull ReceiveListener listener) {
        Check.notNull(listener, "listener");
        UUID uniqueId = UUID.randomUUID();
        this.registerListener(uniqueId, listener);
        return uniqueId;
    }

    @Override
    public void registerListener(@NotNull UUID uniqueId, @NotNull ReceiveListener listener) {
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
    public boolean connected() {
        return this.client.isConnected();
    }

    @Override
    public @NotNull Set<UUID> listeners() {
        return this.listeners.keySet();
    }

    @Override
    public @NotNull Socket socket() {
        return this.client;
    }

    public void callListeners(String input) {
        for (ReceiveListener listener : this.listeners.values()) {
            listener.accept(input);
        }
    }

    public UUID uniqueId() {
        return this.uniqueId;
    }
}

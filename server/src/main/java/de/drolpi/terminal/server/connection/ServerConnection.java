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

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServerConnection implements Connection {

    private final Socket client;
    private final Set<ConnectionListener> listeners = new HashSet<>();

    private DataOutputStream out;
    private ServerConnectionReciever reciever;

    public ServerConnection(Socket client) {
        this.client = client;
    }

    @Override
    public void close() {
        try {
            if(connected()) {
                out.flush();
                out.close();
                client.close();
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void establish() throws IOException {
        this.out = new DataOutputStream(client.getOutputStream());
        this.reciever = new ServerConnectionReciever(this);
        reciever.start();
    }

    @Override @Deprecated
    public void establishNew() throws IOException {}

    @Override
    public void write(String s) throws IOException {
        out.writeUTF(s);
    }

    @Override
    public boolean connected() {
        return false;
    }

    @Override
    public void callListeners(String s) {
        for (ConnectionListener listener : listeners)
            listener.accept(s);
    }

    @Override
    public void registerHandler(ConnectionListener c) {
        listeners.add(c);
    }

    @Override
    public Socket socket() {
        return client;
    }
}

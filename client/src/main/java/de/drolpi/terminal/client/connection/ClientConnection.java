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

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ClientConnection implements Connection {

    private final String host;
    private final int port;
    private final Set<ConnectionListener> listeners = new HashSet<>();

    private Socket socket;
    private DataOutputStream out;

    public ClientConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void write(String s) throws IOException {
        out.writeUTF(s);
    }

    public void close() throws IOException {
        if (connected()) {
            out.flush();
            out.close();
            socket.close();
        }
    }

    public void establishNew() throws IOException {
        establish(true);
    }

    public void establish() throws IOException {
        establish(false);
    }

    private void establish(boolean evenIfConnected) throws IOException {
        if ((connected() && evenIfConnected) || !connected()) {
            socket = new Socket(host, port);
            out = new DataOutputStream(socket.getOutputStream());
        }
    }

    public boolean connected() {
        return socket.isConnected();
    }

    @Override
    public void registerHandler(ConnectionListener c) {
        listeners.add(c);
    }

    public void callListeners(String s) {
        for (ConnectionListener listener : listeners)
            listener.accept(s);
    }

    public Socket socket() {
        return socket;
    }
}

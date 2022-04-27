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
import java.util.HashSet;
import java.util.Set;

public class ClientConnection implements Connection {

    private final String host;
    private final int port;
    private final Set<ConnectionListener> listeners = new HashSet<>();

    private ClientConnectionReciever reciever;
    private Socket socket;
    private BufferedWriter out;

    public ClientConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void write(String s) throws IOException {
        out.write(s);
        out.flush();
    }

    public void close() {
        try {
            if(connected()) {
                out.flush();
                out.close();
                socket.close();
            }
        } catch (Exception ignored) {}
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
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reciever = new ClientConnectionReciever(this);
            reciever.start();
        }
    }

    public boolean connected() {
        return socket != null && socket.isConnected();
    }

    @Override
    public void registerHandler(ConnectionListener c) {
        listeners.add(c);
    }

    @Override
    public void callListeners(String s) {
        for (ConnectionListener listener : listeners)
            listener.accept(s);
    }

    @Override
    public Socket socket() {
        return socket;
    }
}

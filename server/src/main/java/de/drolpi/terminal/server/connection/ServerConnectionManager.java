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

import com.sun.source.doctree.SerialTree;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerConnectionManager extends Thread {

    private final ServerSocket socket;
    private final Map<String, ServerConnection> connectionSet = new HashMap<>();
    private short nextID;

    public ServerConnectionManager(int port) {
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket client = socket.accept();
                String id = getID(100);
                ServerConnection connection = new ServerConnection(client, id);
                connection.establish();
                connectionSet.put(id, connection);
            } catch (IOException ignored) {}
        }
    }

    private String getID(int calls) {
        if(calls < 1)
            throw new StackOverflowError();
        if(connectionSet.containsKey(nextID)) {
            nextID++;
            return getID(calls-1);
        }
        nextID++;
        return String.valueOf(nextID-1);
    }
}

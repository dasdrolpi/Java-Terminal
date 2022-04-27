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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerConnectionReciever extends Thread {

    private final ServerConnection serverConnection;
    private final BufferedReader in;

    public ServerConnectionReciever(ServerConnection serverConnection) throws IOException {
        this.serverConnection = serverConnection;
        this.in = new BufferedReader(new InputStreamReader(serverConnection.socket().getInputStream()));
    }

    @Override
    public void run() {
        String line;
        while (!Thread.interrupted() && (line = read()) != null) {
            serverConnection.callListeners(line);
        }
    }

    private String read() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }
}

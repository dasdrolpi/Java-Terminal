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

import java.io.DataInputStream;
import java.io.IOException;

public class ClientConnectionReciever extends Thread {

    private final ClientConnection clientConnection;
    private final DataInputStream dataInputStream;

    public ClientConnectionReciever(ClientConnection clientConnection) throws IOException {
        this.clientConnection = clientConnection;
        this.dataInputStream = new DataInputStream(clientConnection.socket().getInputStream());
    }


    @Override
    public void run() {
        String line;
        while (!Thread.interrupted() && (line = read()) != null) {
            clientConnection.callListeners(line);
        }
    }

    private String read() {
        try {
            return dataInputStream.readUTF();
        } catch (IOException e) {
            return null;
        }
    }
}

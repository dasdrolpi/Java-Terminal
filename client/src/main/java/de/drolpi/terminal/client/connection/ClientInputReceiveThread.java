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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

final class ClientInputReceiveThread extends Thread {

    private final ClientImpl client;
    private final BufferedReader in;

    ClientInputReceiveThread(ClientImpl client) throws IOException {
        this.client = client;
        this.in = new BufferedReader(new InputStreamReader(client.socket().getInputStream()));
    }

    @Override
    public void run() {
        String input;
        while (!Thread.interrupted() && (input = this.read()) != null) {
            this.client.callListeners(input);
        }
        client.close();
    }

    private String read() {
        try {
            return this.in.readLine();
        } catch (IOException e) {
            return null;
        }
    }
}

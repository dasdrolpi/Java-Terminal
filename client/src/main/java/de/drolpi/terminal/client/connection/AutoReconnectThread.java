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

public final class AutoReconnectThread extends Thread {

    private final String host;
    private final int port;
    private ClientImpl client;
    private int tries;

    public AutoReconnectThread(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        while (this.client != null && this.client.connected()) {
            System.out.println("INFO: Currently connected");
        }
        System.out.println("T");
        tries++;
        try {
            System.out.println("INFO: Trying to reconnect... (" + tries + ")");
            this.client = new ClientImpl(this.host, this.port);
        } catch (Exception exception) {
            try {
                Thread.sleep(1000);
                this.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

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

    public AutoReconnectThread(Client client) {
        this.client = (ClientImpl) client;
        this.host = this.client.host();
        this.port = this.client.port();
    }

    @Override
    public void run() {
        while (!Thread.interrupted() && this.client != null && this.client.connected()) {
            // currently connected
        }

        try {
            System.out.println("Trying to reconnect...");
            this.client = new ClientImpl(this.host, this.port);
        } catch (Exception exception) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.run();
    }
}

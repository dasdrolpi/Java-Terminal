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

package de.drolpi.terminal.server;

import de.drolpi.terminal.server.connection.Server;
import de.natrox.common.scheduler.Scheduler;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class Main {

    public static void main(String[] args) throws IOException {
        Server server = Server.create(8888);
        server.registerListener((connectedClient, input) -> {
            connectedClient.write("Back: " + input);
        });
        server.start();

        Scheduler scheduler = Scheduler.create();

        scheduler
            .buildTask(() -> {
                System.out.println("Close");
                server.close();
                System.exit(-1);
            })
            .delay(Duration.of(10, ChronoUnit.SECONDS))
            .schedule();
    }
}

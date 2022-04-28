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

import de.drolpi.terminal.common.connection.ListenerRegistrable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Set;
import java.util.UUID;

public sealed interface Server extends ListenerRegistrable<ServerReceiveListener> permits ServerImpl {

    static @NotNull Server create(int port) throws IOException {
        return new ServerImpl(port);
    }

    void start();

    @NotNull ServerSocket socket();

    @NotNull Set<UUID> listeners();

}

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

import de.drolpi.terminal.common.connection.Connectable;
import de.drolpi.terminal.common.connection.ListenerRegistrable;
import de.drolpi.terminal.common.connection.ReceiveListener;
import de.natrox.common.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Client extends Connectable, ListenerRegistrable<ReceiveListener> {

    static @NotNull Client create(@NotNull String host, int port) throws IOException {
        Check.notNull(host, "host");
        return new ClientImpl(host, port);
    }

}

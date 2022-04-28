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

package de.drolpi.terminal.launcher;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.jar.JarInputStream;

@SuppressWarnings("ClassCanBeRecord")
final class Launcher {

    private final static String PREFIX = "Launcher » ";
    private final static Consumer<String> INFO = text -> System.out.println(PREFIX + text);
    private final static Consumer<String> ERROR = text -> System.err.println(PREFIX + text);

    private final List<String> arguments;

    Launcher(List<String> arguments) {
        this.arguments = arguments;
    }

    public void start() throws Exception {
        INFO.accept(String.format("Running launcher created with v%s.", getClass().getPackage().getImplementationVersion()));
        INFO.accept("Searching for client versions...");

        final Path appPath;
        boolean devMode = Boolean.getBoolean("client.dev");

        if (devMode) {
            appPath = Path.of("dev", "client.jar");

            if (Files.notExists(appPath)) {
                Files.createDirectories(appPath.getParent());

                throw new IllegalArgumentException("Client is not at the required path for running in dev-mode");
            }
        } else {
            appPath = Path.of("");

            //TODO: Version Updater
        }

        var classLoader = new LauncherClassLoader(appPath.toUri().toURL());

        String mainClass;
        String version;
        try (JarInputStream stream = new JarInputStream(Files.newInputStream(appPath))) {
            var manifest = stream.getManifest();
            mainClass = manifest.getMainAttributes().getValue("Main-Class");
            version = manifest.getMainAttributes().getValue("Implementation-Version");
            Objects.requireNonNull(mainClass, "Unable to resolve main class in client file " + appPath);
        }

        var main = classLoader
            .loadClass(mainClass)
            .getDeclaredMethod("main", String[].class);

        INFO.accept(String.format("Starting client with version v%s.", version));

        main.setAccessible(true);
        main.invoke(null, (Object) arguments.toArray(new String[0]));
    }
}

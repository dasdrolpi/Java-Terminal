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

import net.kyori.indra.git.IndraGitExtension
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.named

fun Project.applyJarMetadata(mainClass: String) {
    if ("jar" in tasks.names) {
        tasks.named<Jar>("jar") {
            manifest.attributes(
                "Main-Class" to mainClass,
                "Implementation-Vendor" to "Terminal",
                "Implementation-Title" to "VersionName",
                "Implementation-Version" to project.version.toString() + "-${shortCommitHash()}",
                "Specification-Version" to project.version.toString() + "-" + System.currentTimeMillis()
            )
            // apply git information to manifest
            git()?.applyVcsInformationToManifest(manifest)
        }
    }
}

fun Project.shortCommitHash(): String {
    return git()?.commit()?.name?.substring(0, 8) ?: "unknown"
}

fun Project.git(): IndraGitExtension? = rootProject.extensions.findByType()

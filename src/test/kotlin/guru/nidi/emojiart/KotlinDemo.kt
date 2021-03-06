/*
 * Copyright © 2017 Stefan Niederhauser (nidin@gmx.ch)
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
package guru.nidi.emojiart

fun main(args: Array<String>) {
    val name = if (args.size > 0) args[0].toLowerCase() else "smile"
    val width = if (args.size > 1) args[1].toInt() else 80
    val back = if (args.size > 2) args[2].toInt(16) else 0

    val image = image(name)
    if (image != null) println(image.convertTo("ansi", width, back))
    else println("'$name' is unknown. Did you mean ${proposals(name, imageList(), 5).joinToString(", ")}?")
}

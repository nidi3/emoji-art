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
package guru.nidi.emojiart;

import java.awt.image.BufferedImage;

import static guru.nidi.emojiart.EmojiArt.*;
import static java.util.stream.Collectors.joining;

public class JavaDemo {
    public static void main(String[] args) {
        String name = args.length > 0 ? args[0].toLowerCase() : "smile";
        int width = args.length > 1 ? Integer.parseInt(args[1]) : 80;
        int back = args.length > 2 ? Integer.parseInt(args[2], 16) : 0;

        BufferedImage image = image(name);
        if (image != null) {
            System.out.println(convertTo(image, "ansi", width, back));
        } else {
            String props = proposals(name, imageList(), 5).stream().collect(joining(", "));
            System.out.println("'" + name + "' is unknown. Did you mean " + props + "?");
        }
    }
}

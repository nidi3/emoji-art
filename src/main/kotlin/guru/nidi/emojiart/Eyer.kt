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

class Eyer {
    @Volatile private var running = false

    fun start() {
        if (!running) {
            running = true
            Thread {
                while (running) {
                    eyes("(o)(o)", 800, 1200)
                    val r = Math.random()
                    when {
                        r < .2 -> bigEyes()
                        r < .4 -> oneEye()
                        else -> blink()
                    }
                }
            }.apply {
                isDaemon = true
                start()
            }
        }
    }

    fun stop() {
        running = false
    }

    private fun bigEyes() {
        eyes("(O)(O)", 300, 500)
        eyes("(o)(o)", 100, 200)
        eyes("(O)(O)", 300, 500)
    }

    private fun oneEye() {
        val r = Math.random()
        val s = when {
            r < .5 -> "(-)(o)(_)(O)"
            else -> "(o)(-)(O)(_)"
        }
        eyes(s.substring(0, 6), 50, 150)
        eyes(s.substring(6, 12), 500, 800)
    }

    private fun blink() {
        val r = Math.random()
        val s = when {
            r < .33 -> "(-)(-)(_)(_)"
            r < .66 -> "(-)(o)(_)(o)"
            else -> "(o)(-)(o)(_)"
        }
        eyes(s.substring(0, 6), 20, 50)
        eyes(s.substring(6, 12), 20, 50)
    }

    private fun eyes(s: String, min: Int, max: Int) {
        print(s + "\u001b[6D")
        System.out.flush()
        sleep(min, max)
    }

    private fun sleep(min: Int, max: Int) {
        try {
            Thread.sleep((Math.random() * (max - min) + min).toLong())
        } catch (e: InterruptedException) {
            //ignore
        }
    }
}
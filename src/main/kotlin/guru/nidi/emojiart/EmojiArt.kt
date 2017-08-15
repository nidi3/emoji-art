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

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStreamReader
import java.net.URL
import javax.imageio.ImageIO
import javax.script.ScriptContext
import javax.script.ScriptEngineManager

private val cl = Thread.currentThread().contextClassLoader!!

private val engine = ScriptEngineManager().getEngineByExtension("js")!!.apply {
    eval(InputStreamReader(cl.getResourceAsStream("img2txt.js")))
    eval("__log=[]; window={}; console={log:function(x){__log.push(x);}};")
}

private val namesFile = File("src/main/resources/image-names.txt")

fun imageList(): List<String> {
    if (namesFile.exists()) return namesFile.readLines()
    val names = cl.getResourceAsStream("image-names.txt")?.let { InputStreamReader(it).readLines() }
    return if (names != null) names else downloadImages()
}

fun image(name: String) = cl.getResourceAsStream("images/$name.png")?.let { ImageIO.read(it) }

fun proposals(name: String, names: List<String>, count: Int) = names.sortedBy { levenshtein(it, name) }.take(count)

fun BufferedImage.convertTo(format: String, width: Int, background: Int): String {
    val buf = ByteArrayOutputStream()
    ImageIO.write(this.alphaBlend(background), "BMP", buf)
    engine.context.setAttribute("data", buf.toByteArray(), ScriptContext.ENGINE_SCOPE)
    return engine.eval("img2txt(data,['-f','$format','-W','$width','-d','none','test.bmp'])") as String
}

private fun levenshtein(s1: String, s2: String): Int {
    val edits = Array(s1.length + 1) { IntArray(s2.length + 1) }
    for (i in 0..s1.length) edits[i][0] = i
    for (i in 1..s2.length) edits[0][i] = i
    for (i in 1..s1.length) {
        for (j in 1..s2.length) {
            val u = if (s1[i - 1] == s2[j - 1]) 0 else 1
            edits[i][j] = Math.min(
                    edits[i - 1][j] + 1,
                    Math.min(edits[i][j - 1] + 1, edits[i - 1][j - 1] + u)
            )
        }
    }
    return edits[s1.length][s2.length]
}

private fun BufferedImage.alphaBlend(back: Int): BufferedImage {
    fun red(c: Int) = (c shr 16) and 0xff
    fun green(c: Int) = (c shr 8) and 0xff
    fun blue(c: Int) = (c shr 0) and 0xff
    fun sat(c: Double) = Math.min(255, c.toInt())

    val out = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    for (x in 0..width - 1) {
        for (y in 0..height - 1) {
            val c = getRGB(x, y)
            val alpha = ((c shr 24) and 0xff) / 256.toDouble()
            val r = sat(red(c) * alpha + red(back) * (1 - alpha))
            val g = sat(green(c) * alpha + green(back) * (1 - alpha))
            val b = sat(blue(c) * alpha + blue(back) * (1 - alpha))
            out.setRGB(x, y, (r shl 16) + (g shl 8) + b)
        }
    }
    return out
}

private fun downloadImages(): List<String> {
    fun URL.toText() = InputStreamReader(openStream()).readText()

    fun URL.toFile(file: File) = openStream().use { input ->
        file.outputStream().use { input.copyTo(it) }
    }

    val html = URL("https://www.webpagefx.com/tools/emoji-cheat-sheet/").toText()
    val names = Regex("""data-src="(.+?)".*?">(.+?)</""").findAll(html).map {
        val url = "https://www.webpagefx.com/tools/emoji-cheat-sheet/${it.groupValues[1]}"
        val name = it.groupValues[2]
        URL(url).toFile(File("src/main/resources/images/$name.png"))
        name
    }.toList()

    namesFile.writeText(names.joinToString("\n"))
    return names
}

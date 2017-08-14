package guru.nidi.emojiart

fun main(args: Array<String>) {
    val name = if (args.size > 0) args[0].toLowerCase() else "smile"
    val width = if (args.size > 1) args[1].toInt() else 80
    val back = if (args.size > 2) args[2].toInt(16) else 0

    val names = init()
    val image = image(name)
    if (image != null) println(image.alphaBlend(back).convertTo("ansi", width))
    else println("'$name' is unknown. Did you mean ${proposals(name, names, 5).joinToString(", ")}?")
}

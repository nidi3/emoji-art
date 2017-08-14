package guru.nidi.emojiart;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

import static guru.nidi.emojiart.EmojiArtKt.*;

public class JavaDemo {
    public static void main(String[] args) {
        String name = args.length > 0 ? args[0].toLowerCase() : "smile";
        int width = args.length > 1 ? Integer.parseInt(args[1]) : 80;
        int back = args.length > 2 ? Integer.parseInt(args[2], 16) : 0;

        List<String> names = init();
        BufferedImage image = image(name);
        if (image != null) {
            final BufferedImage img = alphaBlend(image, back);
            System.out.println(convertTo(img, "ansi", width));
        } else {
            String props = proposals(name, names, 5).stream().collect(Collectors.joining(", "));
            System.out.println("'" + name + "' is unknown. Did you mean " + props + "?");
        }
    }
}

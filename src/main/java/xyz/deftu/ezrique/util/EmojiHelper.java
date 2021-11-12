package xyz.deftu.ezrique.util;

import net.dv8tion.jda.api.entities.Emoji;

public class EmojiHelper {

    public static Emoji fromUnicode(String unicode) {
        unicode = unicode.replace("\\u", "U+");
        return Emoji.fromUnicode(unicode);
    }

}
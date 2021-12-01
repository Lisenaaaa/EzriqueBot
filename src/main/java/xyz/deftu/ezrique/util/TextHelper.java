package xyz.deftu.ezrique.util;

import net.dv8tion.jda.api.entities.Guild;
import xyz.deftu.ezrique.Ezrique;

import java.util.Arrays;
import java.util.List;

public class TextHelper {

    public static String replaceGrammatically(String original, String... input) {
        List<String> inp = Arrays.asList(input);

        StringBuilder replacement = new StringBuilder();
        for (String in : inp) {
            int index = inp.indexOf(in);
            boolean first = index == 0;
            boolean last = index == inp.size() - 1;
            if (!first && !last)
                replacement.append(", ");
            if (!first && last)
                replacement.append("and ");
            replacement.append("`").append(in).append("`");
        }

        return String.format(original, replacement);
    }

    public static String build(Guild guild, String id, String content) {
        return guild.getEmoteById(id).getAsMention() + " " + content;
    }

    public static String buildSuccess(String content) {
        return Ezrique.getInstance().getComponentCreator().createSuccessEmote().getAsMention() + " " + content;
    }

    public static String buildFailure(String content) {
        return Ezrique.getInstance().getComponentCreator().createFailEmote().getAsMention() + " " + content;
    }

    public static String dateTime(long epochSecond) {
        return String.format("<t:%s:f>", epochSecond);
    }

}
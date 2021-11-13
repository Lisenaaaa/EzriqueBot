package xyz.deftu.ezrique.util;

import net.dv8tion.jda.api.entities.Guild;
import xyz.deftu.ezrique.Ezrique;

public class TextHelper {

    public static String build(Guild guild, String id, String content) {
        return guild.getEmoteById(id).getAsMention() + " " + content;
    }

    public static String buildSuccess(String content) {
        return Ezrique.getInstance().getComponentCreator().createSuccessEmote().getAsMention() + " " + content;
    }

    public static String buildFailure(String content) {
        return Ezrique.getInstance().getComponentCreator().createFailEmote().getAsMention() + " " + content;
    }

}
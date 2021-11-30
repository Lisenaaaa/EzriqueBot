package xyz.deftu.ezrique.util;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChannelHelper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm:ss");

    public static String archive(TextChannel channel) {
        MessageHistory history = channel.getHistoryBefore(channel.getLatestMessageId(), 100).complete();
        List<Message> retrieved = new ArrayList<>(history.getRetrievedHistory());
        Collections.reverse(retrieved);

        StringBuilder builder = new StringBuilder();
        for (Message message : retrieved) {
            builder.append("[").append(message.getTimeCreated().format(formatter)).append("] ");
            builder.append(message.getAuthor().getAsTag()).append(": ");
            builder.append(message.getContentDisplay());
            builder.append("\n");
        }

        String content = builder.toString();
        if (content.isEmpty()) {
            return null;
        }

        String url;
        try {
            url = Hastebin.post(content, true);
        } catch (Exception e) {
            e.printStackTrace();
            url = null;
        }

        return url;
    }

}
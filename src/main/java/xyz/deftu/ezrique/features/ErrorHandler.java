package xyz.deftu.ezrique.features;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.util.Hastebin;

public class ErrorHandler {

    private final Ezrique ezrique;
    private final Logger logger = LogManager.getLogger("Ezrique (ErrorHandler)");

    public ErrorHandler(Ezrique ezrique) {
        this.ezrique = ezrique;
    }

    public boolean handle(Throwable throwable) {
        Guild guild = ezrique.getApi().getGuildById(ezrique.getMetadata().getPrimaryGuildId());
        if (guild != null) {
            TextChannel errorLog = guild.getTextChannelById(ezrique.getMetadata().getErrorLogId());
            if (errorLog != null) {
                StringBuilder content = new StringBuilder();

                content.append(throwable.toString());
                StackTraceElement[] stackTrace = throwable.getStackTrace();
                if (stackTrace != null && stackTrace.length > 0) {
                    content.append("\n");
                    for (int i = 0; i < stackTrace.length; i++) {
                        if (i != 0)
                            content.append("\n");
                        content.append("\t");
                        content.append(stackTrace[i].toString());
                    }
                }

                try {
                    errorLog.sendMessage(new MessageBuilder("**An error occurred:** " + Hastebin.post(content.toString(), false)).build()).queue();
                } catch (Exception e) {
                    logger.error("Failed to handle an error.", e);
                    return false;
                }

                return true;
            } else {
                logger.warn("Could not find error log in primary guild.");
                return false;
            }
        } else {
            logger.warn("Could not find primary guild.");
            return false;
        }
    }

}
package xyz.deftu.ezrique.component;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.deftu.ezrique.Ezrique;

public class ComponentCreator {

    private final Ezrique ezrique;
    private final Logger logger;
    private final ShardManager api;

    public ComponentCreator(Ezrique ezrique) {
        this.ezrique = ezrique;
        this.logger = LogManager.getLogger("Ezrique (ComponentCreator)");
        this.logger.info("Initialized component creator.");
        this.api = ezrique.getApi();
    }

    public EmbedBuilder createEmbed() {
        EmbedBuilder value = new EmbedBuilder();
        value.setColor(ezrique.getPrimaryColour());
        value.setFooter("Join my server for support! " + ezrique.getConfigManager().getBot().getSupportInvite(), ezrique.getApi().getShardById(0).getSelfUser().getAvatarUrl());
        return value;
    }

    public Emote createSuccessEmote() {
        return api.getEmoteById(908650243057418240L);
    }

    public Emote createAnimatedSuccessEmote() {
        return api.getEmoteById(908650224845729823L);
    }

    public Emote createFailEmote() {
        return api.getEmoteById(908650256433029140L);
    }

    public Emote createAnimatedFailEmote() {
        return api.getEmoteById(908650271293460491L);
    }

}
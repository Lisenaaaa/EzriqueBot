package xyz.deftu.ezrique.commands.impl.exclusive.testingserver;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.CommandImpl;

import java.time.OffsetDateTime;

public class TestingServerCommand extends CommandImpl {

    public CommandData getData() {
        return new CommandData("testingserver", "Test.");
    }

    public void initialize(Ezrique instance, JDA api) {
        addGuildId(690263476089782428L);
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        event.getJDA().getEventManager().handle(new GuildMemberUpdateBoostTimeEvent(event.getJDA(), -1, event.getMember(), OffsetDateTime.now()));
    }

}
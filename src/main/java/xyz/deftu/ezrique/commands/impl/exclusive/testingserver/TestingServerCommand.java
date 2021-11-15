package xyz.deftu.ezrique.commands.impl.exclusive.testingserver;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.sharding.ShardManager;
import okhttp3.*;
import xyz.deftu.ezrique.Ezrique;
import xyz.deftu.ezrique.commands.CommandImpl;

public class TestingServerCommand extends CommandImpl {

    public CommandData getData() {
        return new CommandData("testingserver", "Test.");
    }

    public void initialize(Ezrique instance, ShardManager api) {
        addGuildId(690263476089782428L); /* Deftu's Bot Testing */
    }

    public void execute(Ezrique instance, SlashCommandEvent event) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://102.129.56.183:4357/github")
                .post(RequestBody.create(MediaType.parse("text/json"), "{\"id\":\"231464321343246123445\"}"))
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body());
            System.out.println(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
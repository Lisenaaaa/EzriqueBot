package xyz.deftu.ezrique;

public class Bootstrap {

    private static Ezrique bot;

    public static void main(String[] args) {
        bot = new Ezrique();
        bot.start();
    }

    public static void restart() {
        bot.kill();
        (bot = new Ezrique()).start();
    }

    public static Ezrique getBot() {
        return bot;
    }

}
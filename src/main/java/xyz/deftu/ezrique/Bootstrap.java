package xyz.deftu.ezrique;

public class Bootstrap {

    private static Ezrique bot;

    public static void main(String[] args) {
        bot = new Ezrique();
        bot.start();

        Runtime.getRuntime().addShutdownHook(new Thread(bot::kill, "Ezrique shutdown"));
    }

    public static void restart() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }

        bot.kill();
        (bot = new Ezrique()).start();
    }

    public static Ezrique getBot() {
        return bot;
    }

}
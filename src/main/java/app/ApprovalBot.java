package app;

import authentication.SymBotAuth;
import clients.SymBotClient;
import configuration.SymConfig;
import configuration.SymConfigLoader;
import listeners.IMListener;
import listeners.RoomListener;
import messaging.IMListenerImpl;
import messaging.RoomListenerImpl;
import services.DatafeedEventsService;

import java.net.URL;

public class ApprovalBot {

    public static ApprovalBot app;

    public static void main(String [] args) {
        app = new ApprovalBot();
        app.start();
    }

    SymBotClient botClient;

    public void start() {
        URL url = getClass().getResource("config.json");
        SymConfigLoader configLoader = new SymConfigLoader();
        SymConfig config = configLoader.loadFromFile(url.getPath());
        SymBotAuth botAuth = new SymBotAuth(config);
        botAuth.authenticate();


        botClient = SymBotClient.initBot(config, botAuth);

        RoomListener roomListenerTest = new RoomListenerImpl(botClient);
        IMListener imListener = new IMListenerImpl(botClient);

        DatafeedEventsService datafeedEventsService = botClient.getDatafeedEventsService();
        datafeedEventsService.addRoomListener(roomListenerTest);
        datafeedEventsService.addIMListener(imListener);

    }

    public SymBotClient getBotClient() {
        return botClient;
    }


}

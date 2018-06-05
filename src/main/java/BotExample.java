import authentication.SymBotAuth;
import clients.SymBotClient;
import configuration.SymConfig;
import configuration.SymConfigLoader;
import listeners.IMListener;
import listeners.RoomListener;
import messaging.IMListenerImpl;
import messaging.RoomListenerImpl;
import model.*;
import services.DatafeedEventsService;

import javax.ws.rs.core.NoContentException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public class BotExample {

    public static void main(String [] args) {
        BotExample app = new BotExample();
    }

    public BotExample() {
        URL url = getClass().getResource("config.json");
        SymConfigLoader configLoader = new SymConfigLoader();
        SymConfig config = configLoader.loadFromFile(url.getPath());
        SymBotAuth botAuth = new SymBotAuth(config);
        botAuth.authenticate();


        SymBotClient botClient = SymBotClient.initBot(config, botAuth);

        RoomListener roomListenerTest = new RoomListenerImpl(botClient);
        IMListener imListener = new IMListenerImpl(botClient);

        DatafeedEventsService datafeedEventsService = botClient.getDatafeedEventsService();
        datafeedEventsService.addRoomListener(roomListenerTest);
        datafeedEventsService.addIMListener(imListener);

        // createRoom(botClient);
    }

    private void createRoom(SymBotClient botClient){
        try {
            UserInfo userInfo = botClient.getUsersClient().getUserFromEmail("commandercheng@gmail.com", true);
            //get user IM and send message
            String IMStreamId = botClient.getStreamsClient().getUserIMStreamId(userInfo.getId());
            OutboundMessage message = new OutboundMessage();
            message.setMessage("test IM");
            botClient.getMessagesClient().sendMessage(IMStreamId,message);

            Room room = new Room();
            room.setName("Angus " + UUID.randomUUID().toString());
            room.setDescription("test");
            room.setDiscoverable(true);
            room.setPublic(true);
            room.setViewHistory(true);

            RoomInfo roomInfo = botClient.getStreamsClient().createRoom(room);
            botClient.getStreamsClient().addMemberToRoom(roomInfo.getRoomSystemInfo().getId(),userInfo.getId());

            Room newRoomInfo = new Room();
            newRoomInfo.setName("test generator " + UUID.randomUUID().toString());
            botClient.getStreamsClient().updateRoom(roomInfo.getRoomSystemInfo().getId(),newRoomInfo);

            List<RoomMember> members =  botClient.getStreamsClient().getRoomMembers(roomInfo.getRoomSystemInfo().getId());

            botClient.getStreamsClient().promoteUserToOwner(roomInfo.getRoomSystemInfo().getId(), userInfo.getId());

            botClient.getStreamsClient().deactivateRoom(roomInfo.getRoomSystemInfo().getId());
        } catch (NoContentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

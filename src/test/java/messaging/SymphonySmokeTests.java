package messaging;

import authentication.SymBotAuth;
import clients.SymBotClient;
import configuration.SymConfig;
import configuration.SymConfigLoader;
import exceptions.SymClientException;
import model.Room;
import model.RoomInfo;
import model.UserInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import services.DatafeedEventsService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.URL;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by nerd-they-nerd-jack on 3/6/2018.
 */
@Ignore
public class SymphonySmokeTests {

    private static SymBotAuth botAuth;
    private static SymBotClient botClient;

    @Before
    public void authenticate() {
        if (botAuth == null) {
            URL url = getClass().getResource("config.json");
            SymConfigLoader configLoader = new SymConfigLoader();
            SymConfig config = configLoader.loadFromFile(url.getPath());
            botAuth = new SymBotAuth(config);
            botAuth.authenticate();
            botClient = SymBotClient.initBot(config, botAuth);
        }
    }

    @Test
    public void can_authenticate() throws Exception {
        assertNotNull(botAuth.getKmToken());
        assertNotNull(botAuth.getSessionToken());
    }

    @Test
    public void can_find_a_user() throws Exception {
        UserInfo userInfo = botClient.getUsersClient().getUserFromEmail("commandercheng@gmail.com", true);
        assertEquals(userInfo.getDisplayName(), "Angus Cheng");
    }

    @Test
    public void can_create_a_room() throws Exception {
        RoomInfo roomInfo = createRoom();
        assertEquals(roomInfo.getRoomAttributes().getDescription(), "test");
    }

    @Test
    public void can_create_a_room_and_add_angus_in() throws Exception {
        RoomInfo roomInfo = createRoom();
        UserInfo userInfo = botClient.getUsersClient().getUserFromEmail("commandercheng@gmail.com", true);
        botClient.getStreamsClient().addMemberToRoom(roomInfo.getRoomSystemInfo().getId(), userInfo.getId());
    }

    @Ignore
    @Test
    public void can_listen_to_rooms_being_created() throws Exception {
        DatafeedEventsService datafeedEventsService = botClient.getDatafeedEventsService();
        TestRoomListener roomListener = new TestRoomListener();
        datafeedEventsService.addRoomListener(roomListener);

        Assert.assertEquals(roomListener.roomCreated, false);
        createRoom();
        Assert.assertEquals(roomListener.roomCreated, true);
    }

    @Ignore
    @Test
    public void can_listen_to_messages() throws Exception {
        throw new NotImplementedException();
    }


    RoomInfo createRoom() throws SymClientException {
        Room room = new Room();
        room.setName("Angus " + UUID.randomUUID().toString());
        room.setDescription("test");
        room.setDiscoverable(true);
        room.setPublic(true);
        room.setViewHistory(true);

        return botClient.getStreamsClient().createRoom(room);
    }
}

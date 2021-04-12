package com.example.demo.service;

import com.example.demo.configs.MyUserDetailsService;
import com.example.demo.entities.Favorite;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    /*
        Detta är vår controller!
     */

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private UserRepo userRepo;

    private String sverigesRadioApi = "http://api.sr.se/api/v2/";
    private String jsonFormatPagiFalse = "/?format=json&pagination=false";
    private String jsonFormat2 = "&format=json&pagination=false";

    private LocalDate date = LocalDate.now();
    private String jsonFormat3 = "&fromdate=" + date + "&todate=" + date + "&pagination=false&format=json";

    private String today = "&date=" + date + "&pagination=false&format=json";
    private String tomorrow = "&date=" + date.plusDays(1) + "&pagination=false&format=json";
    private String dayAfterTomorrow = "&date=" + date.plusDays(2) + "&pagination=false&format=json";

    // Används för att hämta ALLT
    public List<Map> getAllOptions(String pathOption, String responseOption){
        RestTemplate template = new RestTemplate();

        Map response = template.getForObject(sverigesRadioApi + pathOption + jsonFormatPagiFalse, Map.class);

        List<Map> contentMap = (List<Map>) response.get(responseOption);

        switch (pathOption) {
            case "channels":
                return getAllChannels(contentMap);
            case "programcategories":
                return getAllCategories(contentMap);
        }

        return null;
    }

    // Används för att hämta genom ID
    public List<Map> getAllOptionsById(String pathOption, String responseOption, int id){
        RestTemplate template = new RestTemplate();
        Map response = null;

        // If sats för att ha datum till favoriteBroadcasts
        if(pathOption.equals("scheduledepisodes?channelid=")){
            response = template.getForObject(sverigesRadioApi + pathOption + id + today, Map.class);

        }else{
            response = template.getForObject(sverigesRadioApi + pathOption + id + jsonFormat2, Map.class);
        }

        List<Map> contentMap = (List<Map>) response.get(responseOption);

        switch (pathOption) {
            case "programs/index?channelid=":
                return getProgramsByChannelId(contentMap);
            case "programs/index?programcategoryid=":
                return getProgramsByCategoryId(contentMap);
            case "broadcasts?programid=":
                return getProgramBroadcasts(contentMap);
            case "scheduledepisodes?channelid=":
                return getAllBroadcasts(contentMap);
        }

        return null;
    }

    public Map getDescriptionById(int id) {

        // Hämtar ut ett objekt från API:t.

        RestTemplate restTemplate = new RestTemplate();

        Map response = restTemplate.getForObject(sverigesRadioApi + "programs/" + id + "?format=json", Map.class);

        Map program = (Map) response.get("program");

        Map description = Map.of(
            "description" , program.get("description"),
            "broadcastinfo", program.get("broadcastinfo") != null ? program.get("broadcastinfo") : ""
        );

        return description;
    }

    private List<Map> getAllChannels(List<Map> contentMap){
        List<Map> channelList = new ArrayList<>();

        for(Map channelItem : contentMap){

            Map channelContent = Map.of(
                "id", channelItem.get("id"),
                "name", channelItem.get("name"),
                "image", channelItem.get("image") != null ? channelItem.get("image") : "https://static-cdn.sr.se/images/2386/d05d0580-43ed-48ef-991b-01b536e03b33.jpg?preset=api-default-square",
                "tagline", channelItem.get("tagline"),
                "scheduleurl", channelItem.get("scheduleurl") != null ? channelItem.get("scheduleurl") : "",
                "siteurl", channelItem.get("siteurl")
            );
            channelList.add(channelContent);
        }

        return channelList;
    }

    private List<Map> getAllCategories(List<Map> contentMap){
        List<Map> categoryList = new ArrayList<>();

        for(Map categoryItem : contentMap){

            Map categoryContent = Map.of(
                "id" , categoryItem.get("id"),
                "name", categoryItem.get("name")
            );
            categoryList.add(categoryContent);
        }

        return categoryList;
    }
    //----------------------------------------------------------------
    private List<Map> getAllBroadcasts(List<Map> contentMap) {
        List<Map> broadcastList = new ArrayList<>();

        // Formaterar Json-Date till en String
        DateTimeFormatter jsonDateFormatter = new DateTimeFormatterBuilder()
                .appendLiteral("/Date(")
                .appendValue(ChronoField.INSTANT_SECONDS)
                .appendValue(ChronoField.MILLI_OF_SECOND, 3)
                .appendLiteral(")/")
                .toFormatter();

        // list + objekt
        for(Map broadcastItem : contentMap){

            String starttimeutc = (String) broadcastItem.get("starttimeutc");
            Instant date = jsonDateFormatter.parse(starttimeutc, Instant::from);
            String formattedDate = date.toString();

            Map programs = (Map)broadcastItem.get("program");
                int programId = (int)programs.get("id");

            Map episodeContent = Map.of(
                    "id", programId,
                    "title", broadcastItem.get("title"),
                    "description", broadcastItem.get("description"),
                    "starttimeutc", formattedDate.replace("T"," ").replace("Z","")
            );

            broadcastList.add(episodeContent);
        }

        return broadcastList;
    }

    public List<Map> getAllBroadcastsTomorrow(int id) {

        RestTemplate template = new RestTemplate();
        Map response = template.getForObject(sverigesRadioApi + "scheduledepisodes?channelid=" + id + tomorrow, Map.class);

        List<Map> contentMap = (List<Map>) response.get("schedule");
        List<Map> broadcastList = new ArrayList<>();

        // Formaterar Json-Date till en String
        DateTimeFormatter jsonDateFormatter = new DateTimeFormatterBuilder()
                .appendLiteral("/Date(")
                .appendValue(ChronoField.INSTANT_SECONDS)
                .appendValue(ChronoField.MILLI_OF_SECOND, 3)
                .appendLiteral(")/")
                .toFormatter();

        // list + objekt
        for(Map broadcastItem : contentMap){

            String starttimeutc = (String) broadcastItem.get("starttimeutc");
            Instant date = jsonDateFormatter.parse(starttimeutc, Instant::from);
            String formattedDate = date.toString();

            Map programs = (Map)broadcastItem.get("program");
            int programId = (int)programs.get("id");

            Map episodeContent = Map.of(
                    "id", programId,
                    "title", broadcastItem.get("title"),
                    "description", broadcastItem.get("description"),
                    "starttimeutc", formattedDate.replace("T"," ").replace("Z","")
            );

            broadcastList.add(episodeContent);
        }

        return broadcastList;
    }

    public List<Map> getAllBroadcastsdayAfterTomorrow(int id) {

        RestTemplate template = new RestTemplate();
        Map response = template.getForObject(sverigesRadioApi + "scheduledepisodes?channelid=" + id + dayAfterTomorrow, Map.class);

        List<Map> contentMap = (List<Map>) response.get("schedule");
        List<Map> broadcastList = new ArrayList<>();

        // Formaterar Json-Date till en String
        DateTimeFormatter jsonDateFormatter = new DateTimeFormatterBuilder()
                .appendLiteral("/Date(")
                .appendValue(ChronoField.INSTANT_SECONDS)
                .appendValue(ChronoField.MILLI_OF_SECOND, 3)
                .appendLiteral(")/")
                .toFormatter();

        // list + objekt
        for(Map broadcastItem : contentMap){

            String starttimeutc = (String) broadcastItem.get("starttimeutc");
            Instant date = jsonDateFormatter.parse(starttimeutc, Instant::from);
            String formattedDate = date.toString();

            Map programs = (Map)broadcastItem.get("program");
            int programId = (int)programs.get("id");

            Map episodeContent = Map.of(
                    "id", programId,
                    "title", broadcastItem.get("title"),
                    "description", broadcastItem.get("description"),
                    "starttimeutc", formattedDate.replace("T"," ").replace("Z","")
            );

            broadcastList.add(episodeContent);
        }

        return broadcastList;
    }
   // ----------------------------------------------------------------
    private List<Map> getProgramsByChannelId(List<Map> contentMap) {

        List<Map> programList = new ArrayList<>();

        for(Map programItem : contentMap){

            Map programContent = Map.of(
                "id" , programItem.get("id"),
                "name", programItem.get("name"),
                "programimage", programItem.get("programimage"),
                "programurl", programItem.get("programurl"),
                "description", programItem.get("description"),
                "responsibleeditor", programItem.get("responsibleeditor") != null ? programItem.get("responsibleeditor") : ""
            );

            programList.add(programContent);
        }

        return programList;

    }

    private List<Map> getProgramsByCategoryId(List<Map> contentMap) {

        List<Map> programList = new ArrayList<>();

        for(Map programItem : contentMap){

            Map programContent = Map.of(
                    "id" , programItem.get("id"),
                    "name", programItem.get("name"),
                    "programimage", programItem.get("programimage"),
                    "programurl", programItem.get("programurl"),
                    "description", programItem.get("description"),
                    "responsibleeditor", programItem.get("responsibleeditor") != null ? programItem.get("responsibleeditor") : ""
            );
            programList.add(programContent);
        }

        return programList;

    }

    public List<Map> searchProgram(String input) {
        List<Map> programList = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();

        Map response = restTemplate.getForObject(sverigesRadioApi + "programs" + jsonFormatPagiFalse, Map.class);

        List<Map> contentMap = (List<Map>) response.get("programs");

        for(Map programItem : contentMap){
            String name = (String) programItem.get("name"); // Filtrerar genom program-namn
            String description = (String) programItem.get("description"); // Filtrerar genom program-beskrivning

            if(name.toLowerCase().contains(input.toLowerCase()) || description.toLowerCase().contains(input.toLowerCase())){
                Map programContent = Map.of(
                    "id",programItem.get("id"),
                    "name",programItem.get("name"),
                    "programimage",programItem.get("programimage"),
                    "programurl",programItem.get("programurl"),
                    "description", programItem.get("description"),
                    "responsibleeditor",programItem.get("responsibleeditor") != null ? programItem.get("responsibleeditor") : ""
                );

                programList.add(programContent);
            }
        }
        return programList;
    }

    public List<Map> getProgramBroadcasts(List<Map> contentMap) {
        List<Map> broadcastList = new ArrayList<>();

        // Formaterar Json-Date till en String
        DateTimeFormatter jsonDateFormatter = new DateTimeFormatterBuilder()
                .appendLiteral("/Date(")
                .appendValue(ChronoField.INSTANT_SECONDS)
                .appendValue(ChronoField.MILLI_OF_SECOND, 3)
                .appendLiteral(")/")
                .toFormatter();

        for(Map broadcastItem : contentMap){

            String broadcastDateUtc = (String)broadcastItem.get("broadcastdateutc");
            Instant date = jsonDateFormatter.parse(broadcastDateUtc, Instant::from);

            String formattedDate = date.toString();

            int seconds = (int)broadcastItem.get("totalduration");
            int p1 = seconds % 60;
            int p2 = seconds / 60;
            int p3 = p2 % 60;
            p2 = p2 /60;

            String formattedSeconds = "";

            if(p1 != 0 || p2 != 0){
                formattedSeconds = p2 + "t " + p3 + "m " + p1 + "s";
            }else if(p2 == 0 && p1 == 0){
                formattedSeconds = p3 + "m";
            }

            String url = "";

            List<Map> broadcastFiles = (List<Map>)broadcastItem.get("broadcastfiles");
            for(Map broadcastFile : broadcastFiles){
                url = (String)broadcastFile.get("url");
            }

            Map broadcastContent = Map.of(
                    "id", broadcastItem.get("id"),
                    "title", broadcastItem.get("title"),
                    "broadcastdateutc", formattedDate.replace("T"," ").replace("Z",""),
                    "totalduration", formattedSeconds,
                    "image", broadcastItem.get("image"),
                    "url", url

            );

            broadcastList.add(broadcastContent);
        }

        return broadcastList;
    }

    public List<Map> getFavoriteBroadcasts(String pathOption, String responseOption, int id){

        RestTemplate template = new RestTemplate();
        Map response = template.getForObject(sverigesRadioApi + pathOption + id + jsonFormat3, Map.class);

        List<Map> contentMap = (List<Map>) response.get(responseOption);

        List<Map> broadcastList = new ArrayList<>();

        // Formaterar Json-Date till en String
        DateTimeFormatter jsonDateFormatter = new DateTimeFormatterBuilder()
                .appendLiteral("/Date(")
                .appendValue(ChronoField.INSTANT_SECONDS)
                .appendValue(ChronoField.MILLI_OF_SECOND, 3)
                .appendLiteral(")/")
                .toFormatter();

        for(Map broadcastItem : contentMap){

            String broadcastDateUtc = (String)broadcastItem.get("starttimeutc");
            Instant date = jsonDateFormatter.parse(broadcastDateUtc, Instant::from);

            String formattedDate = date.toString();

            Map program = (Map)broadcastItem.get("program");

            Map broadcastContent = Map.of(
                    "id", program.get("id"),
                    "title", broadcastItem.get("title") != null ? broadcastItem.get("title") : "",
                    "starttimeutc", formattedDate.replace("T"," ").replace("Z","")
            );

            broadcastList.add(broadcastContent);

        }

        User user = whoAmI();
        List<Favorite> favorite = user.getFavorites();
        List<Map> myFavorites = new ArrayList<>();

        String url = "";

        for(Favorite item : favorite){
            url = item.getUrl();
            int favoriteId = Integer.parseInt(url.split("=")[1]);

            for(Map findId : broadcastList){

                if (favoriteId == (int)findId.get("id")) {
                        myFavorites.add(findId);
                }
            }
        }

        return myFavorites;
    }

    public User register(User user){return myUserDetailsService.registerUser(user);}

    public User whoAmI(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email);
    }

    public String addFriend(User friend) {
        User user = whoAmI();
        if(user != null){
            user.addFriend(friend);
            userRepo.save(user);
            return "Din vän har lagts till!";
        }
        return null;
    }

    public String deleteFriendById (long id){
            User user = whoAmI();
            long userId = user.getUserId();

            if (user != null) {

                if (userRepo.existsById(id)) {
                    user.removeFriend(id);
                    userRepo.deleteFriend(id, userId);

                    return "Användare med id: " + id + " har tagits bort.";
                }

            }
            return "Hittar inte vän med id: " + id;
    }

    public List<User> getFriends() {
        User user = whoAmI();
        long userId = user.getUserId();

        if(user == null){
            return null;
        }

        return user.getFriends();

    }
}

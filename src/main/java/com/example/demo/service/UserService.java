package com.example.demo.service;

import com.example.demo.configs.MyUserDetailsService;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Instant;
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
            case "scheduledepisodes/rightnow":
                return getAllBroadcasts(contentMap);
        }

        return null;
    }

    // Används för att hämta genom ID
    public List<Map> getAllOptionsById(String pathOption, String responseOption, int id){
        RestTemplate template = new RestTemplate();

        Map response = template.getForObject(sverigesRadioApi + pathOption + id + jsonFormat2, Map.class);

        List<Map> contentMap = (List<Map>) response.get(responseOption);

        switch (pathOption) {
            case "programs/index?channelid=":
                return getProgramsByChannelId(contentMap);
            case "programs/index?programcategoryid=":
                return getProgramsByCategoryId(contentMap);
            case "broadcasts?programid=":
                return getProgramBroadcasts(contentMap);
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
            if(broadcastItem.get("nextscheduledepisode") == null){
                continue;
            }

            Map nextscheduledepisode = (Map) broadcastItem.get("nextscheduledepisode");
            String title = (String)nextscheduledepisode.get("title");
            String description = (String) nextscheduledepisode.get("description");
            String starttimeutc = (String) nextscheduledepisode.get("starttimeutc");

            Instant date = jsonDateFormatter.parse(starttimeutc, Instant::from);
            String formattedDate = date.toString();

            Map broadcastContent = Map.of(
                "id" , broadcastItem.get("id"),
                "name", broadcastItem.get("name"),
                "title", title,
                "description", description,
                "starttimeutc", formattedDate.replace("T"," ").replace("Z","")
            );

            broadcastList.add(broadcastContent);
        }

        return broadcastList;
    }

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

            Map broadcastContent = Map.of(
                    "id", broadcastItem.get("id"),
                    "title", broadcastItem.get("title"),
                    "broadcastdateutc", formattedDate.replace("T"," ").replace("Z",""),
                    "totalduration", formattedSeconds,
                    "image", broadcastItem.get("image")

            );

            broadcastList.add(broadcastContent);


            if(broadcastItem.get("broadcastfiles") != null){
                List<Map> broadcastFilesList = (List<Map>) broadcastItem.get("broadcastfiles");
                for(Map broadcastFile : broadcastFilesList){

                    Map broadCastFiles1 = Map.of(
                         "url" , broadcastFile.get("url")
                    );
                    broadcastList.add(broadCastFiles1);
                }
            }
        }

        return broadcastList;
    }

    public User register(User user){return myUserDetailsService.registerUser(user);}

    public User whoAmI(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email);
    }

    public User addFriend(User friend) {
        User user = whoAmI();
        if(user != null){
            user.addFriend(friend);
            return userRepo.save(user);
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

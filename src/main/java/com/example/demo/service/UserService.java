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
    private String jsonFormat = "?format=json";
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

        //använd GenericObject ist för map. list = Arrays.asList(contentMap)
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
        /*
            Hämtar bara ut ett objekt här.
         */

        RestTemplate template = new RestTemplate();

        Map response = template.getForObject(sverigesRadioApi + "programs/" + id + "?format=json", Map.class);

        Map program = (Map) response.get("program");

        Map description = Map.of(
            "description" , program.get("description"),
            "broadcastinfo", program.get("broadcastinfo")
        );

        return description;
    }

    private List<Map> getAllChannels(List<Map> contentMap){
        List<Map> channels = new ArrayList<>();

        for(Map channel : contentMap){

            Map generic = Map.of(
                "id", channel.get("id"),
                "name", channel.get("name"),
                "image", channel.get("image") != null ? channel.get("image") : "https://static-cdn.sr.se/images/2386/d05d0580-43ed-48ef-991b-01b536e03b33.jpg?preset=api-default-square",
                "tagline", channel.get("tagline"),
                "scheduleurl", channel.get("scheduleurl") != null ? channel.get("scheduleurl") : "",
                "siteurl", channel.get("siteurl")
            );
            channels.add(generic);
        }

        return channels;
    }

    private List<Map> getAllCategories(List<Map> contentMap){
        List<Map> channels = new ArrayList<>();

        for(Map channel : contentMap){

            Map generic = Map.of(
                "id" , channel.get("id"),
                "name", channel.get("name")
            );
            channels.add(generic);
        }

        return channels;
    }

    // kopplad med getAllOptions
    private List<Map> getAllBroadcasts(List<Map> contentMap) {
        List<Map> broadcasts = new ArrayList<>();

        // list + objekt
        for(Map broadcast : contentMap){
            if(broadcast.get("nextscheduledepisode") == null){
                continue;
            }

            Map nextscheduledepisode = (Map) broadcast.get("nextscheduledepisode");
            String title = (String)nextscheduledepisode.get("title");
            String description = (String) nextscheduledepisode.get("description");
            String starttimeutc = (String) nextscheduledepisode.get("starttimeutc");

            Map generic = Map.of(
                "id" , broadcast.get("id"),
                "name", broadcast.get("name"),
                "title", title,
                "description", description,
                "starttimeutc", starttimeutc
            );

            broadcasts.add(generic);
        }

        return broadcasts;
    }

    private List<Map> getProgramsByChannelId(List<Map> contentMap) {

        List<Map> allPrograms = new ArrayList<>();

        for(Map program : contentMap){

            Map generic = Map.of(
                "id" , program.get("id"),
                "name", program.get("name"),
                "programimage", program.get("programimage"),
                "programurl", program.get("programurl"),
                "description", program.get("description"),
                "responsibleeditor", program.get("responsibleeditor")
            );

            allPrograms.add(generic);
        }

        return allPrograms;

    }

    private List<Map> getProgramsByCategoryId(List<Map> contentMap) {

        List<Map> allPrograms = new ArrayList<>();

        for(Map program : contentMap){

            Map generic = Map.of(
                    "id" , program.get("id"),
                    "name", program.get("name"),
                    "programimage", program.get("programimage"),
                    "programurl", program.get("programurl"),
                    "description", program.get("description"),
                    "responsibleeditor", program.get("responsibleeditor")
            );
            allPrograms.add(generic);
        }

        return allPrograms;

    }

    public List<Map> searchProgram(String input) {
        List<Map> programs = new ArrayList<>();

        RestTemplate template = new RestTemplate();

        Map response = template.getForObject(sverigesRadioApi + "programs" + jsonFormatPagiFalse, Map.class);

        List<Map> contentMap = (List<Map>) response.get("programs");

        for(Map program : contentMap){
            String name= (String) program.get("name"); // Filtrerar genom program-namn
            String description = (String) program.get("description"); // Filtrerar genom program-beskrivning

            if(name.toLowerCase().contains(input.toLowerCase()) || description.toLowerCase().contains(input.toLowerCase())){
                Map generic = Map.of(
                    "id",program.get("id"),
                    "name",program.get("name"),
                    "programimage",program.get("programimage"),
                    "programurl",program.get("programurl"),
                    "description", program.get("description"),
                    "responsibleeditor",program.get("responsibleeditor")
                );

                programs.add(generic);

            }

        }
        return programs;
    }

    public List<Map> getProgramBroadcasts(List<Map> contentMap) {
        List<Map> broadCasts = new ArrayList<>();

        DateTimeFormatter jsonDateFormatter = new DateTimeFormatterBuilder()
                .appendLiteral("/Date(")
                .appendValue(ChronoField.INSTANT_SECONDS)
                .appendValue(ChronoField.MILLI_OF_SECOND, 3)
                .appendLiteral(")/")
                .toFormatter();

        for(Map broadCast : contentMap){

            String broadcastDateUtc = (String)broadCast.get("broadcastdateutc");
            Instant date = jsonDateFormatter.parse(broadcastDateUtc, Instant::from);

            String formattedDate = date.toString();

            Map generic = Map.of(
                    "id", broadCast.get("id"),
                    "title", broadCast.get("title"),
                    "broadcastdateutc", formattedDate.replace("T"," ").replace("Z",""),
                    "totalduration", broadCast.get("totalduration"),
                    "image", broadCast.get("image")

            );

            broadCasts.add(generic);

            // skicka url till friends. samt egna favorites.
            // hämtar url för sändningar.
            if(broadCast.get("broadcastfiles") != null){
                List<Map> broadCastFiles = (List<Map>) broadCast.get("broadcastfiles");
                for(Map broadCastList : broadCastFiles){

                    Map broadCastFiles1 = Map.of(
                         "url" , broadCastList.get("url")
                    );
                    broadCasts.add(broadCastFiles1);
                }
            }
        }

        return broadCasts;
    }

    public User register(User user){return myUserDetailsService.registerUser(user);}

    public User whoAmI(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
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

                    return "Deleted";
                }

            }
            return "The friend does not exist!";
    }

}

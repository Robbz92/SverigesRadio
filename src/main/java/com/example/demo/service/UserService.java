package com.example.demo.service;

import com.example.demo.configs.GenericObject;
import com.example.demo.configs.MyUserDetailsService;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    private String jsonFormat = "/?format=json";
    private String jsonFormat2 = "&format=json&pagination=false";

    // Används för att hämta ALLT
    public List<GenericObject> getAllOptions(String pathOption, String responseOption){
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

    public List<GenericObject> getAllOptionsById(String pathOption, String responseOption, int id){
        RestTemplate template = new RestTemplate();

        Map response = template.getForObject(sverigesRadioApi + pathOption + id + jsonFormat2, Map.class);

        List<Map> contentMap = (List<Map>) response.get(responseOption);


        switch (pathOption) {
            case "programs/index?channelid=":
                return getProgramsByChannelId(contentMap);
            case "programs/index?programcategoryid=":
                return getProgramsByCategoryId(contentMap);

        }

        return null;
    }

    private List<GenericObject> getAllChannels(List<Map> contentMap){
        List<GenericObject> channels = new ArrayList<>();

        for(Map channel : contentMap){

            GenericObject generic = new GenericObject(
                    channel.get("id"),
                    channel.get("name"),
                    channel.get("image"),
                    channel.get("tagline"),
                    channel.get("siteurl"),
                    channel.get("scheduleurl")
            );


            channels.add(generic);
        }

        return channels;
    }

    private List<GenericObject> getAllCategories(List<Map> contentMap){
        List<GenericObject> channels = new ArrayList<>();

        for(Map channel : contentMap){

            GenericObject generic = new GenericObject(
                    channel.get("id"),
                    channel.get("name")
            );


            channels.add(generic);
        }

        return channels;
    }

    // kopplad med getAllOptions
    private List<GenericObject> getAllBroadcasts(List<Map> contentMap) {
        List<GenericObject> broadcasts = new ArrayList<>();

        for(Map broadcast : contentMap){

            GenericObject generic = new GenericObject(
                    broadcast.get("id"),
                    broadcast.get("name"),
                    broadcast.get("programimage"),
                    broadcast.get("programurl"),
                    broadcast.get("description"),
                    broadcast.get("responsibleeditor")
            );

            broadcasts.add(generic);
        }

        return broadcasts;
    }

    public User register(User user){return myUserDetailsService.registerUser(user);}

    public List<User> getAll(){
        return userRepo.findAll();
    }


    private List<GenericObject> getProgramsByChannelId(List<Map> contentMap) {


        List<GenericObject> allPrograms = new ArrayList<>();

        for(Map program : contentMap){

            GenericObject generic = new GenericObject(
                    program.get("id"),
                    program.get("name"),
                    program.get("programimage"),
                    program.get("programurl"),
                    program.get("description"),
                    program.get("responsibleeditor")
            );
            allPrograms.add(generic);
        }

        return allPrograms;

    }
    private List<GenericObject> getProgramsByCategoryId(List<Map> contentMap) {


        List<GenericObject> allPrograms = new ArrayList<>();

        for(Map program : contentMap){

            GenericObject generic = new GenericObject(
                    program.get("id"),
                    program.get("name"),
                    program.get("programimage"),
                    program.get("programurl"),
                    program.get("description"),
                    program.get("responsibleeditor")
            );
            allPrograms.add(generic);
        }

        return allPrograms;

    }

}

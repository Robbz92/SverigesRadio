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
    private String jsonFormat = "/?format=json";


    public List<GenericObject> getAllOptions(String option){
        RestTemplate template = new RestTemplate();

        Map response = template.getForObject(sverigesRadioApi + option + jsonFormat, Map.class);

        List<Map> contentMap = (List<Map>) response.get(option);

        if(option.equals("channels")){
            return getAllChannels(contentMap);
        }

        return null;
    }

    public User register(User user){return myUserDetailsService.registerUser(user);}

    public List<User> getAll(){
        return userRepo.findAll();
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
    /*
        här kan vi även lägga till en whoami metod för att visa aktiv användre
     */


}

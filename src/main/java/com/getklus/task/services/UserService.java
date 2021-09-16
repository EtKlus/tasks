package com.getklus.task.services;


import com.getklus.task.models.Users;
import com.getklus.task.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private Users findOrThrow(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "L'utilisateur n'existe pas."));
    }

    public List<Users> getUsers(){
        return userRepository.findAll();
    }
    public Users getUser(String name){
        List<Users> all = getUsers();
        for (Users u: all
             ) {
            if(u.getUsername().equals(name)){
                return u;
            }
        }
        return new Users();
    }

    public Users getUserById(long id){

        return findOrThrow(id);
    }

    public void saveUser(Users user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public void updateUserData(Users user){
        userRepository.save(user);
    }

    public void enableUser(long id){
         Users user = findOrThrow(id);
         user.setEnable(!user.isEnable());

         userRepository.save(user);
    }

    public void removeUser(long id){
        Users user = findOrThrow(id);
        userRepository.delete(user);
    }


}

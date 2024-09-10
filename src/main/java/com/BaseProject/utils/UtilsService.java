package com.BaseProject.utils;

import com.BaseProject.utils.exception.UnAuthorizedException;
import com.BaseProject.user.model.BaseUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UtilsService {

//    @Autowired
//    private ClientRepository clientRepository;
//    @Autowired
//    private ManagerRepository managerRepository;

    public BaseUser extractCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new UnAuthorizedException("Authentication error");
        }
//        var user = clientRepository.findByEmail(authentication.getName()).orElse(null);
//        if(user == null){
//            return managerRepository.findByEmail(authentication.getName()).orElse(null);
//        }
//        return user;
        return null;
    }

}
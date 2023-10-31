package com.example.simulateurmarche.Iservices;


import com.example.simulateurmarche.entities.ERole;
import com.example.simulateurmarche.entities.User;

import java.util.List;

public interface IUserService {

    // CRUD
    List<User> getUsers();
    User saveUser(User user);
    void deleteUser(Long id);
    User updateUser(User user);

    // service avancés
    User addRoleToUser(String username, ERole roleName);
    User getUser(String username);
    User getUserByMail(String mail);
    User activateUser(User user);
    List<User> retrieveUserByState(boolean stateUser);
    List<User> retrieveUserByAddress(String adressUser);
    void updatePassword(String emailUser, String newPassword);

    public void forgotpass(String emailuser);
    User desactivateUser(User user1);
    public List<User> searchh(String s);
    public boolean ifEmailExist(String email);

    public List<User> getdisable();
    public User findById(Long id) ;

}

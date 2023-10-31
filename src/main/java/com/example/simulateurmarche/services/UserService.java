package com.example.simulateurmarche.services;


import com.example.simulateurmarche.Iservices.IUserService;
import com.example.simulateurmarche.entities.ERole;
import com.example.simulateurmarche.entities.Role;
import com.example.simulateurmarche.entities.User;
import com.example.simulateurmarche.repositories.RoleRepository;
import com.example.simulateurmarche.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Value("${spring.mail.username}")
    private String sender;
    private final JavaMailSender javaMailSender;

    public UserService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    @Autowired
    UserRepository userRepo;
    @Autowired
    RoleRepository roleRepo;
    @Override
    public List<User> getUsers() {

        return (List<User>) userRepo.findAll();
    }
    @Override
    public User saveUser(User user) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setStateuser(true);

        return userRepo.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public User updateUser(User user) {
        User updateUser = userRepo.findById(user.getId()).get();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        updateUser.setPassword(hashedPassword);
        updateUser.setName(user.getName());
        updateUser.setPrenom(user.getPrenom());
        updateUser.setTel(user.getTel());
        updateUser.setAddress(user.getAddress());
        updateUser.setUsername(user.getUsername());
        updateUser.setDatenaissance(user.getDatenaissance());
        updateUser.setEmail(user.getEmail());
        return userRepo.save(updateUser);
    }
    //service avancés
    @Override
    public User addRoleToUser(String username, ERole roleName) {


        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
        return userRepo.save((user));

    }

    // recherche
    @Override
    public User getUser(String username) {

        return userRepo.findByUsername(username);
    }

    @Override
    public User getUserByMail(String mail) {
        return this.userRepo.findByEmail(mail);
    }

    @Override
    public List<User> retrieveUserByState(boolean stateUser) {
        return (List<User>) userRepo.findByStateuser(stateUser);
    }
    @Override
    public List<User> retrieveUserByAddress(String adressUser) {
        return userRepo.findByAddress(adressUser);
    }
    //activation user
    @Override
    public User activateUser(User user1)
    { User user=userRepo.findByUsername(user1.getUsername());
        if(user.isStateuser()==false)
        {
            user.setStateuser(true);
        }
        else if(user.isStateuser()==true)
        {
            user.setStateuser(false);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setEmail(user1.getEmail());
        user.setName(user1.getName());
        user.setPrenom(user1.getPrenom());
        user.setDatenaissance(user1.getDatenaissance());
        user.setTel(user1.getTel());
        user.setAddress(user1.getAddress());
        return userRepo.save(user);
    }
    @Override
    public User desactivateUser(User user1)
    { User user=userRepo.findByUsername(user1.getUsername());
        if(user.isStateuser()==true)
        {
            user.setStateuser(false);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setEmail(user1.getEmail());
        user.setName(user1.getName());
        user.setPrenom(user1.getPrenom());
        user.setDatenaissance(user1.getDatenaissance());
        user.setTel(user1.getTel());
        user.setAddress(user1.getAddress());
        return userRepo.save(user);
    }
    // update password
    public void updatePassword(String emailUser, String newPassword) {
        User u = userRepo.findByEmail(emailUser);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        u.setPassword(encodedPassword);
        userRepo.save(u);
    }
    public List<User>  findAllByOrderBOrderByRolesDesc()
    {
        return userRepo.findAllByOrderByRolesDesc();
    }


    // stats nbre of users actives or desactive








// forget password mailing

    /*public void forgotpass(String emailuser) {

        User d = userRepo.findByEmail(emailuser);
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailuser);
            mailMessage.setText("This a non reply message from malak\n " + "Dear Client \n"
                    + "Please follow the following link to reset your password: \n" + "http://localhost:8090/user/updatepassword");
            mailMessage.setSubject("password reset");
            javaMailSender.send(mailMessage);
            //return "Mail Sent Successfully...";
        }*/
    public void forgotpass(String emailuser) {

        User d = userRepo.findByEmail(emailuser);
        SimpleMailMessage mailMessage
                = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(emailuser);
        mailMessage.setText("This a non reply message from dhia\n " + "Dear Client \n"
                + "Please follow the following link to reset your password: \n" + "http://localhost:4200/update");
        mailMessage.setSubject("password reset");
        javaMailSender.send(mailMessage);
        //return "Mail Sent Successfully...";
    }



    public boolean ifEmailExist(String email) {
        return userRepo.existsByEmail(email);
    }
    /// recherche stream
    @Override
    public List<User> searchh(String s) {

        return userRepo.findAll().stream().filter(user -> user.getName()!=null )
                .filter(user -> user.getName().contains(s)  ).collect(Collectors.toList());


    }

// scheduler

    // @Scheduled(fixedRate = 5000)
    @Scheduled(cron = "0 0 8 * * MON")
    public List<User> getdisable()
    {

        return userRepo.getusersdisable();
    }
}

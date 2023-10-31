package com.example.simulateurmarche.controllers;

import java.util.*;
import java.util.stream.Collectors;
import javax.validation.Valid;


import com.example.simulateurmarche.entities.ConfirmationToken;
import com.example.simulateurmarche.entities.ERole;
import com.example.simulateurmarche.entities.Role;
import com.example.simulateurmarche.entities.User;
import com.example.simulateurmarche.jwt.JwtUtils;
import com.example.simulateurmarche.payload.request.LoginRequest;
import com.example.simulateurmarche.payload.request.SignupRequest;
import com.example.simulateurmarche.payload.response.*;
import com.example.simulateurmarche.repositories.ConfirmationTokenRepository;
import com.example.simulateurmarche.repositories.RoleRepository;
import com.example.simulateurmarche.repositories.UserRepository;
import com.example.simulateurmarche.security.UserDetailsImpl;
import com.example.simulateurmarche.services.UserService;
import com.example.simulateurmarche.userFunction.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;


@RestController
@RequestMapping("/api-auth")
@Slf4j
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;
    @Value("${google.id}")
    private String idClient;

    @Value("${secretPsw}")
    String secretPsw;
    private final JavaMailSender javaMailSender;


    int n = 5;

    String email;

    public AuthController(JavaMailSender javaMailSender)
    {
        this.javaMailSender = javaMailSender;
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
          //login
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());
        if (userDetails.getUser().isStateuser() == true) {
            return ResponseEntity.ok(
                    new JwtResponse(jwt, userDetails.getUser().getId(), userDetails.getUsername(),
                            userDetails.getUser().getEmail(), roles
                            , userDetails.getUser().isStateuser(), userDetails.getUser().getName(),
                            userDetails.getUser().getPrenom(),
                            userDetails.getUser().getTel()));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/signup")
    public AccountResponse registerUser( @RequestBody SignupRequest signUpRequest) {


        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getAddress(), signUpRequest.getTel(),
                signUpRequest.getName(), signUpRequest.getPrenom(),signUpRequest.getBirth());

        // Affecter un role
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
                    switch (role) {
                        case "ADMIN":
                            Role adminRole = roleRepository.findByName(ERole.ADMIN);
                            roles.add(adminRole);

                            break;
                        case "consultant":
                            Role consultantrole = roleRepository.findByName(ERole.CONSULTANT);
                            roles.add(consultantrole);

                            break;

                        default:
                            Role userRole = roleRepository.findByName(ERole.CLIENT);
                            roles.add(userRole);
                    }
                }
        );
        //}

        AccountResponse accountResponse = new AccountResponse();
        boolean result = userRepository.existsByEmail(signUpRequest.getEmail());
        if (result) {
            accountResponse.setResult(0);

        } else {

            user.setRoles(roles);
            userRepository.save(user);
            accountResponse.setResult(1);
            //user.setStateuser(false);
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            confirmationTokenRepository.save(confirmationToken);
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            mailMessage.setFrom("dhiaeddine.bensaada@esprit.tn");
            mailMessage.setTo(user.getEmail());
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8089/api-auth/confirm-account?token="+confirmationToken.getConfirmationToken());

            mailMessage.setSubject("Complete Registration!");
            javaMailSender.send(mailMessage);

        }
        log.info("" + ResponseEntity.ok(new MessageResponse("User registered successfully!")));
        return accountResponse;
    }

    // Social Login

    static String getAlphaNumericString(int n) {

        String randomNumber = "0123456789";
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (randomNumber.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(randomNumber.charAt(index));
        }

        return sb.toString();
    }
    private LoginResponse login(String email, String username, String prenom ,String nom) {
        boolean result = userService.ifEmailExist(email); // t // f
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.CLIENT);
        roles.add(userRole);
        if (!result) {

            User user = new User();
            user.setEmail(email);
            user.setPassword(encoder.encode("root1234"));
            user.setUsername(username);
            user.setStateuser(true);
            user.setPrenom(prenom);
            user.setName(nom);
            user.setRoles(roles);
            log.info("test",user);
            userRepository.save(user);
        }
        JwtLogin jwtLogin = new JwtLogin();
        jwtLogin.setUsername(username);
        jwtLogin.setPassword("root1234");
        jwtLogin.setNom(nom);
        jwtLogin.setPrenom(prenom);

        log.info("jwt:" + jwtLogin.getUsername());
        log.info("jwt:" + jwtLogin.getPassword());

        return jwtUtils.login(jwtLogin);
    }


    private User createUser(String email) {
        User user = new User();
        user.setEmail(email);
        user.setName(("malak"));
        user.setPassword(encoder.encode("root123"));

        return userService.saveUser(user);
    }


    //confirm
    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = userRepository.findByEmail(token.getUserEntity().getEmail());
            user.setStateuser(true);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Account verified! User registered successfully!"));

        }
        else
        {
            return ResponseEntity.ok(new MessageResponse("Error: Invalid Link!"));

        }
    }

    // Session
   @GetMapping("/user")
    public User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userOptional = userRepository.findByUsername(username);
       // User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(userOptional.getId()); // User.getId() // getName()

    }
}

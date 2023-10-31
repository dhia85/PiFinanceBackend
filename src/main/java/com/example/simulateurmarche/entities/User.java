package com.example.simulateurmarche.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private String name;

    private String prenom;


    String username;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date datenaissance;
    private String email;


    private boolean stateuser;



    private String password;

    protected String confirmpassworduser;

    private String address;

    private String tel;

    private String image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public User(@Size(max = 80) String username, @Size(max = 50) @Email String email, @Size(max = 120) String password,
                String address, @Size(max = 50) String tel, @Size(max = 50) String nom, @Size(max = 50) String prenom,
                Date birth) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
        this.tel = tel;
        this.name = nom;
        this.prenom = prenom;
        this.datenaissance=birth;



    }

    public User(Long id) {
        this.id = id;
    }
}

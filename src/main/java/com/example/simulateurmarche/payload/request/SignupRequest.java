package com.example.simulateurmarche.payload.request;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.*;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest implements Serializable {
    @NotBlank
    @Size(min = 3, max = 20)
     private String username;
 
    @NotBlank
    @Size(max = 50)
    @Email
     private String email;

    private Set<String> role;
    
    @NotBlank
    @Size(min = 6, max = 40)
      private String password;
    

     private String name;

     private String prenom;
    

    private String address;

    @Size(min = 8, max = 12)
    private String tel;
    
    private boolean enabled;
    private String verificationCode;
	private boolean accountVerified;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
	private Date birth;



    private Long organisationId;
    
    
}

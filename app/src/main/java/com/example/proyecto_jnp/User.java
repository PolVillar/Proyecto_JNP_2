package com.example.proyecto_jnp;

import java.util.Base64;
import java.util.Date;
import java.util.List;

/*import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cat.institutmarianao.closetws.ClosetwsApplication;
import cat.institutmarianao.closetws.PasswordSerializer;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;*/

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    public static final int MIN_USERNAME = 4;
    public static final int MAX_USERNAME = 25;
    public static final int MIN_PASSWORD = 4;
    public static final int MAX_PHONE=15;
    public static final int MIN_FULL_NAME = 3;
    public static final int MAX_FULL_NAME = 100;

    @NonNull

    private String username;

    @NonNull

    private String password;

    @NonNull

    private String mail;

    private String phone;

    @NonNull

    private String fullName;

    @NonNull

    private Date birthDate;
    private byte[] profilePicture;

    private List<Container> containers;

    public String getBase64Image() {
        return Base64.getEncoder().encodeToString(profilePicture);
    }
    public void setBase64Image(String b64Image) {
        this.profilePicture=Base64.getDecoder().decode(b64Image);
    }
}

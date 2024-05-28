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


public class User {

    public static final int MIN_USERNAME = 4;
    public static final int MAX_USERNAME = 25;
    public static final int MIN_PASSWORD = 4;
    public static final int MAX_PHONE=15;
    public static final int MIN_FULL_NAME = 3;
    public static final int MAX_FULL_NAME = 100;


    private String username;
    private String password;
    private String mail;
    private String phone;
    private String fullName;
    private Date birthDate;
    private byte[] profilePicture;
    private List<Container> containers;

    public User() {
    }

    public User(String username, String password, String mail, String phone, String fullName, Date birthDate, byte[] profilePicture, List<Container> containers) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.phone = phone;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.profilePicture = profilePicture;
        this.containers = containers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<Container> getContainers() {
        return containers;
    }

    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }

    public String getBase64Image() {
        return Base64.getEncoder().encodeToString(profilePicture);
    }
    public void setBase64Image(String b64Image) {
        this.profilePicture=Base64.getDecoder().decode(b64Image);
    }
}

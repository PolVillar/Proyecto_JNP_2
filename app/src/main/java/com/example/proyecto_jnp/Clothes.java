package com.example.proyecto_jnp;

import java.util.Base64;
import java.util.Date;

/*import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cat.institutmarianao.closetws.ClosetwsApplication;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;*/

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Clothes {

    public enum Collection{
        WINTER,SPRING,SUMMER,AUTUMN
    }

    public enum Category{
        JACKET,SWEATER_SHIRT,PANTS_SKIRT,SHOES,UNDERWEAR,COMPLEMENT
    }

    @EqualsAndHashCode.Include

    @NonNull

    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String color;

    @NonNull
    private String size;
    private byte[] picture;

    @NonNull

    private Collection collection;

    @NonNull

    private Category category;

    private Container container;

    @NonNull

    private Date lastUse;

    public String getBase64Image() {
        return Base64.getEncoder().encodeToString(picture);
    }

    public void setBase64Image(String b64Image) {
        this.picture=Base64.getDecoder().decode(b64Image);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public Date getLastUse() {
        return lastUse;
    }

    public void setLastUse(Date lastUse) {
        this.lastUse = lastUse;
    }
}


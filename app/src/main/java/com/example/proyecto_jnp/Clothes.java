package com.example.proyecto_jnp;

import java.util.Base64;
import java.util.Date;

public class Clothes {

    public enum Collection{
        WINTER,SPRING,SUMMER,AUTUMN
    }

    public enum Category{
        JACKET,SWEATER_SHIRT,PANTS_SKIRT,SHOES,UNDERWEAR,COMPLEMENT
    }

    private Long id;
    private String name;
    private String color;
    private String size;
    private byte[] picture;
    private Collection collection;
    private Category category;
    private Container container;
    private Date lastUse;

    public Clothes(Long id, String name, String color, String size, byte[] picture, Collection collection, Category category, Container container, Date lastUse) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.size = size;
        this.picture = picture;
        this.collection = collection;
        this.category = category;
        this.container = container;
        this.lastUse = lastUse;
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


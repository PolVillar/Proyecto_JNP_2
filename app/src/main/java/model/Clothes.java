package model;

import java.util.Base64;
import java.util.Date;

import model.Container;

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
    private String collection;
    private String category;
    private Container container;
    private Date lastUse;

    public Clothes() {
    }

    public Clothes(Long id, String name, String color, String size, byte[] picture, String collection, String category, Container container, Date lastUse) {
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

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clothes clothes = (Clothes) o;

        if (!id.equals(clothes.id)) return false;
        if (!collection.equals(clothes.collection)) return false;
        return category.equals(clothes.category);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + collection.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }
}


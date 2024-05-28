package com.example.proyecto_jnp;


import java.util.List;


public abstract class Container {
    public enum Type{
        CLOSET,SUITCASE
    }
    protected Long id;

    protected String name;

    protected Type type;

    protected User owner;

    protected List<Clothes> clothes;

    public Container() {
    }

    public Container(Long id, String name, Type type, User owner, List<Clothes> clothes) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.clothes = clothes;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Clothes> getClothes() {
        return clothes;
    }

    public void setClothes(List<Clothes> clothes) {
        this.clothes = clothes;
    }
}


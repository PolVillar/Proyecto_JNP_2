package com.example.proyecto_jnp;

import java.util.List;

public class Suitcase extends Container{

    public Suitcase() {
    }

    public Suitcase(Long id, String name, Type type, User owner, List<Clothes> clothes) {
        super(id, name, type, owner, clothes);
    }
}


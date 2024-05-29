package model;

import com.example.proyecto_jnp.Clothes;
import com.example.proyecto_jnp.Container;
import com.example.proyecto_jnp.User;

import java.util.List;

public class Suitcase extends Container {

    public Suitcase() {
    }

    public Suitcase(Long id, String name, Type type, User owner, List<Clothes> clothes) {
        super(id, name, type, owner, clothes);
    }
}


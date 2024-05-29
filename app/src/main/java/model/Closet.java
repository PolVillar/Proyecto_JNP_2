package model;

import java.util.List;

public class Closet extends Container{

    public Closet() {
    }
    public Closet(Long id, String name, Container.Type type, User owner, List<Clothes> clothes) {
        super(id, name, type, owner, clothes);
    }
}

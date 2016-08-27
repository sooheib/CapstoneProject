package com.nanodegree.android.watchthemall.api.trakt;

/**
 * Class representing Trakt role info
 */
public class Role {

    private String character;
    private Person person;

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Role{" +
                "character='" + character + '\'' +
                ", person=" + ((person==null)?null:person.toString()) +
                '}';
    }
}

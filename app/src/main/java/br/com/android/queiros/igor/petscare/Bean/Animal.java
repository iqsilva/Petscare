package br.com.android.queiros.igor.petscare.Bean;

/**
 * Created by igorf on 19/09/2017.
 */

public class Animal {
    String animalId;
    String name;
    String breed;
    String color;
    String coat;
    String genre;
    String specie;

    public Animal() {

    }

    public Animal(String animalId, String name, String breed, String color, String coat, String genre, String specie) {
        this.animalId = animalId;
        this.name = name;
        this.breed = breed;
        this.color = color;
        this.coat = coat;
        this.genre = genre;
        this.specie = specie;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCoat() {
        return coat;
    }

    public void setCoat(String coat) {
        this.coat = coat;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }
}



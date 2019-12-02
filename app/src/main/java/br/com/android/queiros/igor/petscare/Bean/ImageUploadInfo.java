package br.com.android.queiros.igor.petscare.Bean;

/**
 * Created by igorf on 03/11/2017.
 */

public class ImageUploadInfo {

    public String animalId;

    public String name;

    public String breed;

    public String color;

    public String coat;

    public String genre;

    public String specie;

    public String imageURL;


    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String animalId, String name, String breed, String color, String coat, String genre, String specie, String imageURL) {
        this.animalId = animalId;
        this.name = name;
        this.breed = breed;
        this.color = color;
        this.coat = coat;
        this.genre = genre;
        this.specie = specie;
        this.imageURL = imageURL;
    }

    public String getAnimalId() {
        return animalId;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getColor() {
        return color;
    }

    public String getCoat() {
        return coat;
    }

    public String getGenre() {
        return genre;
    }

    public String getSpecie() {
        return specie;
    }

    public String getImageURL() {
        return imageURL;
    }
}
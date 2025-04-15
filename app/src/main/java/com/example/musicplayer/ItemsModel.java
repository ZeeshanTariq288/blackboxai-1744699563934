package com.example.musicplayer;

public class ItemsModel {
    private String surahNameEnglish;
    private String surahNumber;
    private String numberOfVerses;
    private String placeOfRevelation;
    private String url;
    private String surahNameArabic;

    public ItemsModel(String surahNameEnglish, String surahNumber, String numberOfVerses, String placeOfRevelation, String url, String surahNameArabic) {
        this.surahNameEnglish = surahNameEnglish;
        this.surahNumber = surahNumber;
        this.numberOfVerses = numberOfVerses;
        this.placeOfRevelation = placeOfRevelation;
        this.url = url;
        this.surahNameArabic = surahNameArabic;
    }

    public String getSurahNameEnglish() {
        return surahNameEnglish;
    }

    public String getSurahNumber() {
        return surahNumber;
    }

    public String getNumberOfVerses() {
        return numberOfVerses;
    }

    public String getPlaceOfRevelation() {
        return placeOfRevelation;
    }

    public String getUrl() {
        return url;
    }

    public String getSurahNameArabic() {
        return surahNameArabic;
    }
}

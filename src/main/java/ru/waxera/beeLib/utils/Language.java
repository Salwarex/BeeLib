package ru.waxera.beeLib.utils;

public enum Language {
    RUSSIAN("ru-RU"),
    ENGLISH("en-EN");

    private final String name;
    private Language(String name){
        this.name = name;
    }

    public String getFilename(){
        return this.name;
    }

    public static Language getLanguage(String shortname){
        for(Language language : Language.values()){
            if(language.name.equalsIgnoreCase(shortname)){
                return language;
            }
        }
        return null;
    }
}

package ru.waxera.beeLib.utils;

import ru.waxera.beeLib.BeeLib;

import java.util.ArrayList;

public class StringUtils {


    public static String format(String message){
        return colChar(language(message));
    }
    public static String language(String str){
        ArrayList<String> def = new ArrayList<>(); //non-translatable
        ArrayList<String> trs = new ArrayList<>(); //translatable

        String[] splStr = str.split("@");
        for(int i = 0; i < splStr.length; i++){
            if(i % 2 == 0){
                def.add(splStr[i]);
            }
            else {
                trs.add(splStr[i]);
            }
        }

        ArrayList<String> trsResult = splittedTranslate(trs);
        StringBuilder result = new StringBuilder();

        int i_ = 0;
        for(int i = 0; i < splStr.length; i++){
            if(i % 2 == 0){
                result.append(def.get(i_));
            }
            else {
                result.append(trsResult.get(i_));
                i_ += 1;
            }
        }
        return result.toString();
    }

    private static ArrayList<String> splittedTranslate(ArrayList<String> trs){
        ArrayList<String> result = new ArrayList<>();
        Storage langStorage = BeeLib.getLanguage(BeeLib.nowLanguage());
        for(String str : trs){
            if(langStorage != null){
                String translation = langStorage.getConfig().getString(str);
                if(translation != null){
                    result.add(translation);
                }
                else{
                    result.add(str);
                }
            }
            else{
                return trs;
            }
        }
        return result;
    }

    public static String colChar(String str){
        return str.replace("&", "§");
    }

    public static boolean isUuid(String str) {
        String uuidRegex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";
        return str != null && str.matches(uuidRegex);
    }

}

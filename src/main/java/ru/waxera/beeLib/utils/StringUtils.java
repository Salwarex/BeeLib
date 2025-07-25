package ru.waxera.beeLib.utils;

import org.bukkit.plugin.Plugin;
import ru.waxera.beeLib.BeeLib;
import ru.waxera.beeLib.utils.data.pools.file.FileStorage;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {


    public static String format(String message, Plugin plugin){
        plugin = plugin == null ? BeeLib.getInstance() : plugin;
        return colChar(language(message, plugin));
    }

    public static String language(String str, Plugin plugin){
        plugin = plugin == null ? BeeLib.getInstance() : plugin;
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

        ArrayList<String> trsResult = splittedTranslate(trs, plugin);
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

    private static ArrayList<String> splittedTranslate(ArrayList<String> trs, Plugin plugin){
        plugin = plugin == null ? BeeLib.getInstance() : plugin;
        ArrayList<String> result = new ArrayList<>();
        FileStorage langStorage = LanguageManager.getInstance(plugin).getLanguage(LanguageManager.getInstance(plugin).nowLanguage());
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

    public static String[] splitString(String input, int maxLength) {
        if (input == null || input.isEmpty() || maxLength <= 0) {
            return new String[0];
        }

        List<String> result = new ArrayList<>();
        
        String specialSplitter = "\u0000";
        String[] parts = input.replace("@n", specialSplitter).split(specialSplitter);

        int globalIndex = 0;
        for (String part : parts) {
            if (part.isEmpty()) continue;

            int start = 0;
            int len = part.length();

            while (start < len) {
                int end = Math.min(start + maxLength, len);

                if (end == len) {
                    result.add(part.substring(start, end));
                    start = end;
                } else {
                    int lastSpace = -1;
                    for (int i = start; i < end; i++) {
                        if (Character.isWhitespace(part.charAt(i))) {
                            lastSpace = i;
                        }
                    }

                    if (lastSpace != -1 && lastSpace > start) {
                        result.add(part.substring(start, lastSpace));
                        start = lastSpace + 1;
                    } else {
                        result.add(part.substring(start, end));
                        start = end;
                    }
                }
            }
        }

        return result.toArray(new String[0]);
    }

}

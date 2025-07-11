package ru.waxera.beeLib.utils.interfaces.container;

import ru.waxera.beeLib.utils.StringUtils;
import ru.waxera.beeLib.utils.message.Message;

import java.util.HashMap;

@Deprecated
public class InterfaceTemplatesList {
    private static HashMap<String, ContainerInterface> templates = new HashMap<>();

    public static void add(ContainerInterface template){
        String title = template.getTitle();
        if(templates.containsKey(title)){ Message.error(null, "Error when saving the inventory template: An inventory with this title already exists: \""
                + title + "\""); return; }
        templates.put(title, template);
    }

    public static ContainerInterface get(String title) {
        title = StringUtils.format(title, null);
        ContainerInterface result = templates.get(title);
        if(result != null){
            try {
                return (ContainerInterface) result.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
    public static ContainerInterface getOrigin(String title){
        title = StringUtils.format(title, null);
        return templates.getOrDefault(title, null);
    }

    public static void update(String title, ContainerInterface template){
        title = StringUtils.format(title, null);
        if(!templates.containsKey(title)){
            add(template);
            return;
        }
        templates.replace(title, template);
    }
}
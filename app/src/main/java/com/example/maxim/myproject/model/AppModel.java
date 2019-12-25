package com.example.maxim.myproject.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class AppModel {

    @PropertyName("applicationId")
    public String appId;
    public String name;
    public String creator;
    public String purpose;
    public String experience;
    public String example;
    public String section;
    public String phone;

    // добавим в неё ещё поля и не буде вызывать, если не нуджно
//  "creator" -> "ncn"
//        "hashs" -> "пробел"
//        "other" -> ""
//        "descriptionApplication" -> "опо"
//        "purpose" -> "описать заявку"
//        "section" -> "Создание сайтов"
//        "experience" -> "0"
//        "example" -> "есть"
//        "can" -> "натьа"
//        "phone" -> ""
//        "vk" -> "ьсь"
//        "name" -> "новая заявка"
//        "applicationId" -> "69"
}

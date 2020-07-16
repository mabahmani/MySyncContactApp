package com.example.mysynccontactapp.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneNumberFormatter {
    private static PhoneNumberFormatter phoneNumberFormatter = null;
    public static PhoneNumberFormatter getInstance(){
        if(phoneNumberFormatter == null)
            return new PhoneNumberFormatter();

        return phoneNumberFormatter;
    }
    public String formatE164(String number){
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = phoneNumberUtil.parse(number,"IR");
            return phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}

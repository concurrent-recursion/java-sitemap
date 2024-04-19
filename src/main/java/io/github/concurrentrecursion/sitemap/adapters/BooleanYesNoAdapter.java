package io.github.concurrentrecursion.sitemap.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter class for converting between Boolean values and "yes" or "no" strings.
 */
public class BooleanYesNoAdapter extends XmlAdapter<String, Boolean> {

    @Override
    public Boolean unmarshal(String s){
        if(s == null){
            return null;
        }
        if(s.equalsIgnoreCase("yes") ){
            return Boolean.TRUE;
        }else if (s.equalsIgnoreCase("no")){
            return Boolean.FALSE;
        }else{
            throw new IllegalArgumentException("Invalid boolean value: " + s);
        }
    }

    @Override
    public String marshal(final Boolean boole) {
        if(boole == null){return null;}
        return boole ? "yes" : "no";
    }
}

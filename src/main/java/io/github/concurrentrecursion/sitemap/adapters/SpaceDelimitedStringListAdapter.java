package io.github.concurrentrecursion.sitemap.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an implementation of the XmlAdapter interface that converts a space-delimited string to a list of
 * strings and vice versa.<br>
 * The space-delimited string is split using the space character as the delimiter to create the list of strings.
 */
public class SpaceDelimitedStringListAdapter extends XmlAdapter<String, List<String>> {
    @Override
    public List<String> unmarshal(String s) throws Exception {
        if(s == null) return new ArrayList<>();

        return List.of(s.split(" "));
    }

    @Override
    public String marshal(List<String> strings) throws Exception {
        if(strings == null) return null;
        return String.join(" ", strings);
    }
}

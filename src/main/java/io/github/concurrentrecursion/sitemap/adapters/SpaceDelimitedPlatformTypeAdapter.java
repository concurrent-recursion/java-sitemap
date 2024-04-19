package io.github.concurrentrecursion.sitemap.adapters;

import io.github.concurrentrecursion.sitemap.model.google.video.Platform;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A custom XmlAdapter class that converts a space-delimited string representation of Platform Types
 * to a List of Platform.Type objects and vice versa.
 */
public class SpaceDelimitedPlatformTypeAdapter extends XmlAdapter<String, List<Platform.Type>> {
    @Override
    public List<Platform.Type> unmarshal(String s) throws Exception {
        if(s == null) return new ArrayList<>();
        return Arrays.stream(s.split(" ")).map(Platform.Type::fromValue).collect(Collectors.toList());
    }

    @Override
    public String marshal(List<Platform.Type> types) throws Exception {
        if(types == null) return null;
        return types.stream().map(Platform.Type::getValue).collect(Collectors.joining(" "));
    }
}

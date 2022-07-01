package msu.sargis.entity;

import java.util.Arrays;

public enum SportType {
    FOOTBALL("Футбол"), HOCKEY("Хоккей"), TENNIS("Теннис"), BASKETBALL("Баскетбол");

    private final String name;

    SportType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SportType getByName(String name) {
        return Arrays.stream(values())
                .filter(sportType -> sportType.getName().equals(name))
                .findFirst().get();
    }
}

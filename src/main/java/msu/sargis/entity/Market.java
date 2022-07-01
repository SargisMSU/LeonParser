package msu.sargis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
@AllArgsConstructor
@Getter @ToString
public class Market {
    private String name;
    private List<Runner> runners;
}

package msu.sargis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Runner{
    private Long id;
    private String tag;
    private Double price;
}

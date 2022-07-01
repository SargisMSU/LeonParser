package msu.sargis.entity;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @ToString
@EqualsAndHashCode(of = "id")
public class League {
    private SportType sportType;
    private long id;
    private String name;
    private String region;
}

package org.example.entity;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyEvent {

    private long id;
    private String name;
    private String date;
    private String city;
    private String building;
}

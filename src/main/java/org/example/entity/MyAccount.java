package org.example.entity;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyAccount {

    private long id;
    private String userName;
    private String password;
}

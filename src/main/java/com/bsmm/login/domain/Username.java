package com.bsmm.login.domain;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor//(onConstructor = @__({@JsonCreator}))
@ToString
public class Username implements Serializable {

    private String username;

}

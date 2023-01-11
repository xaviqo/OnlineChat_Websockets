package tech.xavi.wschatspringvue.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ROLE_USER("USER");
    private final String role;

}

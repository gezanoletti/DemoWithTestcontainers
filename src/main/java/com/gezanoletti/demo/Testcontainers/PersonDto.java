package com.gezanoletti.demo.Testcontainers;

import lombok.Value;

@Value
public class PersonDto {
    Long id;

    String firstName;

    String lastName;

    int age;
}

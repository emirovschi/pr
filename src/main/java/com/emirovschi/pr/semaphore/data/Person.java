package com.emirovschi.pr.semaphore.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Person
{
    private final String name;
    private long time;
}

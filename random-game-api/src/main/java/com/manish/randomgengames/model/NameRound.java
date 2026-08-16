package com.manish.randomgengames.model;

import java.util.List;

public record NameRound(List<String> names, int nameCount, NameMode nameMode) {

    public NameRound {
        names = List.copyOf(names);
    }
}

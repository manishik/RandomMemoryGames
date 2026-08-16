package com.manish.randomgengames.dto.name;

import com.manish.randomgengames.model.NameMode;

import java.util.List;

public record NameRoundResponse(
        String roundId,
        List<String> names,
        int nameCount,
        NameMode nameMode
) {
}

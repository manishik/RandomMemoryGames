package com.manish.randomgengames.dto.name;

import com.manish.randomgengames.model.NameMode;

public record StartNameRoundRequest(int nameCount, NameMode nameMode) {
}

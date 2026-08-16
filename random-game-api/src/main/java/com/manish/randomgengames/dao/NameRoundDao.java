package com.manish.randomgengames.dao;

import com.manish.randomgengames.model.NameRound;
import org.springframework.stereotype.Repository;

@Repository
// Stores active name-game rounds in memory.
public class NameRoundDao extends InMemoryRoundDao<NameRound> {
}

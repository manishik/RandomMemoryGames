package com.manish.randomgengames.dao;

import com.manish.randomgengames.model.NumberRound;
import org.springframework.stereotype.Repository;

@Repository
// Stores active number-game rounds in memory.
public class NumberRoundDao extends InMemoryRoundDao<NumberRound> {
}

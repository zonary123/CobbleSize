package com.kingpixel.cobblesize.events;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.kingpixel.cobblesize.Model.SizeChance;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;

/**
 * @author Carlos Varas Alonso - 07/02/2025 2:08
 */
public class SpawnEvents {
  public static void register() {
    EntityEvent.ADD.register((entity, world) -> {
      if (entity == null) return EventResult.pass();
      if (entity instanceof PokemonEntity pokemonEntity) {
        if (pokemonEntity.isPersistent()) return EventResult.pass();
        SizeChance.solveSize(pokemonEntity.getPokemon());
      }
      return EventResult.pass();
    });
  }
}

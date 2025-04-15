package com.kingpixel.cobblesize.events;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.kingpixel.cobblesize.Model.SizeChance;
import kotlin.Unit;

/**
 * @author Carlos Varas Alonso - 07/02/2025 2:08
 */
public class SpawnEvents {
  public static void register() {
    CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.NORMAL, evt -> {
      var entity = evt.getEntity();
      if (entity.isPersistent()) return Unit.INSTANCE;
      var pokemon = entity.getPokemon();
      SizeChance.solveSize(pokemon);
      return Unit.INSTANCE;
    });
/*    EntityEvent.ADD.register((entity, world) -> {
      if (entity == null) return EventResult.pass();
      if (entity instanceof PokemonEntity pokemonEntity) {
        if (pokemonEntity.isPersistent()) return EventResult.pass();
        SizeChance.solveSize(pokemonEntity.getPokemon());
      }
      return EventResult.pass();
    });*/
  }
}

package com.kingpixel.cobblesize.properties;

import com.cobblemon.mod.common.api.properties.CustomPokemonProperty;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.kingpixel.cobblesize.Model.SizeChance;
import org.jetbrains.annotations.NotNull;

/**
 * @author Carlos Varas Alonso - 04/08/2024 19:40
 */
public class ScaleProperty implements CustomPokemonProperty {
  private final Float value;

  public ScaleProperty(String value) {
    if (value != null) {
      this.value = Float.parseFloat(value);
    } else {
      this.value = 1.0f;
    }
  }

  @Override public void apply(@NotNull PokemonEntity pokemonEntity) {
    SizeChance.applySize(pokemonEntity.getPokemon(), value);
  }

  @NotNull @Override public String asString() {
    return "scale";
  }

  @Override public void apply(@NotNull Pokemon pokemon) {
    SizeChance.applySize(pokemon, value);
  }

  @Override public boolean matches(@NotNull Pokemon pokemon) {
    return true;
  }

  @Override public boolean matches(@NotNull PokemonEntity pokemonEntity) {
    return true;
  }
}

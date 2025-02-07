package com.kingpixel.cobblesize.Model;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.kingpixel.cobblesize.config.Config;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Varas Alonso - 07/02/2025 1:22
 */
@Data
public class CustomSizeChance {
  private List<String> pokemons;
  private List<SizeChance> sizes;

  public CustomSizeChance() {
    pokemons = new ArrayList<>();
    pokemons.add("magikarp");
    pokemons.add("gyarados");
    sizes = Config.defaultSize();

  }

  public CustomSizeChance(List<String> pokemons, List<SizeChance> sizes) {
    this.pokemons = pokemons;
    this.sizes = sizes;
  }

  public List<SizeChance> getSizes(Pokemon pokemon) {
    if (pokemons.contains(pokemon.showdownId())) {
      return sizes;
    }
    return null;
  }
}

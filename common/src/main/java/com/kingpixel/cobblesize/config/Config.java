package com.kingpixel.cobblesize.config;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.Gson;
import com.kingpixel.cobblesize.CobbleSize;
import com.kingpixel.cobblesize.Model.SizeChance;
import com.kingpixel.cobblesize.util.Utils;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Carlos Varas Alonso - 29/04/2024 0:14
 */
@Getter
@Data
@ToString
public class Config {
  private boolean debug;
  private List<SizeChance> pokemonSizes;

  public Config() {
    debug = false;
    pokemonSizes = new ArrayList<>();

  }

  public void init() {
    CompletableFuture<Boolean> futureRead = Utils.readFileAsync(CobbleSize.PATH, "config.json",
      el -> {
        Gson gson = Utils.newGson();
        Config config = gson.fromJson(el, Config.class);

        String data = gson.toJson(this);
        CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(CobbleSize.PATH, "config.json",
          data);
        if (!futureWrite.join()) {
          CobbleSize.LOGGER.fatal("Could not write config.json file for " + CobbleSize.MOD_NAME + ".");
        }
      });

    if (!futureRead.join()) {
      CobbleSize.LOGGER.info("No config.json file found for" + CobbleSize.MOD_NAME + ". Attempting to generate one.");
      Gson gson = Utils.newGson();
      String data = gson.toJson(this);
      CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(CobbleSize.PATH, "config.json",
        data);

      if (!futureWrite.join()) {
        CobbleSize.LOGGER.fatal("Could not write config.json file for " + CobbleSize.MOD_NAME + ".");
      }
    }

  }

  /**
   * Método para obtener un tamaño de Pokémon basado en las probabilidades
   * configuradas.
   *
   * @return El tamaño del Pokémon seleccionado según las probabilidades.
   */
  public SizeChance getRandomPokemonSize() {
    int totalWeight = pokemonsizes.stream().mapToInt(SizeChance::getChance).sum();
    int randomValue = Utils.RANDOM.nextInt(totalWeight) + 1;

    int currentWeight = 0;
    for (SizeChance sizeChance : pokemonsizes) {
      currentWeight += sizeChance.getChance();
      if (randomValue <= currentWeight) {
        return sizeChance;
      }
    }
    return new SizeChance();
  }

  public boolean isBlacklisted(Pokemon pokemon) {
    return blacklist.stream().anyMatch(pokemonData -> PokemonData.equals(pokemonData, PokemonData.from(pokemon)));
  }

  public boolean isShinyTokenBlacklisted(Pokemon pokemon) {
    return shinytokenBlacklist.stream()
      .anyMatch(pokemonData -> PokemonData.equals(pokemonData, PokemonData.from(pokemon)));
  }

  public boolean isLegendary(Pokemon pokemon) {
    return legends.stream().anyMatch(pokemonData -> PokemonData.equals(pokemonData, PokemonData.from(pokemon)));
  }

  public boolean isUltraBeast(Pokemon pokemon) {
    return ultraBeasts.stream().anyMatch(pokemonData -> PokemonData.equals(pokemonData, PokemonData.from(pokemon)));
  }

  public boolean isForm(String form) {
    return forms.contains(form);
  }
}
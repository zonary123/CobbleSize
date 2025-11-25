package com.kingpixel.cobblesize.config;

import com.google.gson.Gson;
import com.kingpixel.cobblesize.CobbleSize;
import com.kingpixel.cobblesize.Model.CustomSizeChance;
import com.kingpixel.cobblesize.Model.SizeChance;
import com.kingpixel.cobbleutils.CobbleUtils;
import com.kingpixel.cobbleutils.util.Utils;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Carlos Varas Alonso - 29/04/2024 0:14
 */
@Getter
@Data
@ToString
public class Config {
  public static Set<String> examples = new HashSet<>();
  private boolean debug;
  private List<String> commands;
  private List<SizeChance> pokemonSizes;
  private List<CustomSizeChance> customPokemonSizes;

  public Config() {
    debug = false;
    commands = new ArrayList<>();
    commands.add("cobblesize");
    commands.add("pokemonsize");
    pokemonSizes = defaultSize();
    customPokemonSizes = new ArrayList<>();
    customPokemonSizes.add(new CustomSizeChance());
  }

  public static List<SizeChance> defaultSize() {
    List<SizeChance> sizes = new ArrayList<>();
    sizes.add(new SizeChance("small", 0.5f, 10));
    sizes.add(new SizeChance("normal", 1f, 100));
    sizes.add(new SizeChance("big", 2f, 10));
    return sizes;
  }

  public void init() {
    CompletableFuture<Boolean> futureRead = Utils.readFileAsync(CobbleSize.PATH, "config.json",
      el -> {
        Gson gson = Utils.newGson();
        CobbleSize.config = gson.fromJson(el, Config.class);

        String data = gson.toJson(CobbleSize.config);
        CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(CobbleSize.PATH, "config.json",
          data);
        if (Boolean.FALSE.equals(futureWrite.join())) {
          CobbleUtils.LOGGER.fatal(CobbleSize.MOD_ID, "Could not write config.json file for " + CobbleSize.MOD_NAME +
            ".");
        }
      });

    if (Boolean.FALSE.equals(futureRead.join())) {
      CobbleUtils.LOGGER.info("No config.json file found for" + CobbleSize.MOD_NAME + ". Attempting to generate one.");
      write();
    }
    examples = CobbleSize.config.getPokemonSizes().stream().map(SizeChance::getId).collect(Collectors.toSet());
    examples.add("aleatory");
  }

  public void write() {
    Gson gson = Utils.newGson();
    String data = gson.toJson(CobbleSize.config);
    CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(CobbleSize.PATH, "config.json",
      data);

    if (Boolean.FALSE.equals(futureWrite.join())) {
      CobbleUtils.LOGGER.fatal("Could not write config.json file for " + CobbleSize.MOD_NAME + ".");
    }
  }
}
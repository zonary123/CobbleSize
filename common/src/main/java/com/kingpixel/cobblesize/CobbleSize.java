package com.kingpixel.cobblesize;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.properties.CustomPokemonProperty;
import com.kingpixel.cobblesize.Model.SizeChance;
import com.kingpixel.cobblesize.command.CommandTree;
import com.kingpixel.cobblesize.config.Config;
import com.kingpixel.cobblesize.events.SpawnEvents;
import com.kingpixel.cobblesize.properties.ScalePropertyType;
import com.kingpixel.cobblesize.properties.SizePropertyType;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import kotlin.Unit;
import net.minecraft.server.MinecraftServer;

public class CobbleSize {
  public static final String MOD_ID = "cobblesize";
  public static final String PATH = "/config/cobblesize";
  public static final String MOD_NAME = "CobbleSize";
  public static MinecraftServer server;
  public static Config config = new Config();

  public static void init() {
    events();
  }

  public static void load() {
    files();
    tasks();
    customProperties();
  }


  private static void files() {
    config.init();
  }


  private static void events() {
    files();
    tasks();
    LifecycleEvent.SERVER_LEVEL_LOAD.register(level -> server = level.getServer());

    CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> CommandTree.register(dispatcher, registry));

    LifecycleEvent.SERVER_STARTING.register(server -> customProperties());

    CobblemonEvents.POKEMON_SENT_POST.subscribe(Priority.HIGHEST, evt -> {
      var pokemonEntity = evt.getPokemonEntity();
      var battleId = pokemonEntity.getBattleId();
      if (battleId == null) return Unit.INSTANCE;
      SizeChance.solveSize(pokemonEntity.getPokemon());
      return Unit.INSTANCE;
    });

    PlayerEvent.PLAYER_JOIN.register(player -> {
      Cobblemon.INSTANCE.getStorage().getParty(player).forEach(SizeChance::solveSize);
      Cobblemon.INSTANCE.getStorage().getPC(player).forEach(SizeChance::solveSize);
    });

    SpawnEvents.register();

  }

  private static void tasks() {

  }

  private static void customProperties() {
    CustomPokemonProperty.Companion.register(SizePropertyType.getInstance());
    CustomPokemonProperty.Companion.register(ScalePropertyType.getInstance());
  }


}

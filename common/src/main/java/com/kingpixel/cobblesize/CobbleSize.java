package com.kingpixel.cobblesize;

import com.kingpixel.cobblesize.command.CommandTree;
import com.kingpixel.cobblesize.config.Config;
import com.kingpixel.cobblesize.events.SpawnEvents;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
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
  }


  private static void files() {
    config.init();
  }


  private static void events() {
    files();
    LifecycleEvent.SERVER_LEVEL_LOAD.register(level -> server = level.getServer());

    CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> CommandTree.register(dispatcher, registry));

    LifecycleEvent.SERVER_STARTED.register(server -> {
      load();
      //CustomPokemonProperty.Companion.register(SizePropertyType.getInstance());
      //CustomPokemonProperty.Companion.register(ScalePropertyType.getInstance());
    });


    PlayerEvent.PLAYER_JOIN.register(player -> {
      //Cobblemon.INSTANCE.getStorage().getParty(player).forEach(SizeChance::solveSize);
      //Cobblemon.INSTANCE.getStorage().getPC(player).forEach(SizeChance::solveSize);
    });

    SpawnEvents.register();

  }

  private static void tasks() {

  }


}

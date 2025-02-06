package com.kingpixel.cobblesize;

import com.cobblemon.mod.common.api.properties.CustomPokemonProperty;
import com.kingpixel.cobblesize.command.CommandTree;
import com.kingpixel.cobblesize.config.Config;
import com.kingpixel.cobblesize.database.DatabaseClientFactory;
import com.kingpixel.cobblesize.events.ItemRightClickEvents;
import com.kingpixel.cobblesize.events.features.FeaturesRegister;
import com.kingpixel.cobblesize.features.Features;
import com.kingpixel.cobblesize.features.breeding.config.BreedConfig;
import com.kingpixel.cobblesize.features.shops.ShopTransactions;
import com.kingpixel.cobblesize.managers.PartyManager;
import com.kingpixel.cobblesize.party.command.CommandsParty;
import com.kingpixel.cobblesize.party.config.PartyConfig;
import com.kingpixel.cobblesize.party.config.PartyLang;
import com.kingpixel.cobblesize.party.event.CreatePartyEvent;
import com.kingpixel.cobblesize.party.event.DeletePartyEvent;
import com.kingpixel.cobblesize.party.util.PartyPlaceholder;
import com.kingpixel.cobblesize.properties.BreedablePropertyType;
import com.kingpixel.cobblesize.properties.MinIvsPropertyType;
import com.kingpixel.cobblesize.properties.ScalePropertyType;
import com.kingpixel.cobblesize.properties.SizePropertyType;
import com.kingpixel.cobblesize.util.ShopExtend;
import com.kingpixel.cobblesize.util.Utils;
import com.kingpixel.cobblesize.util.UtilsLogger;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

import static com.kingpixel.cobblesize.util.EconomyUtil.setEconomyType;

public class CobbleSize extends ShopExtend {
  public static final String MOD_ID = "cobblesize";
  public static final String PATH = "/config/cobblesize";
  public static final String PATH_LANG = PATH + "/lang/";
  public static final String PATH_RANDOM = PATH + "/random/";
  public static final String PATH_PARTY = PATH + "/party/";
  public static final String PATH_PARTY_LANG = PATH_PARTY + "lang/";
  public static final String PATH_PARTY_DATA = PATH_PARTY + "data/";
  public static final String PATH_REWARDS_DATA = PATH + "/rewards/";
  public static final String PATH_BREED = PATH + "/breed/";
  public static final String PATH_BREED_DATA = PATH_BREED + "data/";
  public static final String PATH_SHOP = CobbleSize.PATH + "/shop/";
  public static final String PATH_SHOPS = PATH_SHOP + "shops/";
  public static final String PATH_BOSS = PATH + "/boss/";
  public static final UtilsLogger LOGGER = new UtilsLogger();
  public static final String MOD_NAME = "CobbleUtils";
  public static MinecraftServer server;
  public static Config config = new Config();
  public static BreedConfig breedconfig = new BreedConfig();
  public static ShopConfig shopConfig = new ShopConfig();


  // Party
  public static PartyConfig partyConfig = new PartyConfig();
  public static PartyLang partyLang = new PartyLang();
  public static PartyManager partyManager = new PartyManager();
  public static List<String> modsInUse = new ArrayList<>();


  public static void init() {
    events();
    modsInUse.add(MOD_ID);
  }

  public static void load() {
    checks();
    files(true);
    sign();
    tasks();
    Features.register();
  }

  private static void checks() {
    Utils.createDirectoryIfNeeded(PATH);
    Utils.createDirectoryIfNeeded(PATH_LANG);
    Utils.createDirectoryIfNeeded(PATH_RANDOM);
    Utils.createDirectoryIfNeeded(PATH_PARTY);
    Utils.createDirectoryIfNeeded(PATH_PARTY_LANG);
    Utils.createDirectoryIfNeeded(PATH_PARTY_DATA);
    Utils.createDirectoryIfNeeded(PATH_REWARDS_DATA);
    Utils.createDirectoryIfNeeded(PATH_BREED);
    Utils.createDirectoryIfNeeded(PATH_BREED_DATA);
  }


  private static void files(boolean shop) {
    config.init();
    language.init();
    if (!config.isDebug()) {
      config.setBoss(false);
      config.setStorageRewards(false);
    }
    shopLang.init();
    partyConfig.init();
    partyLang.init();
    breedconfig.init();
    setEconomyType();
    if (config.isApiMode()) return;
    BossConfig.init();
    DatabaseClientFactory.createDatabaseClient(config.getDatabase());
    if (shop) {
      shopConfig.init(PATH_SHOP, MOD_ID, PATH_SHOPS);
    }
  }

  private static void sign() {
    info(MOD_NAME, "1.1.3", "CobbleUtils");
    LOGGER.info("§e| §6Pokemons size: " + isActive(CobbleSize.config.isRandomsize()));
    LOGGER.info("§e| §6Shop: " + isActive(CobbleSize.config.isShops()));
    LOGGER.info("§e| §6Party: " + isActive(CobbleSize.config.isParty()));
    LOGGER.info("§e| §6Storage Rewards: " + isActive(CobbleSize.config.isStorageRewards()));
    LOGGER.info("§e| §6Breeding: " + isActive(CobbleSize.breedconfig.isActive()));
    LOGGER.info("§e| §6Supported economies: Impactor, BlanketEconomy, CobbleDollars, PebbleEconomy and Vault");
    LOGGER.info("§e+-------------------------------+");
  }

  public static void info(String mod, String version, String github) {
    LOGGER.info("§e+-------------------------------+");
    LOGGER.info("§e| §6" + mod);
    LOGGER.info("§e+-------------------------------+");
    LOGGER.info("§e| §6Version: §e" + version);
    LOGGER.info("§e| §6Author: §eZonary123");
    LOGGER.info("§e| §6Website: §9https://github.com/Zonary123/" + github);
    LOGGER.info("§e| §6Discord: §9https://discord.com/invite/fKNc7FnXpa");
    LOGGER.info("§e| §6Support: §9https://github.com/Zonary123/" + github + "/issues");
    LOGGER.info("§e| &dDonate: §9https://ko-fi.com/zonary123");
    LOGGER.info("§e+-------------------------------+");
  }

  private static void events() {
    files(false);
    LifecycleEvent.SERVER_LEVEL_LOAD.register(level -> server = level.getServer());
    if (config.isApiMode()) return;
    Utils.removeFiles(PATH_PARTY_DATA);

    CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
      CommandTree.register(dispatcher, registry);
      CommandsParty.register(dispatcher, registry);
    });

    LifecycleEvent.SERVER_STARTED.register(server -> {
      load();
      spawnRates.init();
      CustomPokemonProperty.Companion.register(MinIvsPropertyType.getInstance());
      CustomPokemonProperty.Companion.register(SizePropertyType.getInstance());
      CustomPokemonProperty.Companion.register(ScalePropertyType.getInstance());
      if (CobbleSize.breedconfig.isActive())
        CustomPokemonProperty.Companion.register(BreedablePropertyType.getInstance());
    });

    LifecycleEvent.SERVER_STOPPING.register(server -> {
      CreatePartyEvent.CREATE_PARTY_EVENT.clear();
      DeletePartyEvent.DELETE_PARTY_EVENT.clear();
    });


    PlayerEvent.PLAYER_JOIN.register(player -> {

    });


    PlayerEvent.PLAYER_QUIT.register(player -> {
      // leave party
      if (config.isParty() && partyManager.isPlayerInParty(player) && partyConfig.isTemporalParty()) {
        partyManager.leaveParty(player);
      }
      // Remove shop transactions data
      if (config.isShops()) {
        ShopTransactions.transactions.remove(player.getUuid());
      }
    });


    InteractionEvent.RIGHT_CLICK_ITEM.register(ItemRightClickEvents::register);


    // ? Add the event for fishing a pokemon
    FeaturesRegister.register();

    PartyPlaceholder.register();
  }

  private static void tasks() {


    setEconomyType();
  }


  private static String isActive(boolean active) {
    if (active) {
      return "§aActive";
    } else {
      return "§cInactive";
    }
  }
}

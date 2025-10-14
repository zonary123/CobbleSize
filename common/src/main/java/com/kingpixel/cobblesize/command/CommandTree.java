package com.kingpixel.cobblesize.command;

import com.cobblemon.mod.common.command.argument.PartySlotArgumentType;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.kingpixel.cobblesize.CobbleSize;
import com.kingpixel.cobblesize.Model.SizeChance;
import com.kingpixel.cobbleutils.api.PermissionApi;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author Carlos Varas Alonso - 10/06/2024 14:08
 */
public class CommandTree {

  public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registry) {
    for (String command : CobbleSize.config.getCommands()) {
      LiteralArgumentBuilder<ServerCommandSource> base = CommandManager.literal(command)
        .requires(source -> PermissionApi.hasPermission(source, CobbleSize.MOD_ID + ".admin", 2));
      dispatcher.register(
        base
          .then(
            CommandManager.literal("reload")
              .executes(context -> {
                CobbleSize.config.init();
                return 1;
              })
          ).then(
            CommandManager.literal("set")
              .then(
                CommandManager.argument("size", FloatArgumentType.floatArg())
                  .then(
                    CommandManager.argument("player", EntityArgumentType.player())
                      .then(
                        CommandManager.argument("pokemon", PartySlotArgumentType.Companion.partySlot())
                          .executes(context -> {
                            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                            Pokemon pokemon = PartySlotArgumentType.Companion.getPokemonOf(context, "pokemon", player);
                            float size = FloatArgumentType.getFloat(context, "size");
                            SizeChance.applySize(pokemon, size);
                            return 1;
                          })
                      )
                  )

              )
          )
      );
    }

  }


}

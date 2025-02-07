package com.kingpixel.cobblesize.Model;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.kingpixel.cobblesize.CobbleSize;
import com.kingpixel.cobbleutils.Model.CobbleUtilsTags;
import com.kingpixel.cobbleutils.util.Utils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.minecraft.nbt.NbtCompound;

import java.util.List;

/**
 * @author Carlos Varas Alonso - 13/06/2024 10:39
 */
@Getter
@Setter
@ToString
@Data
public class SizeChance {
  private String id;
  private float size;
  private int chance;

  public SizeChance() {
    this.id = "normal";
    this.size = 1f;
    this.chance = 100;
  }

  public SizeChance(float size, int chance) {
    this.size = size;
    this.chance = chance;
  }

  public SizeChance(String id, float size, int chance) {
    this.id = id;
    this.size = size;
    this.chance = chance;
  }

  private static SizeChance existSize(Pokemon pokemon) {
    NbtCompound nbt = pokemon.getPersistentData();
    String size = nbt.getString(CobbleUtilsTags.SIZE_TAG);
    if (size.equals(CobbleUtilsTags.SIZE_CUSTOM_TAG)) return new SizeChance(CobbleUtilsTags.SIZE_CUSTOM_TAG, 1f, 100);
    return getSizes(pokemon).stream().filter(
      sizeChance -> sizeChance.getId().equals(size)
    ).findFirst().orElse(null);
  }

  private static SizeChance getRandomSize(Pokemon pokemon) {
    List<SizeChance> sizes = getSizes(pokemon);
    int totalWeight = sizes.stream().mapToInt(SizeChance::getChance).sum();
    int randomWeight = Utils.RANDOM.nextInt(totalWeight);
    int currentWeight = 0;

    for (SizeChance sizeChance : sizes) {
      currentWeight += sizeChance.getChance();
      if (randomWeight < currentWeight) {
        return sizeChance;
      }
    }
    return new SizeChance();
  }

  public static List<SizeChance> getSizes(Pokemon pokemon) {
    if (pokemon == null) return CobbleSize.config.getPokemonSizes();
    List<SizeChance> sizes;
    for (CustomSizeChance customPokemonSize : CobbleSize.config.getCustomPokemonSizes()) {
      sizes = customPokemonSize.getSizes(pokemon);
      if (sizes != null) return sizes;
    }
    return CobbleSize.config.getPokemonSizes();
  }

  public static void solveSize(Pokemon pokemon) {
    SizeChance sizeChance = existSize(pokemon);
    if (sizeChance == null) sizeChance = getRandomSize(pokemon);
    if (sizeChance.getId().equals(CobbleUtilsTags.SIZE_CUSTOM_TAG)) return;
    if (pokemon.getScaleModifier() == sizeChance.getSize()) return;
    pokemon.setScaleModifier(sizeChance.getSize());
    pokemon.getPersistentData().putString(CobbleUtilsTags.SIZE_TAG, sizeChance.getId());
  }

  public static void applySize(Pokemon pokemon, String value) {
    if (value == null) return;
    if (value.equals("aleatory")) {
      solveSize(pokemon);
    } else {
      SizeChance sizeChance = getSizes(pokemon).stream().filter(
        size -> size.getId().equals(value)
      ).findFirst().orElse(null);
      if (sizeChance == null) return;
      if (sizeChance.getId().equals(CobbleUtilsTags.SIZE_CUSTOM_TAG)) return;
      if (pokemon.getScaleModifier() == sizeChance.getSize()) return;
      pokemon.setScaleModifier(sizeChance.getSize());
      pokemon.getPersistentData().putString(CobbleUtilsTags.SIZE_TAG, sizeChance.getId());
    }
  }

  public static void applySize(Pokemon pokemon, Float value) {
    pokemon.setScaleModifier(value);
    pokemon.getPersistentData().putString(CobbleUtilsTags.SIZE_TAG, CobbleUtilsTags.SIZE_CUSTOM_TAG);
  }
}

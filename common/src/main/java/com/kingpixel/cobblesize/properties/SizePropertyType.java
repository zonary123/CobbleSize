package com.kingpixel.cobblesize.properties;

import com.cobblemon.mod.common.api.properties.CustomPokemonPropertyType;
import com.kingpixel.cobblesize.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Carlos Varas Alonso - 04/08/2024 19:40
 */
public class SizePropertyType implements CustomPokemonPropertyType<SizeProperty> {
  public static final SizePropertyType INSTANCE = new SizePropertyType();

  public SizePropertyType() {
    // TODO document why this constructor is empty
  }

  public static SizePropertyType getInstance() {
    return INSTANCE;
  }

  @NotNull @Override public Iterable<String> getKeys() {
    return Collections.singleton("size");
  }


  @Nullable @Override public SizeProperty fromString(@Nullable String s) {
    return new SizeProperty(s);
  }

  @NotNull @Override public Collection<String> examples() {
    return Config.examples;
  }

  @Override public boolean getNeedsKey() {
    return true;
  }
}

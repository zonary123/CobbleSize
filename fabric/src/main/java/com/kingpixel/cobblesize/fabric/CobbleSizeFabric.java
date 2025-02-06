package com.kingpixel.cobblesize.fabric;

import com.kingpixel.cobblesize.CobbleSize;
import net.fabricmc.api.ModInitializer;

public class CobbleSizeFabric implements ModInitializer {
  @Override
  public void onInitialize() {
    CobbleSize.init();
  }
}

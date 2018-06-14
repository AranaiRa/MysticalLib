package epicsquid.mysticallib.proxy;

import java.lang.reflect.InvocationTargetException;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.MysticalLib;
import epicsquid.mysticallib.event.RegisterColorHandlersEvent;
import epicsquid.mysticallib.event.RegisterParticleEvent;
import epicsquid.mysticallib.fx.EffectManager;
import epicsquid.mysticallib.hax.Hax;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.particle.ParticleRenderer;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
  public static ParticleRenderer particleRenderer = new ParticleRenderer();

  @Override
  public void preInit(FMLPreInitializationEvent event) {
    super.preInit(event);

    try {
      Hax.init();
    } catch (InstantiationException | IllegalAccessException | NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
    }

    LibRegistry.registerEntityRenders();
    OBJLoader.INSTANCE.addDomain(MysticalLib.MODID);
    MinecraftForge.EVENT_BUS.register(new EffectManager());
    ModelLoaderRegistry.registerLoader(new CustomModelLoader());
    MinecraftForge.EVENT_BUS.post(new RegisterParticleEvent());
  }

  @Override
  public void init(FMLInitializationEvent event) {
    super.init(event);
    MinecraftForge.EVENT_BUS.post(new RegisterColorHandlersEvent());
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    super.postInit(event);
  }
}

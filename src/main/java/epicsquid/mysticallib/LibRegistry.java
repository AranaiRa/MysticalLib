package epicsquid.mysticallib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.block.BlockSlabBase;
import epicsquid.mysticallib.block.IBlock;
import epicsquid.mysticallib.block.INoCullBlock;
import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.mysticallib.event.RegisterCustomModelsEvent;
import epicsquid.mysticallib.event.RegisterParticleEvent;
import epicsquid.mysticallib.model.BakedModelColorWrapper;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.model.ICustomModeledObject;
import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.mysticallib.model.block.BakedModelBlockUnlitWrapper;
import epicsquid.mysticallib.particle.ParticleRegistry;
import epicsquid.mysticallib.particle.particles.*;
import epicsquid.mysticallib.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LibRegistry {
  private static ArrayList<Item> items = new ArrayList<Item>();
  private static ArrayList<Block> blocks = new ArrayList<Block>();
  private static ArrayList<SoundEvent> sounds = new ArrayList<>();
  private static Map<Class<? extends Entity>, IRenderFactory> entityRenderMap = new HashMap<Class<? extends Entity>, IRenderFactory>();
  private static Map<Class<? extends TileEntity>, TileEntitySpecialRenderer> tileEntityRenderMap = new HashMap<Class<? extends TileEntity>, TileEntitySpecialRenderer>();
  private static int entityId = 0;

  private static String activeModid = "";

  public static String getActiveModid() {
    return activeModid;
  }

  /**
   * Test method for the registry
   */
  public static void initAll() {
    ModContainer container = Loader.instance().activeModContainer();
    MinecraftForge.EVENT_BUS.post(new RegisterContentEvent(items, blocks, sounds));
    setActiveMod(MysticalLib.MODID, container);
  }

  @SideOnly(Side.CLIENT)
  public static void registerEntityRenderer(@Nonnull Class<? extends Entity> entity, @Nonnull IRenderFactory render) {
    entityRenderMap.put(entity, render);
  }

  @SideOnly(Side.CLIENT)
  public static void registerTileRenderer(@Nonnull Class<? extends TileEntity> entity, @Nonnull TileEntitySpecialRenderer render) {
    tileEntityRenderMap.put(entity, render);
  }

  public static void addSlabPair(Material material, SoundType type, float hardness, String name, IBlockState parent, Block[] refs, boolean customModels,
      CreativeTabs tab) {
    BlockSlabBase double_slab = new BlockSlabBase(material, type, hardness, name + "_double_slab", parent, true, null).setModelCustom(customModels);
    BlockSlabBase slab = new BlockSlabBase(material, type, hardness, name + "_slab", parent, false, double_slab).setModelCustom(customModels);
    double_slab.slab = slab;
    slab.setCreativeTab(tab);
    refs[0] = slab;
    refs[1] = double_slab;
    blocks.add(slab);
    blocks.add(double_slab);
  }

  public static void addSlabPair(Material material, SoundType type, float hardness, String name, IBlockState parent, Block[] refs, boolean customModels) {
    BlockSlabBase double_slab = new BlockSlabBase(material, type, hardness, name + "_double_slab", parent, true, null).setModelCustom(customModels);
    BlockSlabBase slab = new BlockSlabBase(material, type, hardness, name + "_slab", parent, false, double_slab).setModelCustom(customModels);
    double_slab.slab = slab;
    refs[0] = slab;
    refs[1] = double_slab;
    blocks.add(slab);
    blocks.add(double_slab);
  }

  public static void registerEntity(Class<? extends Entity> entity, int eggColor1, int eggColor2) {
    String[] nameParts = entity.getTypeName().split("\\.");
    String className = nameParts[nameParts.length - 1];
    EntityRegistry.registerModEntity(new ResourceLocation(activeModid + ":" + Util.lowercase(className)), entity, Util.lowercase(className), entityId++,
        MysticalLib.INSTANCE, 64, 1, true, eggColor1, eggColor2);
  }

  public static void registerEntity(Class<? extends Entity> entity) {
    String[] nameParts = entity.getTypeName().split("\\.");
    String className = nameParts[nameParts.length - 1];
    EntityRegistry.registerModEntity(new ResourceLocation(activeModid + ":" + Util.lowercase(className)), entity, Util.lowercase(className), entityId++,
        MysticalLib.INSTANCE, 64, 1, true);
  }

  /**
   * Sets the current mod to register the blocks/items/entities/tes under
   *
   * @param modid     ID Of the mod (e.g. "MysticalLib")
   * @param container The instance of the ModContainer
   */
  public static void setActiveMod(@Nonnull String modid, @Nonnull ModContainer container) {
    activeModid = modid;
    Loader.instance().setActiveModContainer(container);
  }

  @SubscribeEvent
  public void registerItems(@Nonnull RegistryEvent.Register<Item> event) {
    for (Item i : items) {
      event.getRegistry().register(i);
    }
    for (Block b : blocks) {
      if (b instanceof IBlock) {
        Item i = ((IBlock) b).getItemBlock();
        if (i != null && i != Items.AIR) {
          event.getRegistry().register(i);
        }
      }
    }
  }

  @SubscribeEvent
  public void registerBlocks(@Nonnull RegistryEvent.Register<Block> event) {
    for (Block b : blocks) {
      event.getRegistry().register(b);
    }
  }

  @SubscribeEvent
  public void registerSounds(@Nonnull RegistryEvent.Register<SoundEvent> event) {
    for (SoundEvent s: sounds) {
      event.getRegistry().register(s);
    }
  }

  private static Set<ModelResourceLocation> noCullMRLs = new HashSet<>();
  private static Set<ModelResourceLocation> colorMRLs = new HashSet<>();

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void registerRendering(@Nonnull ModelRegistryEvent event) {
    for (Item i : items) {
      if (i instanceof IModeledObject) {
        ((IModeledObject) i).initModel();
      }
    }
    for (Block b : blocks) {
      if (b instanceof IModeledObject) {
        ((IModeledObject) b).initModel();
      }
    }
  }

  /**
   * Call in preInit to register Entity and TE rendering
   */
  public static void registerEntityRenders() {
    for (Entry<Class<? extends Entity>, IRenderFactory> e : entityRenderMap.entrySet()) {
      RenderingRegistry.registerEntityRenderingHandler(e.getKey(), e.getValue());
    }
    for (Entry<Class<? extends TileEntity>, TileEntitySpecialRenderer> e : tileEntityRenderMap.entrySet()) {
      ClientRegistry.bindTileEntitySpecialRenderer(e.getKey(), e.getValue());
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onRegisterCustomModels(@Nonnull RegisterCustomModelsEvent event) {
    for (Block b : blocks) {
      if (b instanceof ICustomModeledObject) {
        ((ICustomModeledObject) b).initCustomModel();
      }
    }
    for (Item i : items) {
      if (i instanceof ICustomModeledObject) {
        ((ICustomModeledObject) i).initCustomModel();
      }
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onTextureStitch(@Nonnull TextureStitchEvent event) {
    for (Entry<String, ResourceLocation> e : ParticleRegistry.particleTextures.entrySet()) {
      event.getMap().registerSprite(e.getValue());
    }
    for (Entry<ResourceLocation, IModel> e : CustomModelLoader.itemmodels.entrySet()) {
      for (ResourceLocation r : e.getValue().getTextures()) {
        event.getMap().registerSprite(r);
      }
    }
  }

  public class RegisterTintedModelsEvent extends Event {
    public void addModel(ModelResourceLocation mrl) {
      colorMRLs.add(mrl);
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onModelBake(@Nonnull ModelBakeEvent event) {
    MinecraftForge.EVENT_BUS.post(new RegisterTintedModelsEvent());
    noCullMRLs.clear();
    for (Block b : blocks) {
      if (b instanceof IModeledObject) {
        if (b instanceof INoCullBlock && ((INoCullBlock) b).noCull()) {
          noCullMRLs.addAll(event.getModelManager().getBlockModelShapes().getBlockStateMapper().getVariants(b).values());
        }
      }
    }

    for (ResourceLocation r : CustomModelLoader.itemmodels.keySet()) {
      ModelResourceLocation mrl = new ModelResourceLocation(r.toString().replace("#handlers", ""), "handlers");
      IBakedModel bakedModel = event.getModelRegistry().getObject(mrl);
      if (bakedModel != null) {
        IModel m = CustomModelLoader.itemmodels.get(r);
        event.getModelRegistry().putObject(mrl, m.bake(m.getDefaultState(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter()));
      }
    }

    for (ModelResourceLocation mrl : event.getModelRegistry().getKeys()) {
      if (noCullMRLs.contains(mrl)) {
        event.getModelRegistry().putObject(mrl, new BakedModelBlockUnlitWrapper(event.getModelManager().getModel(mrl)));
      }
      if (colorMRLs.contains(mrl)) {
        event.getModelRegistry().putObject(mrl, new BakedModelColorWrapper(event.getModelManager().getModel(mrl)));
      }
    }
  }

  public static String PARTICLE_GLOW, PARTICLE_SMOKE, PARTICLE_SPARK, PARTICLE_GLITTER, PARTICLE_FLAME;
  public static String PARTICLE_WHITE_RING;

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onRegisterCustomModels(@Nonnull RegisterParticleEvent event) {
    PARTICLE_GLOW = ParticleRegistry.registerParticle(MysticalLib.MODID, ParticleGlow.class, new ResourceLocation("mysticallib:particle/particle_glow"));
    PARTICLE_SMOKE = ParticleRegistry.registerParticle(MysticalLib.MODID, ParticleSmoke.class, new ResourceLocation("mysticallib:particle/particle_smoke"));
    PARTICLE_SPARK = ParticleRegistry.registerParticle(MysticalLib.MODID, ParticleSpark.class, new ResourceLocation("mysticallib:particle/particle_sparkle"));
    PARTICLE_GLITTER = ParticleRegistry
        .registerParticle(MysticalLib.MODID, ParticleGlitter.class, new ResourceLocation("mysticallib:particle/particle_sparkle"));
    PARTICLE_FLAME = ParticleRegistry.registerParticle(MysticalLib.MODID, ParticleFlame.class, new ResourceLocation("mysticallib:particle/particle_fire"));

    PARTICLE_WHITE_RING = ParticleRegistry.registerParticle(MysticalLib.MODID, ParticleExample.class, new ResourceLocation("mysticallib:particle/particle_ring_white_16x"));
  }
}

package epicsquid.mysticallib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.entity.IDelayedEntityRenderer;
import epicsquid.mysticallib.gui.IHUDContainer;
import epicsquid.mysticallib.network.MessageLeftClickEmpty;
import epicsquid.mysticallib.network.MessageTEUpdate;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.particle.ParticleRegistry;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.tile.IDelayedTileRenderer;
import epicsquid.mysticallib.util.FluidTextureUtil;
import epicsquid.mysticallib.world.GenerationData;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LibEvents {

  public static boolean acceptUpdates = true;

  public static Map<BlockPos, TileEntity> toUpdate = new HashMap<BlockPos, TileEntity>();
  public static Map<BlockPos, TileEntity> overflow = new HashMap<BlockPos, TileEntity>();

  public static int ticks = 0;

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onTextureStitchPre(TextureStitchEvent.Pre event) {
    FluidTextureUtil.initTextures(event.getMap());
  }

  // TODO: FIX THIS FIX THIS FIX THIS
  public static void markForUpdate(@Nonnull BlockPos pos, @Nonnull TileEntity tile) {
    if (!tile.getWorld().isRemote && acceptUpdates) {
      if (!toUpdate.containsKey(pos)) {
        toUpdate.put(pos, tile);
      } else {
        toUpdate.replace(pos, tile);
      }
    } else if (!tile.getWorld().isRemote) {
      if (!overflow.containsKey(pos)) {
        overflow.put(pos, tile);
      } else {
        overflow.replace(pos, tile);
      }
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onTextureStitch(TextureStitchEvent event) {
    for (Entry<String, ResourceLocation> e : ParticleRegistry.particleTextures.entrySet()) {
      event.getMap().registerSprite(e.getValue());
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      ticks++;
      ClientProxy.particleRenderer.updateParticles();
    }
  }

  @SubscribeEvent
  // TODO: FIX THIS FIX THIS FIX THIS
  public void onServerTick(TickEvent.WorldTickEvent event) {
    if (!event.world.isRemote && event.phase == TickEvent.Phase.END) {
      NBTTagList list = new NBTTagList();
      // TODO: WHY WOULD UPDATES... ARGH NETWORK PACKETS???
      // TODO: JUST WHY
      // TODO: ... I DON'T EVEN KNOW
      acceptUpdates = false;
      TileEntity[] updateArray = toUpdate.values().toArray(new TileEntity[0]);
      acceptUpdates = true;
      for (Entry<BlockPos, TileEntity> e : overflow.entrySet()) {
        toUpdate.put(e.getKey(), e.getValue());
      }
      overflow.clear();
      for (int i = 0; i < updateArray.length; i++) {
        TileEntity t = updateArray[i];
        list.appendTag(t.getUpdateTag());
      }
      if (!list.isEmpty()) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("data", list);
        // THIS ONE CAN STAY ~ Noob
        // TODO: NO THIS REALLY CAN'T
        // TODO: IT DOESN'T CHECK DIMENSIONS???
        PacketHandler.INSTANCE.sendToAll(new MessageTEUpdate(tag));
      }
      toUpdate.clear();
    }
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void onRenderAfterWorld(RenderWorldLastEvent event) {
    if (MysticalLib.proxy instanceof ClientProxy) {
      GlStateManager.pushMatrix();
      ClientProxy.particleRenderer.renderParticles(Minecraft.getMinecraft() != null ? Minecraft.getMinecraft().player : null, event.getPartialTicks());
      GlStateManager.popMatrix();
      if (Minecraft.getMinecraft().world != null) {
        List<TileEntity> list = Minecraft.getMinecraft().world.loadedTileEntityList;
        GlStateManager.pushMatrix();
        for (int i = 0; i < list.size(); i++) {
          TileEntitySpecialRenderer render = TileEntityRendererDispatcher.instance.getRenderer(list.get(i));
          if (render instanceof IDelayedTileRenderer) {
            double x = Minecraft.getMinecraft().player.lastTickPosX + Minecraft.getMinecraft().getRenderPartialTicks() * (Minecraft.getMinecraft().player.posX
                - Minecraft.getMinecraft().player.lastTickPosX);
            double y = Minecraft.getMinecraft().player.lastTickPosY + Minecraft.getMinecraft().getRenderPartialTicks() * (Minecraft.getMinecraft().player.posY
                - Minecraft.getMinecraft().player.lastTickPosY);
            double z = Minecraft.getMinecraft().player.lastTickPosZ + Minecraft.getMinecraft().getRenderPartialTicks() * (Minecraft.getMinecraft().player.posZ
                - Minecraft.getMinecraft().player.lastTickPosZ);
            GlStateManager.translate(-x, -y, -z);
            ((IDelayedTileRenderer) render).renderLater(list.get(i), list.get(i).getPos().getX(), list.get(i).getPos().getY(), list.get(i).getPos().getZ(),
                Minecraft.getMinecraft().getRenderPartialTicks());
            GlStateManager.translate(x, y, z);
          }
        }
        GlStateManager.popMatrix();
        List<Entity> entityList = Minecraft.getMinecraft().world.loadedEntityList;
        GlStateManager.pushMatrix();
        for (int i = 0; i < entityList.size(); i++) {
          Render render = Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(entityList.get(i).getClass());
          if (render instanceof IDelayedEntityRenderer) {
            renderEntityStatic(entityList.get(i), Minecraft.getMinecraft().getRenderPartialTicks(), true, render);
          }
        }
        GlStateManager.popMatrix();
      }
    }
  }

  @SideOnly(Side.CLIENT)
  public static void renderEntityStatic(@Nonnull Entity entityIn, float partialTicks, boolean b, @Nonnull Render render) {
    if (entityIn.ticksExisted == 0) {
      entityIn.lastTickPosX = entityIn.posX;
      entityIn.lastTickPosY = entityIn.posY;
      entityIn.lastTickPosZ = entityIn.posZ;
    }

    double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
    double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
    double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;
    float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
    int i = entityIn.getBrightnessForRender();

    if (entityIn.isBurning()) {
      i = 15728880;
    }

    int j = i % 65536;
    int k = i / 65536;
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    ((IDelayedEntityRenderer) render).renderLater(entityIn, -TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY,
        -TileEntityRendererDispatcher.staticPlayerZ, f, partialTicks);
  }

  Int2IntOpenHashMap dimCounts = new Int2IntOpenHashMap();

  @SubscribeEvent
  public void onWorldTick(WorldTickEvent event) {
    if (event.phase == TickEvent.Phase.START) {
      int dim = event.world.provider.getDimension();
      if (!dimCounts.containsKey(dim)) {
        dimCounts.put(dim, 0);
      } else {
        int val = dimCounts.get(dim);
        if (val + 1 >= 20) {
          dimCounts.put(dim, 0);
          GenerationData data = GenerationData.get(event.world);
          data.update(event.world);
        } else {
          dimCounts.put(dim, val + 1);
        }
      }

    }
  }

  @SubscribeEvent
  public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
    PacketHandler.INSTANCE.sendToServer(new MessageLeftClickEmpty(event.getEntityPlayer(), event.getHand(), event.getItemStack()));
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onGameOverlayRender(RenderGameOverlayEvent.Post e) {
    int w = e.getResolution().getScaledWidth();
    int h = e.getResolution().getScaledHeight();
    EntityPlayer player = Minecraft.getMinecraft().player;
    World world = player.getEntityWorld();
    RayTraceResult result = player.rayTrace(6.0, e.getPartialTicks());

    if (result != null) {
      if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
        TileEntity tile = world.getTileEntity(result.getBlockPos());
        if (tile instanceof IHUDContainer) {
          ((IHUDContainer) tile).addHUD(w, h);
        }
      }
    }
  }
}

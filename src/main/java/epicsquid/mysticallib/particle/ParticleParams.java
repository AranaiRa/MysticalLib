package epicsquid.mysticallib.particle;

import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;

/**
 * Convenience class to give names to various particle parameters.
 * Not all parameters are used by all particles.
 */
public class ParticleParams {
    private static final int TOTAL_PARAM_COUNT = 7;

    public static final int
    MAXIMUM_LIFESPAN = 0,
    TINT_RED = 1,
    TINT_GREEN = 2,
    TINT_BLUE = 3,
    ALPHA = 4,
    SCALE = 5,
    THETA = 6;


    public Class clazz;
    public Vec3d position, speed, tint;
    public float alpha, scale, theta;
    public int maxAge;

    public ParticleParams(Class clazz) {
        this.clazz = clazz;
        this.position = new Vec3d(0,0,0);
        this.speed = new Vec3d(0,0,0);
        this.tint = new Vec3d(1,1,1);
        this.alpha = 1.0f;
        this.scale = 1.0f;
        this.theta = 0.0f;
    }

    public ParticleParams(Class clazz, int maxAge, Vec3d position, Vec3d speed, Vec3d tint, float alpha, float scale) {
        this.clazz = clazz;
        this.maxAge = maxAge;
        this.position = position;
        this.speed = speed;
        this.tint = tint;
        this.alpha = alpha;
        this.scale = scale;
        this.theta = 0.0f;
    }

    public void spawnParticle(World world) {
        double[] data = new double[TOTAL_PARAM_COUNT];
        data[MAXIMUM_LIFESPAN] = maxAge;
        data[TINT_RED] = tint.x;
        data[TINT_GREEN] = tint.y;
        data[TINT_BLUE] = tint.z;
        data[ALPHA] = alpha;
        data[SCALE] = scale;
        data[THETA] = theta;

        ClientProxy.particleRenderer.spawnParticle(world,
                Util.getLowercaseClassName(clazz),
                position.x, position.y, position.z,
                speed.x, speed.y, speed.z,
                data);
    }
}

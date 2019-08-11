package epicsquid.mysticallib.particle;

import epicsquid.mysticallib.MysticalLib;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

/**
 * Convenience class to give names to various particle parameters.
 * Not all parameters are used by all particles.
 */
public class ParticleParams {
    public static Random random = new Random();

    private static final int TOTAL_PARAM_COUNT = 13;
    private ArrayList<ParticleEmitter> subemitter = new ArrayList<>();

    //How long in ticks the particle lasts before disappearing.
    public static final int MAXIMUM_LIFESPAN = 0;
    //What color to multiply the particle texture by.
    public static final int TINT_RED = 1;
    public static final int TINT_GREEN = 2;
    public static final int TINT_BLUE = 3;
    //What alpha the particle begins at.
    public static final int ALPHA = 4;
    //What scale the particle begins at.
    public static final int SCALE = 5;
    //A value used for starting positions in some ParticleDynamics patterns. Not all particles will use this value.
    public static final int THETA = 6;
    //How long in ticks until the particle begins its lifetime.
    public static final int PARTICLE_DELAY = 7;
    //How long in ticks until the subemitters, if any, begin.
    public static final int SUBEMITTER_DELAY = 8;
    //What rotation in radians the particle begins at.
    public static final int ANGLE_START = 9;
    //How quickly in radians the particle rotates per tick.
    public static final int ANGLE_SPEED = 10;
    //How much speed the particle loses when it collides with a surface and bounces. 1.0 is no loss, 0.0 is a complete stop. Only particles capable of collision can use this value.
    public static final int BOUNCINESS = 11;
    //How much the particle is affected by gravity. 1.0 is normal.
    public static final int GRAVITY = 12;

    protected Class clazz;
    protected Vec3d position, speed, tint;
    protected float alpha, scale, theta, angleStart, angleSpeed, bounciness, gravity;
    protected int maxAge, particleDelay, subEmitterDelay;

    public ParticleParams(Class clazz) {
        this.clazz = clazz;
        this.position = new Vec3d(0,0,0);
        this.speed = new Vec3d(0,0,0);
        this.tint = new Vec3d(1,1,1);
        this.alpha = 1.0f;
        this.scale = 1.0f;
        this.theta = 0.0f;
        this.particleDelay = 0;
        this.subEmitterDelay = 0;
        this.angleStart = 0.0f;
        this.angleSpeed = 0.0f;
        this.bounciness = 0.5f;
        this.gravity = 0.0f;
    }

    public ParticleParams(Class clazz, int maxAge, Vec3d position, Vec3d speed, float alpha, float scale) {
        this.clazz = clazz;
        this.maxAge = maxAge;
        this.position = position;
        this.speed = speed;
        this.tint = new Vec3d(1,1,1);
        this.alpha = alpha;
        this.scale = scale;
        this.theta = 0.0f;
        this.particleDelay = 0;
        this.subEmitterDelay = 0;
        this.angleStart = 0.0f;
        this.angleSpeed = 0.0f;
        this.bounciness = 0.5f;
        this.gravity = 0.0f;
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
        data[PARTICLE_DELAY] = particleDelay;
        data[SUBEMITTER_DELAY] = subEmitterDelay;
        data[ANGLE_START] = angleStart;
        data[ANGLE_SPEED] = angleSpeed;
        data[BOUNCINESS] = bounciness;
        data[GRAVITY] = gravity;

        MysticalLib.logger.info("param speed="+speed);

        ParticleBase pba = ClientProxy.particleRenderer.spawnParticle(world,
                Util.getLowercaseClassName(clazz),
                position.x, position.y, position.z,
                speed.x, speed.y, speed.z,
                data);

        if(subemitter.size() != 0) {
            if(pba instanceof ParticleAdvanced) {
                ((ParticleAdvanced) pba).subemitters = (ParticleAdvanced[]) subemitter.toArray();
            }
            else
                MysticalLib.logger.warn("WARNING: Can only attach a sub-emitter to ParticleAdvanced and its subclasses. Aborting.");
        }
    }

    public Class getParticleClass() {
        return clazz;
    }

    public void setParticleClass(Class clazz) {
        this.clazz = clazz;
    }

    public Vec3d getPosition() {
        return position;
    }

    public void setPosition(Vec3d value) {
        this.position = value;
    }

    public void setPosition(BlockPos value) {
        this.position = new Vec3d(value.getX(), value.getY(), value.getZ());
    }

    public void setPosition(double x, double y, double z) {
        this.position = new Vec3d(x, y, z);
    }

    public Vec3d getSpeed() {
        return speed;
    }

    public void setSpeed(Vec3d value) {
        this.speed = value;
    }

    public void setSpeed(BlockPos value) {
        this.speed = new Vec3d(value.getX(), value.getY(), value.getZ());
    }

    public void setSpeed(double x, double y, double z) {
        this.speed = new Vec3d(x, y, z);
    }

    public ParticleEmitter[] getSubEmitters() {
        return (ParticleEmitter[])subemitter.toArray();
    }

    public void addSubEmitter(ParticleEmitter emitter) {
        subemitter.add(emitter);
    }

    public int getLifespan() {
        return maxAge;
    }

    public void setLifespan(int value) {
        this.maxAge = value;
    }

    public void setLifespan(int minValue, int maxValue) {
        this.maxAge = (int) (random.nextFloat() * ((float)maxValue - (float)minValue) + (float)minValue);
    }

    public Vec3d getTint() {
        return tint;
    }

    public void setTint(double r, double g, double b) {
        new Vec3d(r, g, b);
    }

    public void setTint(double minR, double maxR, double minG, double maxG, double minB, double maxB) {
        double r = random.nextDouble() * (maxR - minR) + minR;
        double g = random.nextDouble() * (maxG - minG) + minG;
        double b = random.nextDouble() * (maxB - minB) + minB;
        this.tint = new Vec3d(r, g, b);
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float value) {
        this.alpha = value;
    }

    public void setAlpha(float minValue, float maxValue) {
        this.alpha = random.nextFloat() * (maxValue - minValue) + minValue;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float value) {
        this.scale = value;
    }

    public void setScale(float minValue, float maxValue) {
        this.scale = random.nextFloat() * (maxValue - minValue) + minValue;
    }

    public float getTheta() {
        return theta;
    }

    public void setTheta(float value) {
        this.theta = value;
    }

    public void setTheta(float minValue, float maxValue) {
        this.theta = random.nextFloat() * (maxValue - minValue) + minValue;
    }

    public float getOnsetDelay() {
        return particleDelay;
    }

    public void setOnsetDelay(int value) {
        this.particleDelay = value;
    }

    public void setOnsetDelay(int minValue, int maxValue) {
        this.particleDelay = (int) (random.nextFloat() * ((float)maxValue - (float)minValue) + (float)minValue);
    }

    public int getSubemitterDelay() {
        return subEmitterDelay;
    }

    public void setSubemitterDelay(int value) {
        this.subEmitterDelay = value;
    }

    public void setSubEmitterDelay(int minValue, int maxValue) {
        this.subEmitterDelay = (int) (random.nextFloat() * ((float)maxValue - (float)minValue) + (float)minValue);
    }

    //TODO: Figure out why these are flipping the fuck out
    /*public float getStartAngle() {
        return angleStart;
    }

    public void setStartAngle(float value) {
        this.angleStart = value;
    }

    public void setStartAngle(float minValue, float maxValue) {
        this.angleStart = random.nextFloat() * (maxValue - minValue) + minValue;
    }

    public float getRotationSpeed() {
        return angleSpeed;
    }

    public void setRotationSpeed(float value) {
        this.angleSpeed = value;
    }

    public void setRotationSpeed(float minValue, float maxValue) {
        this.angleSpeed = random.nextFloat() * (maxValue - minValue) + minValue;
    }*/

    //TODO: Figure out particle collisions
    /*public float getGravityMultiplier() {
        return gravity;
    }

    public void setGravityMultiplier(float value) {
        this.gravity = value;
    }

    public void setGravityMultiplier(float minValue, float maxValue) {
        this.gravity = random.nextFloat() * (maxValue - minValue) + minValue;
    }

    public float getBounciness() {
        return bounciness;
    }

    public void setBounciness(float value) {
        this.bounciness = value;
    }

    public void setBounciness(float minValue, float maxValue) {
        this.bounciness = random.nextFloat() * (maxValue - minValue) + minValue;
    }*/
}

package epicsquid.mysticallib.particle;

import epicsquid.mysticallib.MysticalLib;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class ParticleAdvanced extends ParticleBase {
    public static Random random = new Random();

    protected Vec3d actualPosition, actualSpeed;
    protected float initAlpha, initScale, initTheta, bounciness, gravity, actualAngle;
    protected float gravitySpeed, angleSpeed;
    protected int emitterBeginAge;
    protected ParticleAdvanced[] subemitters;

    public ParticleAdvanced(@Nonnull World world, double x, double y, double z, double vx, double vy, double vz, double[] data) {
        super(world, x, y, z, vx, vy, vz, data);
        this.canCollide = true;
        this.particleMaxAge = (int) data[ParticleParams.MAXIMUM_LIFESPAN];
        actualPosition = new Vec3d(x, y, z);
        actualSpeed = new Vec3d(vx, vy, vz);
        float colorR = (float) data[ParticleParams.TINT_RED];
        float colorG = (float) data[ParticleParams.TINT_GREEN];
        float colorB = (float) data[ParticleParams.TINT_BLUE];
        if (colorR > 1.0) {
            colorR = colorR / 255.0f;
        }
        if (colorG > 1.0) {
            colorG = colorG / 255.0f;
        }
        if (colorB > 1.0) {
            colorB = colorB / 255.0f;
        }
        this.setRBGColorF(colorR, colorG, colorB);
        this.initAlpha = (float) data[ParticleParams.ALPHA];
        this.initScale = (float) data[ParticleParams.SCALE];
        this.initTheta = (float) data[ParticleParams.THETA];
        this.particleAge = (int) -data[ParticleParams.PARTICLE_DELAY];
        this.emitterBeginAge = (int) data[ParticleParams.SUBEMITTER_DELAY];
        this.actualAngle = (float) data[ParticleParams.ANGLE_START];
        this.angleSpeed = (float) data[ParticleParams.ANGLE_SPEED];
        this.bounciness = (float) data[ParticleParams.BOUNCINESS];
        this.gravity = (float) data[ParticleParams.GRAVITY];
        this.gravitySpeed = 0.0f;

        this.particleScale = initScale;
    }

    @Override
    public void onUpdate() {
        if(particleAge < 0) {
            particleAge++;
        }
        else {
            super.onUpdate();
            //MysticalLib.logger.info("===\ngrounded?:"+onGround+"\npos:"+actualPosition+"\nspd:"+actualSpeed);
            gravitySpeed += gravity * 0.049f;
            actualPosition = actualPosition.add(actualSpeed);
            actualPosition = actualPosition.add(new Vec3d(0, -gravitySpeed, 0));
            Vec3d offset = getOffset();
            this.posX = actualPosition.x + offset.x;
            this.posY = actualPosition.y + offset.y;
            this.posZ = actualPosition.z + offset.z;
            actualAngle += angleSpeed;
            this.particleAngle = actualAngle;
        }
    }

    private static Vec3d getRandomDirection() {
        float x = random.nextFloat() - 0.5f;
        float y = random.nextFloat() - 0.5f;
        float z = random.nextFloat() - 0.5f;

        float magnitude = (float) Math.sqrt(x*x + y*y + z*z);
        x /= magnitude;
        y /= magnitude;
        z /= magnitude;

        return new Vec3d(x, y, z);
    }

    public Vec3d getOffset() {
        return new Vec3d(0,0,0);
    }

    protected float getAgeCoefficient() {
        return (float)this.particleAge / (float)this.particleMaxAge;
    }
}

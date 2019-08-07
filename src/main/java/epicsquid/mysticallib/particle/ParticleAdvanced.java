package epicsquid.mysticallib.particle;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class ParticleAdvanced extends ParticleBase {
    public static Random random = new Random();

    public Vec3d actualPosition, actualSpeed;
    public float initAlpha, initScale, initTheta;
    public int emitterBeginAge;
    public ParticleAdvanced[] subemitters;

    public ParticleAdvanced(@Nonnull World world, double x, double y, double z, double vx, double vy, double vz, double[] data) {
        super(world, x, y, z, vx, vy, vz, data);
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

        this.particleScale = initScale;
    }

    @Override
    public void onUpdate() {
        if(particleAge < 0) {
            particleAge++;
        }
        else {
            super.onUpdate();
            actualPosition = actualPosition.add(actualSpeed);
            Vec3d offset = getOffset();
            this.posX = actualPosition.x + offset.x;
            this.posY = actualPosition.y + offset.y;
            this.posZ = actualPosition.z + offset.z;
            //MysticalLib.logger.info(this.particleAngle);

            if(particleAge == emitterBeginAge) {

            }
        }
    }

    public Vec3d getOffset() {
        return new Vec3d(0,0,0);
    }

    protected float getAgeCoefficient() {
        return (float)this.particleAge / (float)this.particleMaxAge;
    }
}

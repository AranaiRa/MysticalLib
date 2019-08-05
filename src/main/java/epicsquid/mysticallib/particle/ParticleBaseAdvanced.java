package epicsquid.mysticallib.particle;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class ParticleBaseAdvanced extends ParticleBase {
    public static Random random = new Random();

    public Vec3d actualPosition, actualSpeed;
    public float initAlpha, initScale, initTheta;

    public ParticleBaseAdvanced(@Nonnull World world, double x, double y, double z, double vx, double vy, double vz, double[] data) {
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

        this.particleScale = initScale;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        actualPosition = actualPosition.add(actualSpeed);
        Vec3d offset = getOffset();
        this.posX = actualPosition.x + offset.x;
        this.posY = actualPosition.y + offset.y;
        this.posZ = actualPosition.z + offset.z;
    }

    public Vec3d getOffset() {
        return new Vec3d(0,0,0);
    }

    protected float getAgeCoefficient() {
        return (float)this.particleAge / (float)this.particleMaxAge;
    }
}

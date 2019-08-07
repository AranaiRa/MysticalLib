package epicsquid.mysticallib.particle.particles;

import epicsquid.mysticallib.particle.ParticleAdvanced;
import epicsquid.mysticallib.particle.ParticleDynamics;
import epicsquid.mysticallib.util.BlendingUtil;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * An example of the type of stuff you can do with a ParticleAdvanced
 */
public class ParticleExample extends ParticleAdvanced {
    float heightJitter, radiusJitter, revJitter, scaleJitter;

    public ParticleExample(@Nonnull World world, double x, double y, double z, double vx, double vy, double vz, double[] data) {
        super(world, x, y, z, vx, vy, vz, data);
        this.particleAngle = 0.18f;//(float) (random.nextFloat() * 0.12 - 0.06);
        heightJitter = random.nextFloat() * 0.5f;
        radiusJitter = random.nextFloat() * 0.5f - 0.25f;
        revJitter = random.nextFloat();
        scaleJitter = random.nextFloat() * 0.25f;
    }

    @Override
    public Vec3d getOffset() {
        return ParticleDynamics.vortex(getAgeCoefficient(), 0.0625f, 1.5f+radiusJitter, 3.0f+heightJitter, initTheta, 2+revJitter, true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.particleAlpha = BlendingUtil.quadraticHump(0, initAlpha, getAgeCoefficient());
        this.particleScale = initScale * (1.0f - getAgeCoefficient()) + scaleJitter;
    }

    @Override
    public boolean isAdditive() {
        return true;
    }
}

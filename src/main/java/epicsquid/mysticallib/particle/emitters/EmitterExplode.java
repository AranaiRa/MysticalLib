package epicsquid.mysticallib.particle.emitters;

import epicsquid.mysticallib.MysticalLib;
import epicsquid.mysticallib.particle.ParticleBaseAdvanced;
import epicsquid.mysticallib.particle.ParticleEmitter;
import epicsquid.mysticallib.particle.ParticleParams;
import epicsquid.mysticallib.struct.Vec3f;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EmitterExplode extends ParticleEmitter
{
    public int count;
    public float minRadius, maxRadius;

    public EmitterExplode(Class clazz, Vec3d position) {
        super(clazz, position);
        minRadius = 0;
        maxRadius = 1;
        count = 40;
    }

    @Override
    public void createParticles(World world) {
        particles.clear();
        for(int i=0;i<count;i++) {
            Vec3d pos = position.add(getRandomPointInSphereOuterShell(random.nextFloat() * (maxRadius - minRadius) + minRadius));
            ParticleParams params = new ParticleParams(clazz, maxAge, pos, speed, alpha, scale);
            params.setLifespan(40,90);
            params.setTheta(0, (float)Math.PI * 2.0f);
            params.setScale(1.0f, 2.0f);
            params.setAlpha(0.85f, 1.0f);
            params.setOnsetDelay(0, 20);
            params.setTint(0.3, 1.0, 0.3, 1.0, 0.3, 1.0);
            params.setSpeed(new Vec3d(0.0, 0.0, 3.5));
            particles.add(params);
        }

        for(ParticleParams pba : particles) {
            pba.spawnParticle(world);
        }
    }

    public static Vec3d getRandomPointInSphereOuterShell(float radius) {
        float x = random.nextFloat() - 0.5f;
        float y = random.nextFloat() - 0.5f;
        float z = random.nextFloat() - 0.5f;

        float magnitude = (float) Math.sqrt(x*x + y*y + z*z);
        x /= magnitude;
        y /= magnitude;
        z /= magnitude;

        return new Vec3d(x * radius, y * radius, z * radius);
    }
}

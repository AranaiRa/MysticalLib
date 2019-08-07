package epicsquid.mysticallib.particle;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

/**
 * Used to create a bunch of particles in a structured manner.
 * By AranaiRa
 */
public abstract class ParticleEmitter {

    protected Class clazz;
    protected ArrayList<ParticleParams> particles = new ArrayList<>();
    protected static Random random = new Random();

    public int count, maxAge;
    public float scale, alpha;
    public Vec3d position, speed;

    public ParticleEmitter(Class clazz, Vec3d position) {
        this.clazz = clazz;
        count = 12;
        maxAge = 40;
        alpha = 1.0f;
        this.position = position;
        speed = Vec3d.ZERO;
    }

    public ParticleEmitter() {
    }

    public void createParticles(World world) {

    }

    public ArrayList<ParticleParams> getParticleParams() { return particles; }
}

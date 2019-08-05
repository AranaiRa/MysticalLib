package epicsquid.mysticallib.particle;

import net.minecraft.util.math.Vec3d;

/**
 * Static class used to generate particle movement patterns to be added as an offset to the particle's actual position
 * By: AranaiRa
 */
public class ParticleDynamics {

    /**
     * Generates a vertically spiraling pattern
     * @param lifespanCoefficient 0..1 value of how far into its lifespan a particle is
     * @param minRadius Minimum radius of the particle's position
     * @param maxRadius Maximum radius of the particle's position
     * @param heightGain How high up the particle reaches at the end of its lifespan
     * @param startAngle The angle in radians that the particle starts its journey from
     * @param revolutions How many revolutions around a circle the particle makes
     * @param rotateClockwise If true, rotates clockwise, if false rotates counter-clockwise
     * @return
     */
    public static Vec3d vortex(float lifespanCoefficient, float minRadius, float maxRadius, float heightGain, float startAngle, float revolutions, boolean rotateClockwise) {
        float y = lifespanCoefficient * heightGain;
        float rotDir = 1.0f;
        if(rotateClockwise) rotDir *= -1;
        float theta = (float) (lifespanCoefficient * Math.PI * revolutions * rotDir) + startAngle;
        float radius = (maxRadius - minRadius) * lifespanCoefficient + minRadius;
        float x = (float) (Math.cos(theta) * radius);
        float z = (float) (Math.sin(theta) * radius);
        return new Vec3d(x, y, z);
    }
}

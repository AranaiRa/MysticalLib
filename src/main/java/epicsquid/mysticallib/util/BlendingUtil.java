package epicsquid.mysticallib.util;

import epicsquid.mysticallib.struct.Vec2d;
import epicsquid.mysticallib.struct.Vec3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

/**
 * Adds static blending functions used for interpolating two values.
 * By AranaiRa
 */
public class BlendingUtil {
    //Reference for the appearance of blend functions:  https://thebookofshaders.com/05/kynd.png

    private static float clampToExtents(float in) {
        if (in < -1) in = 1;
        if (in > 1) in = 1;
        return in;
    }

    /**
     * Transforms a mix float from [-1.0 - 1.0] space to [0.0 - 1.0] space.
     * @param in The mix range to convert
     * @return
     */
    public static float convertMixRange2ZeroToOne (float in) {
        in *= 2.0f - 1.0f;
        return in;
    }

    /**
     * Transforms a mix float from [0.0 - 1.0] space to [-1.0 - 1.0] space.
     * @param in The mix range to convert
     * @return
     */
    public static float convertMixRange2NegativeOneToOne (float in) {
        in += 1.0f;
        in *= 0.5f;
        return in;
    }

    /**
     * Evaluates a blending function with sharp in and out points and some kind of easing around 0.
     * See the reference image linked at the top of BlendingUtil.java; this function can replicate all of the graphs on the top row.
     *
     * @param left  The value when the expression evaluates to 0.0
     * @param right The value when the expression evaluates to 1.0
     * @param power The power that the expression is raised to. 1.0 is linear, <1 is concave, >1 is convex
     * @param mix   A value between -1.0 and 1.0 fed into the expression. -1 or 1 will always evaluate to 0, 0 will always evaluate to 1.
     * @return
     */
    public static double oneMinusAbsolutePower(double left, double right, float power, float mix) {
        mix = clampToExtents(mix);
        double point = 1.0 - Math.pow(Math.abs(mix), power);
        return (right - left) * point + left;
    }

    public static float oneMinusAbsolutePower(float left, float right, float power, float mix) {
        return (float) oneMinusAbsolutePower((double) left, (double) right, power, mix);
    }

    public static Vec2f oneMinusAbsolutePower(Vec2f left, Vec2f right, float power, float mix) {
        float x = oneMinusAbsolutePower(left.x, right.x, power, mix);
        float y = oneMinusAbsolutePower(left.y, right.y, power, mix);
        return new Vec2f(x, y);
    }

    public static Vec2d oneMinusAbsolutePower(Vec2d left, Vec2d right, float power, float mix) {
        double x = oneMinusAbsolutePower(left.x, right.x, power, mix);
        double y = oneMinusAbsolutePower(left.y, right.y, power, mix);
        return new Vec2d(x, y);
    }

    public static Vec3f oneMinusAbsolutePower(Vec3f left, Vec3f right, float power, float mix) {
        float x = oneMinusAbsolutePower(left.x, right.x, power, mix);
        float y = oneMinusAbsolutePower(left.y, right.y, power, mix);
        float z = oneMinusAbsolutePower(left.z, right.z, power, mix);
        return new Vec3f(x, y, z);
    }

    public static Vec3d oneMinusAbsolutePower(Vec3d left, Vec3d right, float power, float mix) {
        double x = oneMinusAbsolutePower(left.x, right.x, power, mix);
        double y = oneMinusAbsolutePower(left.y, right.y, power, mix);
        double z = oneMinusAbsolutePower(left.z, right.z, power, mix);
        return new Vec3d(x, y, z);
    }

    public static BlockPos oneMinusAbsolutePower(BlockPos left, BlockPos right, float power, float mix) {
        double x = oneMinusAbsolutePower((double)left.getX(), (double)right.getX(), power, mix);
        double y = oneMinusAbsolutePower((double)left.getY(), (double)right.getY(), power, mix);
        double z = oneMinusAbsolutePower((double)left.getZ(), (double)right.getZ(), power, mix);
        return new BlockPos(x, y, z);
    }

    /**
     * Evaluates a blending function using a cosine-based function.
     * See the reference image linked at the top of BlendingUtil.java; this function can replicate all of the graphs on the second row.
     *
     * @param left  The value when the expression evaluates to 0.0
     * @param right The value when the expression evaluates to 1.0
     * @param power The power that the expression is raised to. 0.5 is roughly circular, 1.0 is roughly parabolic, 2.0 and above estimates a bell curve with harsher easing as the value increases.
     * @param mix   A value between -1.0 and 1.0 fed into the expression. -1 or 1 will always evaluate to 0, 0 will always evaluate to 1.
     * @return
     */
    public static double exponentialCosine(double left, double right, float power, float mix) {
        mix = clampToExtents(mix);
        double point = Math.pow(Math.cos(Math.PI * mix / 2.0), power);
        return (right - left) * point + left;
    }

    public static float exponentialCosine(float left, float right, float power, float mix) {
        return (float) exponentialCosine((double) left, (double) right, power, mix);
    }

    public static Vec2f exponentialCosine(Vec2f left, Vec2f right, float power, float mix) {
        float x = exponentialCosine(left.x, right.x, power, mix);
        float y = exponentialCosine(left.y, right.y, power, mix);
        return new Vec2f(x, y);
    }

    public static Vec2d exponentialCosine(Vec2d left, Vec2d right, float power, float mix) {
        double x = exponentialCosine(left.x, right.x, power, mix);
        double y = exponentialCosine(left.y, right.y, power, mix);
        return new Vec2d(x, y);
    }

    public static Vec3f exponentialCosine(Vec3f left, Vec3f right, float power, float mix) {
        float x = exponentialCosine(left.x, right.x, power, mix);
        float y = exponentialCosine(left.y, right.y, power, mix);
        float z = exponentialCosine(left.z, right.z, power, mix);
        return new Vec3f(x, y, z);
    }

    public static Vec3d exponentialCosine(Vec3d left, Vec3d right, float power, float mix) {
        double x = exponentialCosine(left.x, right.x, power, mix);
        double y = exponentialCosine(left.y, right.y, power, mix);
        double z = exponentialCosine(left.z, right.z, power, mix);
        return new Vec3d(x, y, z);
    }

    public static BlockPos exponentialCosine(BlockPos left, BlockPos right, float power, float mix) {
        double x = exponentialCosine((double)left.getX(), (double)right.getX(), power, mix);
        double y = exponentialCosine((double)left.getY(), (double)right.getY(), power, mix);
        double z = exponentialCosine((double)left.getZ(), (double)right.getZ(), power, mix);
        return new BlockPos(x, y, z);
    }

    /**
     * Evaluates a blending function using a sine-based function.
     * See the reference image linked at the top of BlendingUtil.java; this function can replicate all of the graphs on the third row.
     *
     * @param left  The value when the expression evaluates to 0.0
     * @param right The value when the expression evaluates to 1.0
     * @param power The power that the expression is raised to. 0.5 is roughly inverse circular, 1.0 has a sharp corner at eval:0 and easing at eval:-1 and eval:1, 2.0 and above estimates a bell curve with shallower easing as the value increases.
     * @param mix   A value between -1.0 and 1.0 fed into the expression. -1 or 1 will always evaluate to 0, 0 will always evaluate to 1.
     * @return
     */
    public static double oneMinusExponentialSine(double left, double right, float power, float mix) {
        mix = clampToExtents(mix);
        double point = Math.pow(Math.abs(Math.sin(Math.PI * mix / 2.0)), power);
        return (right - left) * point + left;
    }

    public static float oneMinusExponentialSine(float left, float right, float power, float mix) {
        return (float) oneMinusExponentialSine((double) left, (double) right, power, mix);
    }

    public static Vec2f oneMinusExponentialSine(Vec2f left, Vec2f right, float power, float mix) {
        float x = oneMinusExponentialSine(left.x, right.x, power, mix);
        float y = oneMinusExponentialSine(left.y, right.y, power, mix);
        return new Vec2f(x, y);
    }

    public static Vec2d oneMinusExponentialSine(Vec2d left, Vec2d right, float power, float mix) {
        double x = oneMinusExponentialSine(left.x, right.x, power, mix);
        double y = oneMinusExponentialSine(left.y, right.y, power, mix);
        return new Vec2d(x, y);
    }

    public static Vec3f oneMinusExponentialSine(Vec3f left, Vec3f right, float power, float mix) {
        float x = oneMinusExponentialSine(left.x, right.x, power, mix);
        float y = oneMinusExponentialSine(left.y, right.y, power, mix);
        float z = oneMinusExponentialSine(left.z, right.z, power, mix);
        return new Vec3f(x, y, z);
    }

    public static Vec3d oneMinusExponentialSine(Vec3d left, Vec3d right, float power, float mix) {
        double x = oneMinusExponentialSine(left.x, right.x, power, mix);
        double y = oneMinusExponentialSine(left.y, right.y, power, mix);
        double z = oneMinusExponentialSine(left.z, right.z, power, mix);
        return new Vec3d(x, y, z);
    }

    public static BlockPos oneMinusExponentialSine(BlockPos left, BlockPos right, float power, float mix) {
        double x = oneMinusExponentialSine((double)left.getX(), (double)right.getX(), power, mix);
        double y = oneMinusExponentialSine((double)left.getY(), (double)right.getY(), power, mix);
        double z = oneMinusExponentialSine((double)left.getZ(), (double)right.getZ(), power, mix);
        return new BlockPos(x, y, z);
    }

    /**
     * Evaluates a blending function using a minimized comparison between a cosine or an absolute function.
     * See the reference image linked at the top of BlendingUtil.java; this function can replicate all of the graphs on the fourth row.
     *
     * @param left  The value when the expression evaluates to 0.0
     * @param right The value when the expression evaluates to 1.0
     * @param power The power that the expression is raised to. All values have a sharp corner at eval:0, <1.0 has harsh easing, 1.0 is linear, and >1.0 adds progressively heavier easing to eval:-1 and eval:1
     * @param mix   A value between -1.0 and 1.0 fed into the expression. -1 or 1 will always evaluate to 0, 0 will always evaluate to 1.
     * @return
     */
    public static double exponentialMinimizedCosine(double left, double right, float power, float mix) {
        mix = clampToExtents(mix);
        double point = Math.pow(Math.min(Math.cos(Math.PI * mix / 2.0), 1.0 - (Math.abs(mix))), power);
        return (right - left) * point + left;
    }

    public static float exponentialMinimizedCosine(float left, float right, float power, float mix) {
        return (float) exponentialMinimizedCosine((double) left, (double) right, power, mix);
    }

    public static Vec2f exponentialMinimizedCosine(Vec2f left, Vec2f right, float power, float mix) {
        float x = exponentialMinimizedCosine(left.x, right.x, power, mix);
        float y = exponentialMinimizedCosine(left.y, right.y, power, mix);
        return new Vec2f(x, y);
    }

    public static Vec2d exponentialMinimizedCosine(Vec2d left, Vec2d right, float power, float mix) {
        double x = exponentialMinimizedCosine(left.x, right.x, power, mix);
        double y = exponentialMinimizedCosine(left.y, right.y, power, mix);
        return new Vec2d(x, y);
    }

    public static Vec3f exponentialMinimizedCosine(Vec3f left, Vec3f right, float power, float mix) {
        float x = exponentialMinimizedCosine(left.x, right.x, power, mix);
        float y = exponentialMinimizedCosine(left.y, right.y, power, mix);
        float z = exponentialMinimizedCosine(left.z, right.z, power, mix);
        return new Vec3f(x, y, z);
    }

    public static Vec3d exponentialMinimizedCosine(Vec3d left, Vec3d right, float power, float mix) {
        double x = exponentialMinimizedCosine(left.x, right.x, power, mix);
        double y = exponentialMinimizedCosine(left.y, right.y, power, mix);
        double z = exponentialMinimizedCosine(left.z, right.z, power, mix);
        return new Vec3d(x, y, z);
    }

    public static BlockPos exponentialMinimizedCosine(BlockPos left, BlockPos right, float power, float mix) {
        double x = exponentialMinimizedCosine((double)left.getX(), (double)right.getX(), power, mix);
        double y = exponentialMinimizedCosine((double)left.getY(), (double)right.getY(), power, mix);
        double z = exponentialMinimizedCosine((double)left.getZ(), (double)right.getZ(), power, mix);
        return new BlockPos(x, y, z);
    }

    /**
     * Evaluates a blending function that results in the right value for half of the function's lifespan (-0.5 to 0.5).
     * See the reference image linked at the top of BlendingUtil.java; this function can replicate all of the graphs on the bottom row.
     *
     * @param left  The value when the expression evaluates to 0.0
     * @param right The value when the expression evaluates to 1.0
     * @param power The power that the expression is raised to. Values below 1.0 are concave, 1.0 is linear, values above 1.0 are concave with progressively heavier easing towards the eval:0 value
     * @param mix   A value between -1.0 and 1.0 fed into the expression. -1 or 1 will always evaluate to 0, 0 will always evaluate to 1.
     * @return
     */
    public static double maximizedPlateau(double left, double right, float power, float mix) {
        mix = clampToExtents(mix);
        double point = 1.0 - Math.pow(Math.max(0.0, Math.abs(mix) * 2.0 - 1.0), power);
        return (right - left) * point + left;
    }

    public static float maximizedPlateau(float left, float right, float power, float mix) {
        return (float) maximizedPlateau((double) left, (double) right, power, mix);
    }

    public static Vec2f maximizedPlateau(Vec2f left, Vec2f right, float power, float mix) {
        float x = maximizedPlateau(left.x, right.x, power, mix);
        float y = maximizedPlateau(left.y, right.y, power, mix);
        return new Vec2f(x, y);
    }

    public static Vec2d maximizedPlateau(Vec2d left, Vec2d right, float power, float mix) {
        double x = maximizedPlateau(left.x, right.x, power, mix);
        double y = maximizedPlateau(left.y, right.y, power, mix);
        return new Vec2d(x, y);
    }

    public static Vec3f maximizedPlateau(Vec3f left, Vec3f right, float power, float mix) {
        float x = maximizedPlateau(left.x, right.x, power, mix);
        float y = maximizedPlateau(left.y, right.y, power, mix);
        float z = maximizedPlateau(left.z, right.z, power, mix);
        return new Vec3f(x, y, z);
    }

    public static Vec3d maximizedPlateau(Vec3d left, Vec3d right, float power, float mix) {
        double x = maximizedPlateau(left.x, right.x, power, mix);
        double y = maximizedPlateau(left.y, right.y, power, mix);
        double z = maximizedPlateau(left.z, right.z, power, mix);
        return new Vec3d(x, y, z);
    }

    public static BlockPos maximizedPlateau(BlockPos left, BlockPos right, float power, float mix) {
        double x = maximizedPlateau((double)left.getX(), (double)right.getX(), power, mix);
        double y = maximizedPlateau((double)left.getY(), (double)right.getY(), power, mix);
        double z = maximizedPlateau((double)left.getZ(), (double)right.getZ(), power, mix);
        return new BlockPos(x, y, z);
    }
}

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

    private static float clampToRestrictedExtents(float in) {
        if (in < 0) in = 0;
        if (in > 1) in = 1;
        return in;
    }

    /**
     * Generates a function that bounces back and forth from 0 to 1 linearly.
     * @param in The time of the function
     * @return A value between 0..1
     */
    public static float zigZag(float in) {
        float a = -1.0f + 2.0f * ((float)Math.floor(in) % 2);
        float b = -((float)Math.floor(in) % 2);
        float c = in - (float)Math.floor(in);
        return Math.abs(a*c+b);
    }

    public static float zigZag(double in) {
        float a = (float) (-1.0f + 2.0f * (Math.floor(in) % 2.0));
        float b = (float) -(Math.floor(in) % 2.0f);
        return (float) -((in - Math.floor(in)) * a + b);
    }

    public static float zigZag(long in) {
        float a = (float) (-1.0 + 2.0 * (Math.floor(in) % 2.0));
        float b = (float) -(Math.floor(in) % 2.0f);
        return (float) -((in - Math.floor(in)) * a + b);
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

    /**
     * Evaluates a blending function from 0..1 that eases in the middle for a bit
     * @param left  The value when the expression evaluates to 0.0
     * @param right The value when the expression evaluates to 1.0
     * @param mix   A value between 0.0 and 1.0 fed into the expression. 0 or will always evaluate very close to 0, 1 will always evaluate very close to 1.
     * @return
     */
    public static double cubicHoldZeroToOne(double left, double right, float mix) {
        mix = clampToRestrictedExtents(mix);
        double point = (mix - 0.5) * Math.PI * 0.5;
        double out = (right-left) * point + left;
        return out;
    }

    public static float cubicHoldZeroToOne(float left, float right, float mix) {
        return (float) cubicHoldZeroToOne((double) left, (double) right, mix);
    }

    public static Vec2f cubicHoldZeroToOne(Vec2f left, Vec2f right, float power, float mix) {
        float x = cubicHoldZeroToOne(left.x, right.x, mix);
        float y = cubicHoldZeroToOne(left.y, right.y, mix);
        return new Vec2f(x, y);
    }

    public static Vec2d cubicHoldZeroToOne(Vec2d left, Vec2d right, float mix) {
        double x = cubicHoldZeroToOne(left.x, right.x, mix);
        double y = cubicHoldZeroToOne(left.y, right.y, mix);
        return new Vec2d(x, y);
    }

    public static Vec3f cubicHoldZeroToOne(Vec3f left, Vec3f right, float mix) {
        float x = cubicHoldZeroToOne(left.x, right.x, mix);
        float y = cubicHoldZeroToOne(left.y, right.y, mix);
        float z = cubicHoldZeroToOne(left.z, right.z, mix);
        return new Vec3f(x, y, z);
    }

    public static Vec3d cubicHoldZeroToOne(Vec3d left, Vec3d right, float mix) {
        double x = cubicHoldZeroToOne(left.x, right.x, mix);
        double y = cubicHoldZeroToOne(left.y, right.y, mix);
        double z = cubicHoldZeroToOne(left.z, right.z, mix);
        return new Vec3d(x, y, z);
    }

    public static BlockPos cubicHoldZeroToOne(BlockPos left, BlockPos right, float mix) {
        double x = cubicHoldZeroToOne((double)left.getX(), (double)right.getX(), mix);
        double y = cubicHoldZeroToOne((double)left.getY(), (double)right.getY(), mix);
        double z = cubicHoldZeroToOne((double)left.getZ(), (double)right.getZ(), mix);
        return new BlockPos(x, y, z);
    }

    /**
     * Evaluates a blending function from 0..1 that eases harshly toward maximum before dipping back down to zero
     * @param left  The value when the expression evaluates to 0.0
     * @param right The value when the expression evaluates to 1.0
     * @param mix   A value between 0.0 and 1.0 fed into the expression. 0 or 1 or will always evaluate to 0, 0.5 will always evaluate to 1.
     * @return
     */
    public static double quarticHump(double left, double right, float mix) {
        mix = clampToRestrictedExtents(mix);
        double point = -Math.pow((mix - 0.5) * 2.0, 4.0) + 1.0;
        double out = (right-left) * point + left;
        return out;
    }

    public static float quarticHump(float left, float right, float mix) {
        return (float) quarticHump((double) left, (double) right, mix);
    }

    public static Vec2f quarticHump(Vec2f left, Vec2f right, float power, float mix) {
        float x = quarticHump(left.x, right.x, mix);
        float y = quarticHump(left.y, right.y, mix);
        return new Vec2f(x, y);
    }

    public static Vec2d quarticHump(Vec2d left, Vec2d right, float mix) {
        double x = quarticHump(left.x, right.x, mix);
        double y = quarticHump(left.y, right.y, mix);
        return new Vec2d(x, y);
    }

    public static Vec3f quarticHump(Vec3f left, Vec3f right, float mix) {
        float x = quarticHump(left.x, right.x, mix);
        float y = quarticHump(left.y, right.y, mix);
        float z = quarticHump(left.z, right.z, mix);
        return new Vec3f(x, y, z);
    }

    public static Vec3d quarticHump(Vec3d left, Vec3d right, float mix) {
        double x = quarticHump(left.x, right.x, mix);
        double y = quarticHump(left.y, right.y, mix);
        double z = quarticHump(left.z, right.z, mix);
        return new Vec3d(x, y, z);
    }

    public static BlockPos quarticHump(BlockPos left, BlockPos right, float mix) {
        double x = quarticHump((double)left.getX(), (double)right.getX(), mix);
        double y = quarticHump((double)left.getY(), (double)right.getY(), mix);
        double z = quarticHump((double)left.getZ(), (double)right.getZ(), mix);
        return new BlockPos(x, y, z);
    }
}

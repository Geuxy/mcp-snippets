
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

@UtilityClass
public class RotationUtil {

    public static float[] getRotationsToVec(Vec3 vec) {
        return getRotationsToVec(vec, 100);
    }

    public static float[] getRotationsToVec(Vec3 from, Vec3 to) {
        return getRotationsToVec(from, to, 100);
    }

    public static float[] getRotationsToVec(Vec3 vec, float speed) {
        Vec3 eyePos = Minecraft.getMinecraft().thePlayer.getPositionEyes(1F);

        float[] rots = getRotationsToVec(eyePos, vec, speed);

        return fixRotations(rots, speed);
    }

    public static float[] getRotationsToVec(Vec3 from, Vec3 to, float speed) {
        double xDiff = to.xCoord - from.xCoord;
        double yDiff = to.yCoord - from.yCoord;
        double zDiff = to.zCoord - from.zCoord;

        double diffSqrt = Math.sqrt(xDiff * xDiff + zDiff * zDiff);

        float yaw = (float) ((Math.atan2(zDiff, xDiff) * 180 / Math.PI) - 90);
        float pitch = (float) (-(Math.atan2(yDiff, diffSqrt) * 180 / Math.PI));

        return fixRotations(new float[] {yaw, pitch}, speed);
    }

    public static float[] fixRotations(float[] rotations, float speed) {
        // This just gets my current aim / rotations, replace with whatever you have.
        float[] current = Client.getInstance().getAimProcessor().getAim();

        // 180 Wrap fix
        rotations = updateRotations(current, rotations);

        return capRotations(current, rotations, speed);
    }

    // Stupid name, but it limits how fast the rotations can move
    public static float[] capRotations(float[] current, float[] rotations, float speed) {
        float limitedYaw = capRotation(current[0], rotations[0], speed);
        float limitedPitch = capRotation(current[1], rotations[1], speed);

        return new float[] {limitedYaw, limitedPitch};
    }

    // Stupid name, but it limits how fast the rotations can move
    public static float capRotation(float current, float rotation, float speed) {
        if(speed >= 100) {
            return rotation;
        }

        float delta = getRotationDelta(rotation, current);
        float limited = delta > speed ? speed : Math.max(delta, -speed);

        return current + limited;
    }

    public static float[] updateRotations(float[] current, float[] rotations) {
        float updatedYaw = updateRotation(current[0], rotations[0]);
        float updatedPitch = updateRotation(current[1], rotations[1]);

        return new float[] {updatedYaw, updatedPitch};
    }

    public static float updateRotation(float current, float rotations) {
        return current + MathHelper.wrapAngleTo180_float(rotations - current);
    }

    private static float getRotationDelta(float oldRotation, float newRotation) {
        // works but tries to 360 no-scope half the time
        // return (((oldRotation - newRotation) % 360) + 180) % 360 - 180;
        // fixed version ??
        return ((((oldRotation - newRotation) % 360F) + 540) % 360) - 180;
    }

}

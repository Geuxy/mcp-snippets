
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.*;

import java.util.List;

@UtilityClass
public class RaycastUtil {

    /**
     * NOTE: Some methods may need to be modified, this can be done by:
     * - Editing minecrafts code (change methods visibility from 'protected' or 'private' to 'public'
     * - Using access widener (fabric)
     */
    public static MovingObjectPosition raycast(float range, float yaw, float pitch) {
        Minecraft mc = Minecraft.getMinecraft();

        if(mc.thePlayer != null && mc.theWorld != null) {
            Vec3 playerEyePos = mc.thePlayer.getPositionEyes(1f);
            Vec3 lookVec = mc.thePlayer.getVectorForRotation(pitch, yaw);
            Vec3 rayEnd = playerEyePos.addVector(lookVec.xCoord * range, lookVec.yCoord * range, lookVec.zCoord * range);

            MovingObjectPosition objectMouseOver = rayTrace(range, yaw, pitch);
            Vec3 hitVec = objectMouseOver != null ? objectMouseOver.hitVec : null;

            double minDistance = Double.MAX_VALUE;

            Entity hitEntity = null;

            if(objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                hitEntity = objectMouseOver.entityHit;
                minDistance = playerEyePos.distanceTo(hitVec);
            }

            List<Entity> entities = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().addCoord(lookVec.xCoord * range, lookVec.yCoord * range, lookVec.zCoord * range).expand(1, 1, 1));

            for(Entity entity : entities) {
                if(entity.canBeCollidedWith()) {
                    AxisAlignedBB boundingBox = entity.getEntityBoundingBox().expand(entity.getCollisionBorderSize(), entity.getCollisionBorderSize(), entity.getCollisionBorderSize());

                    MovingObjectPosition movingObj = boundingBox.calculateIntercept(playerEyePos, rayEnd);

                    if(boundingBox.isVecInside(playerEyePos)) {
                        if(0 < minDistance || minDistance == 0) {
                            hitEntity = entity;
                            hitVec = playerEyePos;
                            minDistance = 0;
                        }
                    } else if(movingObj != null) {
                        double distance = playerEyePos.distanceTo(movingObj.hitVec);

                        if(distance < minDistance || minDistance == 0) {
                            if(entity != mc.thePlayer.ridingEntity) {
                                hitEntity = entity;
                                hitVec = movingObj.hitVec;
                                minDistance = distance;
                            }
                        }
                    }
                }
            }

            if(hitEntity != null && (minDistance < range || objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(hitEntity, hitVec);

                if(hitEntity instanceof EntityLivingBase || hitEntity instanceof EntityItemFrame) {
                    return objectMouseOver;
                }
            }
            return objectMouseOver;
        }
        return null;
    }

    // TODO: Find way to ignore blocks to allow through walls
    private static MovingObjectPosition rayTrace(double range, float yaw, float pitch) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        final Vec3 vec3 = player.getPositionEyes(1F);
        final Vec3 vec31 = player.getVectorForRotation(pitch, yaw);
        final Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);
        return player.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
    }

}

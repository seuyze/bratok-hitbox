package com.bratok.hitbox.client.mixin;

import com.bratok.hitbox.client.BratokHitboxConfig;
import com.bratok.hitbox.client.HitboxMode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class BratokHitboxRendererMixin {

    private static void addEdge(BufferBuilder buf, Matrix4f pose, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a) {
        float nx = x2 - x1, ny = y2 - y1, nz = z2 - z1;
        float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len < 1.0E-6f) return;
        nx /= len; ny /= len; nz /= len;
        buf.addVertex(pose, x1, y1, z1).setColor(r, g, b, a).setNormal(nx, ny, nz);
        buf.addVertex(pose, x2, y2, z2).setColor(r, g, b, a).setNormal(nx, ny, nz);
    }

        private static void drawThickBox(PoseStack poseStack, VertexConsumer consumer, AABB aabb, float r, float g, float b, float a, float lineWidth, boolean fill) {
        if (fill) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.setShader(CoreShaders.POSITION_COLOR);
            BufferBuilder buf = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            
            Matrix4f pose = poseStack.last().pose();
            float minX = (float) aabb.minX; float minY = (float) aabb.minY; float minZ = (float) aabb.minZ;
            float maxX = (float) aabb.maxX; float maxY = (float) aabb.maxY; float maxZ = (float) aabb.maxZ;

            buf.addVertex(pose, minX, minY, minZ).setColor(r, g, b, a);
            buf.addVertex(pose, maxX, minY, minZ).setColor(r, g, b, a);
            buf.addVertex(pose, maxX, minY, maxZ).setColor(r, g, b, a);
            buf.addVertex(pose, minX, minY, maxZ).setColor(r, g, b, a);

            buf.addVertex(pose, minX, maxY, minZ).setColor(r, g, b, a);
            buf.addVertex(pose, minX, maxY, maxZ).setColor(r, g, b, a);
            buf.addVertex(pose, maxX, maxY, maxZ).setColor(r, g, b, a);
            buf.addVertex(pose, maxX, maxY, minZ).setColor(r, g, b, a);

            buf.addVertex(pose, minX, minY, minZ).setColor(r, g, b, a);
            buf.addVertex(pose, minX, maxY, minZ).setColor(r, g, b, a);
            buf.addVertex(pose, maxX, maxY, minZ).setColor(r, g, b, a);
            buf.addVertex(pose, maxX, minY, minZ).setColor(r, g, b, a);

            buf.addVertex(pose, minX, minY, maxZ).setColor(r, g, b, a);
            buf.addVertex(pose, maxX, minY, maxZ).setColor(r, g, b, a);
            buf.addVertex(pose, maxX, maxY, maxZ).setColor(r, g, b, a);
            buf.addVertex(pose, minX, maxY, maxZ).setColor(r, g, b, a);

            buf.addVertex(pose, minX, minY, minZ).setColor(r, g, b, a);
            buf.addVertex(pose, minX, minY, maxZ).setColor(r, g, b, a);
            buf.addVertex(pose, minX, maxY, maxZ).setColor(r, g, b, a);
            buf.addVertex(pose, minX, maxY, minZ).setColor(r, g, b, a);

            buf.addVertex(pose, maxX, minY, minZ).setColor(r, g, b, a);
            buf.addVertex(pose, maxX, maxY, minZ).setColor(r, g, b, a);
            buf.addVertex(pose, maxX, maxY, maxZ).setColor(r, g, b, a);
            buf.addVertex(pose, maxX, minY, maxZ).setColor(r, g, b, a);

            BufferUploader.drawWithShader(buf.buildOrThrow());
            RenderSystem.depthMask(true);
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
        }

        if (lineWidth <= 1.0f) {
            ShapeRenderer.renderLineBox(poseStack, consumer, aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, r, g, b, a);
        } else {
            int steps = Math.max(1, (int)lineWidth);
            float stepSize = 0.003f;
            float startOffset = -(steps - 1) * stepSize / 2.0f;
            for (int i = 0; i < steps; i++) {
                float o = startOffset + i * stepSize;
                ShapeRenderer.renderLineBox(poseStack, consumer, 
                    aabb.minX - o, aabb.minY - o, aabb.minZ - o, 
                    aabb.maxX + o, aabb.maxY + o, aabb.maxZ + o, 
                    r, g, b, a);
            }
        }
    }

    private static boolean shouldRenderForEntity(Entity entity) {
        if (entity instanceof net.minecraft.world.entity.player.Player) {
            return BratokHitboxConfig.INSTANCE.showPlayers;
        } else if (entity instanceof LivingEntity) {
            return BratokHitboxConfig.INSTANCE.showMobs;
        } else {
            return BratokHitboxConfig.INSTANCE.showOthers;
        }
    }

    @Redirect(method = "renderHitbox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ShapeRenderer;renderLineBox(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/AABB;FFFF)V"))
    private static void onRenderMainHitbox(PoseStack poseStack, VertexConsumer vertexConsumer, AABB aabb, float rO, float gO, float bO, float aO, PoseStack poseStack2, VertexConsumer vertexConsumer2, Entity entity, float partialTick, float red, float green, float blue) {
        if (!shouldRenderForEntity(entity)) return;

        HitboxMode mode = BratokHitboxConfig.INSTANCE.currentMode;

        float r = 1.0f, g = 1.0f, b = 1.0f, a = 1.0f;
        float lineWidth = (float) BratokHitboxConfig.INSTANCE.globalLineWidth;
        boolean fill = false;
        boolean shouldRender = true;

        switch (mode) {
            case DEFAULT:
                r = ((BratokHitboxConfig.INSTANCE.globalColor >> 16) & 0xFF) / 255f;
                g = ((BratokHitboxConfig.INSTANCE.globalColor >> 8) & 0xFF) / 255f;
                b = (BratokHitboxConfig.INSTANCE.globalColor & 0xFF) / 255f;
                a = BratokHitboxConfig.INSTANCE.globalAlpha / 255f;
                break;
            case FULL:
                fill = true;
                r = ((BratokHitboxConfig.INSTANCE.fullColor >> 16) & 0xFF) / 255f;
                g = ((BratokHitboxConfig.INSTANCE.fullColor >> 8) & 0xFF) / 255f;
                b = (BratokHitboxConfig.INSTANCE.fullColor & 0xFF) / 255f;
                a = BratokHitboxConfig.INSTANCE.fullAlpha / 255f;
                break;
            case DAMAGE:
                if (entity instanceof LivingEntity living && living.hurtTime > 0) {
                    r = ((BratokHitboxConfig.INSTANCE.damageColor >> 16) & 0xFF) / 255f;
                    g = ((BratokHitboxConfig.INSTANCE.damageColor >> 8) & 0xFF) / 255f;
                    b = (BratokHitboxConfig.INSTANCE.damageColor & 0xFF) / 255f;
                    a = BratokHitboxConfig.INSTANCE.damageAlpha / 255f;
                } else {
                    if (BratokHitboxConfig.INSTANCE.damageShowDefault) {
                        r = ((BratokHitboxConfig.INSTANCE.globalColor >> 16) & 0xFF) / 255f;
                        g = ((BratokHitboxConfig.INSTANCE.globalColor >> 8) & 0xFF) / 255f;
                        b = (BratokHitboxConfig.INSTANCE.globalColor & 0xFF) / 255f;
                        a = BratokHitboxConfig.INSTANCE.globalAlpha / 255f;
                    } else {
                        shouldRender = false;
                    }
                }
                break;
            case SIGHT:
                Entity cameraEntity = Minecraft.getInstance().cameraEntity;
                if (cameraEntity != null) {
                    Vec3 look = cameraEntity.getViewVector(partialTick);
                    Vec3 eyePos = cameraEntity.getEyePosition(partialTick);
                    AABB targetBox = entity.getBoundingBox().inflate(entity.getPickRadius()); 

                    double reach = 3.0;
                    if (cameraEntity instanceof net.minecraft.world.entity.player.Player player) {
                        reach = player.entityInteractionRange();
                    }

                    if (entity != cameraEntity && targetBox.clip(eyePos, eyePos.add(look.scale(reach))).isPresent()) {
                        r = ((BratokHitboxConfig.INSTANCE.sightColor >> 16) & 0xFF) / 255f;
                        g = ((BratokHitboxConfig.INSTANCE.sightColor >> 8) & 0xFF) / 255f;
                        b = (BratokHitboxConfig.INSTANCE.sightColor & 0xFF) / 255f;
                        a = BratokHitboxConfig.INSTANCE.sightAlpha / 255f;
                    } else {
                        if (BratokHitboxConfig.INSTANCE.sightShowDefault) {
                            r = ((BratokHitboxConfig.INSTANCE.globalColor >> 16) & 0xFF) / 255f;
                            g = ((BratokHitboxConfig.INSTANCE.globalColor >> 8) & 0xFF) / 255f;
                            b = (BratokHitboxConfig.INSTANCE.globalColor & 0xFF) / 255f;
                            a = BratokHitboxConfig.INSTANCE.globalAlpha / 255f;
                        } else {
                            shouldRender = false;
                        }
                    }
                } else {
                    if (BratokHitboxConfig.INSTANCE.sightShowDefault) {
                        r = ((BratokHitboxConfig.INSTANCE.globalColor >> 16) & 0xFF) / 255f;
                        g = ((BratokHitboxConfig.INSTANCE.globalColor >> 8) & 0xFF) / 255f;
                        b = (BratokHitboxConfig.INSTANCE.globalColor & 0xFF) / 255f;
                        a = BratokHitboxConfig.INSTANCE.globalAlpha / 255f;
                    } else {
                        shouldRender = false;
                    }
                }
                break;
        }

        if (BratokHitboxConfig.INSTANCE.enableDistanceFading) {
            Entity cameraEntity = Minecraft.getInstance().cameraEntity;
            if (cameraEntity != null) {
                double dist = cameraEntity.distanceTo(entity);
                double maxDist = BratokHitboxConfig.INSTANCE.fadeMaxDistance;
                if (dist >= maxDist) {
                    shouldRender = false;
                } else {
                    a *= (float) (1.0 - (dist / maxDist));
                }
            }
        }

        if (shouldRender) {
            drawThickBox(poseStack, vertexConsumer, aabb, r, g, b, a, lineWidth, fill);
        }
    }

    @Redirect(method = "renderHitbox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ShapeRenderer;renderLineBox(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDDDDFFFF)V"))
    private static void onRenderEyeLine(PoseStack poseStack, VertexConsumer vertexConsumer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float r, float g, float b, float a, PoseStack poseStack2, VertexConsumer vertexConsumer2, Entity entity, float partialTick, float red, float green, float blue) {
        if (!shouldRenderForEntity(entity)) return;

        if (BratokHitboxConfig.INSTANCE.enableDistanceFading) {
            Entity cameraEntity = Minecraft.getInstance().cameraEntity;
            if (cameraEntity != null) {
                double dist = cameraEntity.distanceTo(entity);
                double maxDist = BratokHitboxConfig.INSTANCE.fadeMaxDistance;
                if (dist >= maxDist) return;
                a *= (float) (1.0 - (dist / maxDist));
            }
        }

        if (BratokHitboxConfig.INSTANCE.showEyeLine) {
            drawThickBox(poseStack, vertexConsumer, new AABB(minX, minY, minZ, maxX, maxY, maxZ), r, g, b, a, (float) BratokHitboxConfig.INSTANCE.globalLineWidth, false);
        }
    }

    @Inject(method = "renderHitbox", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getViewVector(F)Lnet/minecraft/world/phys/Vec3;"), cancellable = true)
    private static void onRenderViewVector(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, float partialTick, float r, float g, float b, CallbackInfo ci) {
        if (!shouldRenderForEntity(entity) || !BratokHitboxConfig.INSTANCE.showLookLine) {
            ci.cancel();
        } else {
            RenderSystem.lineWidth((float) BratokHitboxConfig.INSTANCE.globalLineWidth);
        }
    }
}

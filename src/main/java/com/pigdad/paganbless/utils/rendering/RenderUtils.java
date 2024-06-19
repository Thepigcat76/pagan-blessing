package com.pigdad.paganbless.utils.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.Arrays;
import java.util.function.Predicate;

public final class RenderUtils {
    private static final int[] combinedARGB = new int[Direction.values().length];

    // From Mystical agriculture. Thank you, Blake <3
    public static void renderFloatingItem(ItemStack stack, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        Minecraft minecraft = Minecraft.getInstance();

        if (!stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 1.2D, 0.5D);
            float scale = stack.getItem() instanceof BlockItem ? 0.95F : 0.75F;
            poseStack.scale(scale, scale, scale);
            double tick = System.currentTimeMillis() / 800.0D;
            poseStack.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);
            poseStack.mulPose(Axis.YP.rotationDegrees((float) ((tick * 40.0D) % 360)));
            minecraft.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, combinedLight, combinedOverlay, poseStack, multiBufferSource, minecraft.level, 0);
            poseStack.popPose();
        }
    }

    public static void renderBlockModel(BlockState blockState, PoseStack poseStack, MultiBufferSource pBufferSource, int combinedLight, int combinedOverlay) {
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BakedModel bakedmodel = blockRenderer.getBlockModel(blockState);
        int i = blockRenderer.blockColors.getColor(blockState, null, null, 0);
        float f = (float) (i >> 16 & 255) / 255.0F;
        float f1 = (float) (i >> 8 & 255) / 255.0F;
        float f2 = (float) (i & 255) / 255.0F;

        for (RenderType rt : bakedmodel.getRenderTypes(blockState, RandomSource.create(42L), ModelData.EMPTY)) {
            blockRenderer.modelRenderer.renderModel(poseStack.last(), pBufferSource.getBuffer(RenderTypeHelper.getEntityRenderType(rt, false)), blockState, bakedmodel, f, f1, f2, combinedLight, combinedOverlay, ModelData.EMPTY, rt);
        }
    }

    public static void rotateCentered(PoseStack poseStack, Axis axis, float radians) {
        poseStack.translate(.5f, .5f, .5f);
        poseStack.mulPose(axis.rotationDegrees(radians));
        poseStack.translate(-.5f, -.5f, -.5f);
    }

    public static void rotateCentered(PoseStack poseStack, Direction direction, float degrees) {
        var step = direction.step();
        poseStack.translate(.5f, .5f, .5f);
        poseStack.mulPose(new Quaternionf().setAngleAxis(Math.toRadians(degrees), step.x(), step.y(), step.z()));
        poseStack.translate(-.5f, -.5f, -.5f);
    }

    public static TextureAtlasSprite getFluidTexture(@NotNull FluidStack fluidStack, @NotNull RenderUtils.FluidTextureType type) {
        IClientFluidTypeExtensions properties = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation spriteLocation;
        if (type == FluidTextureType.STILL) {
            spriteLocation = properties.getStillTexture(fluidStack);
        } else {
            spriteLocation = properties.getFlowingTexture(fluidStack);
        }
        return getSprite(spriteLocation);
    }

    public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(spriteLocation);
    }

    public static void renderObject(@Nullable Model3D object, @NotNull PoseStack matrix, VertexConsumer buffer, int argb, int light, int overlay,
                                    RenderResizableCuboid.FaceDisplay faceDisplay, Camera camera, BlockPos renderPos) {
        if (object != null) {
            RenderResizableCuboid.renderCube(object, matrix, buffer, argb, light, overlay, faceDisplay, camera, Vec3.atLowerCornerOf(renderPos));
        }
    }

    public enum FluidTextureType {
        STILL,
        FLOWING
    }

    public static class Model3D {

        public float minX, minY, minZ;
        public float maxX, maxY, maxZ;

        private final TextureAtlasSprite[] textures = new TextureAtlasSprite[6];
        private final boolean[] renderSides = {true, true, true, true, true, true};

        public Model3D setSideRender(Predicate<Direction> shouldRender) {
            for (Direction direction : Direction.values()) {
                setSideRender(direction, shouldRender.test(direction));
            }
            return this;
        }

        public Model3D setSideRender(Direction side, boolean value) {
            renderSides[side.ordinal()] = value;
            return this;
        }

        public Model3D copy() {
            Model3D copy = new Model3D();
            System.arraycopy(textures, 0, copy.textures, 0, textures.length);
            System.arraycopy(renderSides, 0, copy.renderSides, 0, renderSides.length);
            return copy.bounds(minX, minY, minZ, maxX, maxY, maxZ);
        }

        @Nullable
        public TextureAtlasSprite getSpriteToRender(Direction side) {
            int ordinal = side.ordinal();
            return renderSides[ordinal] ? textures[ordinal] : null;
        }

        public Model3D shrink(float amount) {
            return grow(-amount);
        }

        public Model3D grow(float amount) {
            return bounds(minX - amount, minY - amount, minZ - amount, maxX + amount, maxY + amount, maxZ + amount);
        }

        public Model3D xBounds(float min, float max) {
            this.minX = min;
            this.maxX = max;
            return this;
        }

        public Model3D yBounds(float min, float max) {
            this.minY = min;
            this.maxY = max;
            return this;
        }

        public Model3D zBounds(float min, float max) {
            this.minZ = min;
            this.maxZ = max;
            return this;
        }

        public Model3D bounds(float min, float max) {
            return bounds(min, min, min, max, max, max);
        }

        public Model3D bounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
            return xBounds(minX, maxX)
                    .yBounds(minY, maxY)
                    .zBounds(minZ, maxZ);
        }

        public Model3D prepSingleFaceModelSize(Direction face) {
            bounds(0, 1);
            return switch (face) {
                case DOWN -> yBounds(-0.01F, -0.001F);
                case UP -> yBounds(1.001F, 1.01F);
                case NORTH -> zBounds(-0.01F, -0.001F);
                case SOUTH -> zBounds(1.001F, 1.01F);
                case WEST -> xBounds(-0.01F, -0.001F);
                case EAST -> xBounds(1.001F, 1.01F);
            };
        }

        public Model3D prepFlowing(@NotNull FluidStack fluid) {
            TextureAtlasSprite still = getFluidTexture(fluid, FluidTextureType.STILL);
            TextureAtlasSprite flowing = getFluidTexture(fluid, FluidTextureType.FLOWING);
            return setTextures(still, still, flowing, flowing, flowing, flowing);
        }

        public Model3D setTexture(Direction side, @Nullable TextureAtlasSprite sprite) {
            textures[side.ordinal()] = sprite;
            return this;
        }

        public Model3D setTexture(TextureAtlasSprite tex) {
            Arrays.fill(textures, tex);
            return this;
        }

        public Model3D setTextures(TextureAtlasSprite down, TextureAtlasSprite up, TextureAtlasSprite north, TextureAtlasSprite south, TextureAtlasSprite west,
                                   TextureAtlasSprite east) {
            textures[0] = down;
            textures[1] = up;
            textures[2] = north;
            textures[3] = south;
            textures[4] = west;
            textures[5] = east;
            return this;
        }

        public interface ModelBoundsSetter {
            Model3D set(float min, float max);
        }
    }
}

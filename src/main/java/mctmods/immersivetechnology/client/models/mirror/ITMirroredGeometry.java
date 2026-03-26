package mctmods.immersivetechnology.client.models.mirror;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Function;

public record ITMirroredGeometry(UnbakedModel inner) implements IUnbakedGeometry<ITMirroredGeometry> {
    @Override
    public BakedModel bake(IGeometryBakingContext owner, ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
        BakedModel baseResult = inner.bake(bakery, spriteGetter, new ITMirroredModelState(modelState));
        return new ITMirroredBakedModel<>(baseResult);
    }
}

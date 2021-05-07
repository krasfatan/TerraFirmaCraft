package net.dries007.tfc.world.feature.vein;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;

import com.mojang.serialization.Codec;
import net.dries007.tfc.common.types.Rock;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ChunkDataProvider;

public class DykeVeinFeature extends PipeVeinFeature
{
    public DykeVeinFeature(Codec<PipeVeinConfig> codec)
    {
        super(codec);
    }

    @Override
    protected MutableBoundingBox getBoundingBox(PipeVeinConfig config, PipeVein vein)
    {
        MutableBoundingBox bb = super.getBoundingBox(config, vein);
        bb.y0 = 50 - vein.getPos().getY(); //todo: can we do better? hard to sample noise...
        return bb;
    }

    @Nullable
    @Override
    protected BlockState getStateToGenerate(BlockState stoneState, Random random, PipeVeinConfig config, ISeedReader world, BlockPos pos)
    {
        if (config.getStateToGenerate(stoneState, random) != null) // we ignore the actual result, using the config like a whitelist
        {
            final ChunkData data = ChunkDataProvider.getOrThrow(world).get(pos, ChunkData.Status.ROCKS);
            final Rock rock = data.getRockData().getBottomRock(pos.getX(), pos.getZ()); //todo: re-evaluate if rock stuff changes?

            return rock.getBlock(Rock.BlockType.RAW).defaultBlockState();
        }
        return null;
    }
}

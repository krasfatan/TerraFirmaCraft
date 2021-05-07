/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.dries007.tfc.world.feature.vein;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;

import com.mojang.serialization.Codec;

public class PipeVeinFeature extends VeinFeature<PipeVeinConfig, PipeVeinFeature.PipeVein>
{
    public PipeVeinFeature(Codec<PipeVeinConfig> codec)
    {
        super(codec);
    }

    @Override
    protected MutableBoundingBox getBoundingBox(PipeVeinConfig config, PipeVein vein)
    {
        int radius = config.getRadius();
        int skew = vein.skew;
        return new MutableBoundingBox(-radius - skew, -config.getSize(), -radius - skew, radius + skew, config.getSize(), radius + skew);
    }

    @Override
    protected float getChanceToGenerate(int x, int y, int z, PipeVein vein, PipeVeinConfig config)
    {
        final double yScaled = (double) y / config.getSize();
        x += vein.skew * MathHelper.cos(vein.angle) * yScaled;
        z += vein.skew * MathHelper.sin(vein.angle) * yScaled;

        final double yFactor = (double) vein.yFlipper * yScaled + 0.5D;
        final double trueRadius = config.getRadius() * (1 - yFactor) + (config.getRadius() - vein.slant) * yFactor;
        if (Math.abs(y) < config.getSize() && (x * x) + (z * z) < trueRadius * trueRadius)
        {
            return config.getDensity();
        }
        return 0;
    }

    @Override
    protected PipeVein createVein(int chunkX, int chunkZ, Random random, PipeVeinConfig config)
    {
        return new PipeVein(defaultPos(chunkX, chunkZ, random, config), random, config);
    }

    static class PipeVein extends Vein
    {
        final int yFlipper;
        final float angle;
        final int skew;
        final int slant;

        PipeVein(BlockPos pos, Random random, PipeVeinConfig config)
        {
            super(pos);
            this.yFlipper = random.nextBoolean() ? 1 : -1;
            this.angle = random.nextFloat() * (float) Math.PI * 2;
            this.skew = MathHelper.nextInt(random, config.getMinSkew(), config.getMaxSkew());
            this.slant = MathHelper.nextInt(random, config.getMinSlant(), config.getMaxSlant());
        }
    }
}

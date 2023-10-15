package dev.clepto.ironfurnaces.blocks;

import dev.clepto.ironfurnaces.IronFurnaceType;

public class BlockIronFurnace extends BlockCustomFurnace {

    public BlockIronFurnace(boolean isActive) {
        super(IronFurnaceType.IRON, isActive);
    }
}

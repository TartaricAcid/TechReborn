package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import techreborn.client.SlotOutput;
import techreborn.tiles.TileAlloySmelter;
import techreborn.tiles.TileBlastFurnace;

public class ContainerAlloySmelter extends TechRebornContainer {

	EntityPlayer player;

	TileAlloySmelter tile;

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	public int tickTime;

	public ContainerAlloySmelter(TileAlloySmelter tileAlloysmelter,
			EntityPlayer player)
	{
		tile = tileAlloysmelter;
		this.player = player;

		// input
		this.addSlotToContainer(new Slot(tileAlloysmelter.inventory, 0, 56, 25));
		this.addSlotToContainer(new Slot(tileAlloysmelter.inventory, 1, 56, 43));
		// outputs
		this.addSlotToContainer(new SlotOutput(tileAlloysmelter.inventory, 2,
				116, 35));

		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9
						+ 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18,
					142));
		}
	}

}
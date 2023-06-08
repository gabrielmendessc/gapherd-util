package com.gapherd.gapherdutil.menu.pump;

import com.gapherd.gapherdutil.menu.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Objects;

public class PumpMenu extends AbstractContainerMenu {

    private final Container container;

    private static class BucketSlot extends Slot {

        public BucketSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack itemStack) {

            return Items.BUCKET.equals(itemStack.getItem());
            
        }

    }

    public PumpMenu(int id, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {

        this(id, inventory, new SimpleContainer(9));

    }

    public PumpMenu(int id, Inventory inventory, Container container) {

        super(ModMenuTypes.PUMP_MENU.get(), id);

        checkContainerSize(container, 9);

        this.container = container;
        container.startOpen(inventory.player);

        addInjectorSlots(container);
        addInventorySlots(inventory);
        addHotbarSlots(inventory);

    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {

        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot.hasItem()) {

            ItemStack movingStack = slot.getItem();
            originalStack = movingStack.copy();

            if (slotIndex < 9) {

                if (!this.moveItemStackTo(movingStack, 9, 45, true)) {

                    return ItemStack.EMPTY;

                }

            } else if (!this.moveItemStackTo(movingStack, 0, 9, false)) {

                return ItemStack.EMPTY;

            }

            if (movingStack.isEmpty()) {

                slot.setByPlayer(ItemStack.EMPTY);

            } else {

                slot.setChanged();

            }

            if (Objects.equals(movingStack.getCount(), originalStack.getCount())) {

                return ItemStack.EMPTY;

            }

            slot.onTake(player, movingStack);

        }

        return originalStack;

    }

    @Override
    public boolean stillValid(Player player) {

        return this.container.stillValid(player);

    }

    private void addInjectorSlots(Container container) {

        for(int i = 0; i < 3; ++i) {

            for(int j = 0; j < 3; ++j) {

                this.addSlot(new BucketSlot(container, j + i * 3, 62 + j * 18, 17 + i * 18));

            }

        }

    }

    private void addInventorySlots(Inventory inventory) {

        for(int k = 0; k < 3; ++k) {

            for(int i1 = 0; i1 < 9; ++i1) {

                this.addSlot(new Slot(inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));

            }

        }

    }

    private void addHotbarSlots(Inventory inventory) {

        for(int l = 0; l < 9; ++l) {

            this.addSlot(new Slot(inventory, l, 8 + l * 18, 142));

        }

    }

}

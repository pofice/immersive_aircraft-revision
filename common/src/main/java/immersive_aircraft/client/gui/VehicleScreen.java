package immersive_aircraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import immersive_aircraft.Main;
import immersive_aircraft.entity.misc.VehicleInventoryDescription;
import immersive_aircraft.screen.VehicleScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

public class VehicleScreen extends HandledScreen<VehicleScreenHandler> {
    private static final Identifier TEXTURE = Main.locate("textures/gui/container/inventory.png");

    public static int titleHeight = 10;
    public static int baseHeight = 86;
    public static int containerSize;

    public VehicleScreen(VehicleScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        containerSize = handler.getVehicle().getInventoryDescription().getHeight();

        backgroundHeight = baseHeight + containerSize + titleHeight * 2;
        playerInventoryTitleY = containerSize + titleHeight;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        //nop
    }

    protected void drawCustomBackground(MatrixStack matrices) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        drawTexture(matrices, x, y, 0, 0, backgroundWidth, containerSize + titleHeight * 2, 512, 256);
        drawTexture(matrices, x, y + containerSize + titleHeight * 2 - 4, 0, 222 - baseHeight, backgroundWidth, baseHeight, 512, 256);
    }

    private void drawImage(MatrixStack matrices, int x, int y, int u, int v, int w, int h) {
        drawTexture(matrices, x, y, u, v, w, h, 512, 256);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawCustomBackground(matrices);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int titleHeight = 10;

        for (VehicleInventoryDescription.Slot slot : handler.getVehicle().getInventoryDescription().getSlots()) {
            switch (slot.type) {
                case INVENTORY -> drawImage(matrices, x + slot.x - 1, y + titleHeight + slot.y - 1, 284, 0, 22, 22);
                case STORAGE -> drawImage(matrices, x + slot.x - 1 + 1, y + titleHeight + slot.y - 1, 284, 0, 22, 22);
                case BOILER -> drawImage(matrices, x + slot.x - 4, y + titleHeight + slot.y - 18, 318, 0, 24, 39);
                case WEAPON -> drawImage(matrices, x + slot.x - 3, y + titleHeight + slot.y - 3, 262, 0, 22, 22);
                case UPGRADE -> drawImage(matrices, x + slot.x - 3, y + titleHeight + slot.y - 3, 262, 22, 22, 22);
                case BANNER -> drawImage(matrices, x + slot.x - 3, y + titleHeight + slot.y - 3, 262, 44, 22, 22);
            }
        }

        super.render(matrices, mouseX, mouseY, delta);

        if (focusedSlot != null && !focusedSlot.hasStack()) {
            this.renderTooltip(matrices, List.of(new LiteralText("Engine")), Optional.empty(), mouseX, mouseY);
        } else {
            drawMouseoverTooltip(matrices, mouseX, mouseY);
        }
    }

    @Override
    protected void init() {
        super.init();

        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}

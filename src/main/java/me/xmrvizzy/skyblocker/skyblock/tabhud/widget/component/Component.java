package me.xmrvizzy.skyblocker.skyblock.tabhud.widget.component;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Abstract base class for a component that may be added to a Widget.
 */
public abstract class Component {

    static final int ICO_DIM = 16;
    public static final int PAD_S = 2;
    public static final int PAD_L = 4;

    static TextRenderer txtRend = MinecraftClient.getInstance().textRenderer;
    static ItemRenderer itmRend = MinecraftClient.getInstance().getItemRenderer();

    // these should always be the content dimensions without any padding.
    int width, height;

    public abstract void render(MatrixStack ms, int x, int y);

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

}

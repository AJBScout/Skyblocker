package me.xmrvizzy.skyblocker.skyblock.tabhud.widget.component;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Meta-Component that consists of a grid of other components
 * Grid cols are separated by lines.
 */
public class TableComponent extends Component {

    private Component[][] comps;
    private int color;
    private int cols, rows;
    private int cellW, cellH;

    public TableComponent(int w, int h, int col) {
        comps = new Component[w][h];
        color = 0xff000000 | col;
        cols = w;
        rows = h;
    }

    public void addToCell(int x, int y, Component c) {
        this.comps[x][y] = c;

        // pad extra to add a vertical line later
        this.cellW = Math.max(this.cellW, c.width + PAD_S + PAD_L);

        // assume all rows are equally high so overwriting doesn't matter
        // if this wasn't the case, drawing would need more math
        // not doing any of that if it's not needed
        this.cellH = c.height + PAD_S;

        this.width = this.cellW * this.cols;
        this.height = (this.cellH * this.rows) - PAD_S / 2;

    }

    @Override
    public void render(MatrixStack ms, int xpos, int ypos) {
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                if (comps[x][y] != null) {
                    comps[x][y].render(ms, xpos + (x * cellW), ypos + y * cellH);
                }
            }
            // add a line before the col if we're not drawing the first one
            if (x != 0) {
                int lineX1 = xpos + (x * cellW) - PAD_S - 1;
                int lineX2 = xpos + (x * cellW) - PAD_S;
                int lineY1 = ypos + 1;
                int lineY2 = ypos + this.height - PAD_S - 1; // not sure why but it looks correct
                DrawableHelper.fill(ms, lineX1, lineY1, lineX2, lineY2, this.color);
            }
        }
    }

}

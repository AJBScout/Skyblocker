package me.xmrvizzy.skyblocker.skyblock.tabhud.widget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.xmrvizzy.skyblocker.skyblock.tabhud.util.Ico;
import me.xmrvizzy.skyblocker.skyblock.tabhud.util.PlayerListMgr;
import me.xmrvizzy.skyblocker.skyblock.tabhud.widget.component.IcoTextComponent;
import me.xmrvizzy.skyblocker.skyblock.tabhud.widget.component.ProgressComponent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

// this widget shows your faction status (crimson isle)

public class ReputationWidget extends Widget {

    private static final MutableText TITLE = Text.literal("Faction Status").formatted(Formatting.AQUA,
            Formatting.BOLD);

    // matches your faction alignment progress
    // group 1: percentage to next alignment level
    private static final Pattern PROGRESS_PATTERN = Pattern.compile("\\|+ \\((?<prog>[0-9.]*)%\\)");

    // matches alignment level names
    // group 1: left level name
    // group 2: right level name
    private static final Pattern STATE_PATTERN = Pattern.compile("(?<from>\\S*) *(?<to>\\S*)");

    public ReputationWidget() {
        super(TITLE, Formatting.AQUA.getColorValue());

        String fracstr = PlayerListMgr.strAt(45);

        int spaceidx;
        IcoTextComponent faction;
        if (fracstr == null || (spaceidx = fracstr.indexOf(' ')) == -1) {
            faction = new IcoTextComponent();
        } else {
            String fname = fracstr.substring(0, spaceidx);
            if (fname.equals("Mage")) {
                faction = new IcoTextComponent(Ico.POTION, Text.literal(fname).formatted(Formatting.DARK_AQUA));
            } else {
                faction = new IcoTextComponent(Ico.SWORD, Text.literal(fname).formatted(Formatting.RED));
            }
        }
        this.addComponent(faction);

        Text rep = Widget.plainEntryText(46);
        Matcher prog = PlayerListMgr.regexAt(47, PROGRESS_PATTERN);
        Matcher state = PlayerListMgr.regexAt(48, STATE_PATTERN);

        if (prog == null || state == null) {
            this.addComponent(new ProgressComponent());
        } else {
            float pcnt = Float.parseFloat(prog.group("prog"));
            ProgressComponent pc = new ProgressComponent(Ico.LANTERN,
                    Text.of(state.group("from") + " -> " + state.group("to")), rep, pcnt,
                    Formatting.AQUA.getColorValue());
            this.addComponent(pc);
        }

        this.pack();

    }

}

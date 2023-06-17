package me.xmrvizzy.skyblocker.skyblock.dungeon;

import me.xmrvizzy.skyblocker.config.SkyblockerConfig;
import me.xmrvizzy.skyblocker.utils.RenderUtils;
import me.xmrvizzy.skyblocker.utils.Utils;
import me.xmrvizzy.skyblocker.utils.color.QuadColor;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TicTacToe {

    static String[] grid =new String[9];
    private static final Logger LOGGER = LoggerFactory.getLogger(TicTacToe.class.getName());
    static boolean isz=false;
    static Vec3d center;
    static boolean renderHooked = false;
    static double[] next=new double[3];
    static final double[][]  offsets=new double[][]{{-1.5,.5,.5},{-.5,.5,.5},{.5,.5,.5},{-1.5,-.5,.5},{-.5,-.5,.5},{.5,-.5,.5},{-1.5,-1.5,.5},{-.5,-1.5,.5},{.5,-1.5,.5}};
    static int positive=1;

    public static void printInfo(String txt){
        MinecraftClient client = MinecraftClient.getInstance();
        client.player.sendMessage(Text.of(txt), false);
    }
    public static void update() {
        ClientWorld world = MinecraftClient.getInstance().world;
        if(!Utils.isInDungeons()&&center!=null){
            center=null;
            next=null;
        }
        if (world == null||!Utils.isInDungeons()) return;
        if(!renderHooked){

            WorldRenderEvents.END.register(TicTacToe::boxRenderer);
            renderHooked = true;
        }
        grid =new String[9];
        Iterable<Entity> entities = world.getEntities();
        for (Entity entity : entities) {
            if (entity instanceof ItemFrameEntity) {
                if (!((ItemFrameEntity) entity).getMapId().isEmpty()) {
                    try {
                        if(1==((ItemFrameEntity) entity).getMapId().getAsInt()){
                            center=null;
                            next=null;
                        }
                        if (31339 == ((ItemFrameEntity) entity).getMapId().getAsInt()) {
                            if (center == null) {
                                findPos((ItemFrameEntity) entity);
                                printInfo(center.toString());
                            }
                            grid[(int) (center.y - entity.getPos().y + 1) * 3 + (isz ? (int) (center.x - entity.getPos().x) : (int) (positive*(center.z - entity.getPos().z))) + 1] = "x";
                        }

                        if (31340 == ((ItemFrameEntity) entity).getMapId().getAsInt() && center != null)
                            grid[(int) (center.y - entity.getPos().y + 1) * 3 + (isz ? (int) (center.x - entity.getPos().x) : (int) (positive*(center.z - entity.getPos().z))) + 1] = "o";
                    }
                    catch (Exception e) {
                    printInfo(e.toString());
                    }
                }
            }
        }
        if(center!=null) {
            solve();
        }
    }
    public static void solve(){
        if(grid[4]==null){
            next=offsets[4];
            return;
        }
        for(int n=0;n<9;n++){
            if(grid[n]==null){
                grid[n]="x";
                if(win(grid)){
                    next=offsets[n];
                    grid[n]=null;
                    printInfo(Integer.toString(n));
                    return;
                }
                grid[n]=null;
            }
        }
        if(grid[0]==grid[8]&&grid[0]!=null){
            next=offsets[2];
            return;
        }
        if(grid[3]==grid[7]&&grid[7]!=null){
            next=offsets[2];
            return;
        }
        for(int n=0;n<9;n++){
            if(grid[n]==null){
                next=offsets[n];
                return;
            }
        }
    }
    public static boolean win(String[] board){
        if(board[0]==(board[1])&&board[1]==board[2]&&board[0]!=null)return true;
        if(board[3]==(board[4])&&board[4]==board[5]&&board[3]!=null)return true;
        if(board[6]==(board[7])&&board[7]==board[8]&&board[6]!=null)return true;
        if(board[0]==(board[3])&&board[3]==board[6]&&board[0]!=null)return true;
        if(board[1]==(board[4])&&board[4]==board[7]&&board[1]!=null)return true;
        if(board[2]==(board[5])&&board[5]==board[8]&&board[2]!=null)return true;
        if(board[0]==(board[4])&&board[4]==board[8]&&board[0]!=null)return true;
        if(board[2]==(board[4])&&board[4]==board[6]&&board[2]!=null)return true;
        return false;
    }
    public static void findPos(ItemFrameEntity frame){//use vec3d
        ClientWorld world = MinecraftClient.getInstance().world;
        center=frame.getPos();
        if(world.getBlockState(new BlockPos((int)frame.getPos().x, (int)frame.getPos().y-2, (int)frame.getPos().z)).getBlock().getTranslationKey().contains("butt")){
            center=center.add(0,-1,0);
        }
        if(world.getBlockState(new BlockPos((int)frame.getPos().x-1, (int)frame.getPos().y-1, (int)frame.getPos().z)).getBlock().getTranslationKey().contains("butt")){
            center=center.add(-1,0,0);
        }
        if(world.getBlockState(new BlockPos((int)frame.getPos().x, (int)frame.getPos().y, (int)frame.getPos().z)).getBlock().getTranslationKey().contains("butt")){
            center=center.add(0,1,0);
        }
        if(world.getBlockState(new BlockPos((int)frame.getPos().x+1, (int)frame.getPos().y-1, (int)frame.getPos().z)).getBlock().getTranslationKey().contains("butt")){
            center=center.add(1,0,0);
        }
        if(world.getBlockState(new BlockPos((int)frame.getPos().x, (int)frame.getPos().y-1, (int)frame.getPos().z-1)).getBlock().getTranslationKey().contains("butt")){
            center=center.add(0,0,-1);
        }
        if(world.getBlockState(new BlockPos((int)frame.getPos().x, (int)frame.getPos().y-1, (int)frame.getPos().z+1)).getBlock().getTranslationKey().contains("butt")){
            center=center.add(0,0,1);
        }

        isz=String.valueOf(frame.getPos().x).contains(".5");
        positive=(String.valueOf(frame.getPos().x)+String.valueOf(frame.getPos().z)).contains(".03")?1:-1;
    }
    public static void boxRenderer(WorldRenderContext wrc) {
        QuadColor outlineColorGreen = QuadColor.single( 0.0F, 1.0F, 0.0F, 1f);
        QuadColor outlineColorRed = QuadColor.single(1.0F, 0.0F, 0.0F, 1f);
        try {
            Box button=Box.from(center);
            if(isz&&SkyblockerConfig.get().locations.dungeons.ticTacToe){
                button = button.offset(next[0], next[1], next[2]);
                RenderUtils.drawBoxOutline(button, outlineColorGreen, 5f);
            }
             button=Box.from(center);
            if(!isz&& SkyblockerConfig.get().locations.dungeons.ticTacToe){
                button = button.offset(next[2], next[1], next[0]);
                RenderUtils.drawBoxOutline(button, outlineColorRed, 5f);
            }

        }catch(Exception e) {
            LOGGER.warn("[Skyblocker TicTacToe] " + e);
        }
    }
}

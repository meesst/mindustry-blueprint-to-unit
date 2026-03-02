package blueprintunit.interaction;

import arc.struct.*;
import blueprintunit.grid.*;
import blueprintunit.unit.*;
import mindustry.game.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.blocks.power.*;

public class UnitGridInteraction {
    /**
     * 单位向虚拟网格内方块放入物品
     */
    public static boolean putItem(BlueprintUnit unit, int tileX, int tileY, Item item, int amount){
        if(unit.grid == null) return false;
        
        VirtualTile tile = unit.grid.tile(tileX, tileY);
        if(tile == null || !tile.block.hasItems) return false;
        
        if(tile.build != null && tile.build.items != null){
            int added = tile.build.items.add(item, amount);
            return added > 0;
        }
        return false;
    }
    
    /**
     * 单位从虚拟网格内方块取出物品
     */
    public static int getItem(BlueprintUnit unit, int tileX, int tileY, Item item, int amount){
        if(unit.grid == null) return 0;
        
        VirtualTile tile = unit.grid.tile(tileX, tileY);
        if(tile == null || !tile.block.hasItems) return 0;
        
        if(tile.build != null && tile.build.items != null){
            int removed = tile.build.items.remove(item, amount);
            return removed;
        }
        return 0;
    }
    
    /**
     * 为单位创建独立的电力网络
     */
    public static PowerGraph createPowerGraph(BlueprintUnit unit){
        PowerGraph graph = new PowerGraph();
        
        // 遍历虚拟网格内的所有电力方块
        if(unit.grid != null){
            for(VirtualTile tile : unit.grid.tiles){
                if(tile.build != null && tile.build.power != null){
                    graph.add(tile.build);
                }
            }
        }
        
        return graph;
    }
    
    /**
     * 更新单位的电力网络
     */
    public static void updatePowerGraph(BlueprintUnit unit, PowerGraph graph){
        if(graph != null){
            graph.update();
        }
    }
    
    /**
     * 检查单位是否可以与虚拟网格内的方块交互
     */
    public static boolean canInteract(BlueprintUnit unit, int tileX, int tileY){
        if(unit.grid == null) return false;
        
        VirtualTile tile = unit.grid.tile(tileX, tileY);
        return tile != null;
    }
    
    /**
     * 获取虚拟网格内方块的物品库存
     */
    public static ItemSeq getItems(BlueprintUnit unit, int tileX, int tileY){
        ItemSeq items = new ItemSeq();
        
        if(unit.grid == null) return items;
        
        VirtualTile tile = unit.grid.tile(tileX, tileY);
        if(tile != null && tile.build != null && tile.build.items != null){
            for(Item item : content.items()){
                int amount = tile.build.items.get(item);
                if(amount > 0){
                    items.add(item, amount);
                }
            }
        }
        
        return items;
    }
}
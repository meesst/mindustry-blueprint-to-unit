package blueprintunit.sync;

import arc.struct.*;
import blueprintunit.grid.*;
import blueprintunit.parser.SchematicParser;
import blueprintunit.unit.*;
import mindustry.game.Team;
import mindustry.io.*;
import mindustry.world.*;

public class SaveHandler {
    /**
     * 保存蓝图单位的状态
     */
    public static void saveBlueprintUnit(BlueprintUnit unit, Json json) {
        if (unit.grid != null) {
            // 保存虚拟网格的大小
            json.writeInt(unit.grid.width);
            json.writeInt(unit.grid.height);
            
            // 保存每个虚拟Tile的状态
            for (int i = 0; i < unit.grid.tiles.size; i++) {
                VirtualTile tile = unit.grid.tiles.get(i);
                saveVirtualTile(tile, json);
            }
        } else {
            // 如果没有网格，保存0大小
            json.writeInt(0);
            json.writeInt(0);
        }
        
        // 保存蓝图数据
        if (unit.schematicData != null) {
            json.writeString(unit.schematicData.name);
            json.writeFloat(unit.schematicData.health);
            json.writeFloat(unit.schematicData.armor);
            json.writeInt(unit.schematicData.itemCapacity);
            json.writeInt(unit.schematicData.liquidCapacity);
        } else {
            json.writeString("");
            json.writeFloat(0f);
            json.writeFloat(0f);
            json.writeInt(0);
            json.writeInt(0);
        }
    }
    
    /**
     * 加载蓝图单位的状态
     */
    public static void loadBlueprintUnit(BlueprintUnit unit, Json json) {
        int width = json.readInt();
        int height = json.readInt();
        
        if (width > 0 && height > 0) {
            // 创建新的虚拟网格
            unit.grid = new VirtualTileGrid(width, height);
            
            // 加载每个虚拟Tile的状态
            for (int i = 0; i < unit.grid.tiles.size; i++) {
                VirtualTile tile = unit.grid.tiles.get(i);
                loadVirtualTile(tile, json);
            }
        }
        
        // 加载蓝图数据
        String name = json.readString();
        float health = json.readFloat();
        float armor = json.readFloat();
        int itemCapacity = json.readInt();
        int liquidCapacity = json.readInt();
        
        if (!name.isEmpty()) {
            // 创建新的SchematicData对象
            unit.schematicData = new SchematicParser.SchematicData();
            unit.schematicData.name = name;
            unit.schematicData.health = health;
            unit.schematicData.armor = armor;
            unit.schematicData.itemCapacity = itemCapacity;
            unit.schematicData.liquidCapacity = liquidCapacity;
        }
    }
    
    /**
     * 保存虚拟Tile的状态
     */
    private static void saveVirtualTile(VirtualTile tile, Json json) {
        // 保存方块ID
        json.writeString(tile.block.name);
        // 保存队伍
        json.writeInt(tile.team.id);
        // 保存数据
        json.writeByte(tile.data);
        json.writeByte(tile.floorData);
        json.writeByte(tile.overlayData);
        json.writeInt(tile.extraData);
        
        // 保存建筑状态
        if (tile.build != null) {
            json.writeBoolean(true);
            // 这里可以添加建筑状态的保存逻辑
        } else {
            json.writeBoolean(false);
        }
    }
    
    /**
     * 加载虚拟Tile的状态
     */
    private static void loadVirtualTile(VirtualTile tile, Json json) {
        // 加载方块
        String blockName = json.readString();
        Block block = Blocks.get(blockName);
        if (block != null) {
            // 加载队伍
            int teamId = json.readInt();
            Team team = Team.get(teamId);
            
            // 设置方块
            tile.setBlock(block, team, 0);
            
            // 加载数据
            tile.data = json.readByte();
            tile.floorData = json.readByte();
            tile.overlayData = json.readByte();
            tile.extraData = json.readInt();
            
            // 加载建筑状态
            boolean hasBuild = json.readBoolean();
            if (hasBuild && tile.build != null) {
                // 这里可以添加建筑状态的加载逻辑
            }
        }
    }
}

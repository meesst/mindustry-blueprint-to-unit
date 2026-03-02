package blueprintunit.sync;

import arc.net.*;
import arc.util.*;
import blueprintunit.grid.*;
import blueprintunit.unit.*;
import mindustry.content.Blocks;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.net.*;
import mindustry.world.Block;

public class NetworkSync {
    /**
     * 注册网络同步处理器
     */
    public static void registerSyncHandlers() {
        // 注册蓝图单位的网络同步处理器
        Net.registerPacket(BlueprintUnitSyncPacket.class, (packet, player) -> {
            if (player == null) return;
            
            // 找到对应的单位
            BlueprintUnit unit = Groups.unit.getByID(packet.unitId);
            if (unit != null && unit instanceof BlueprintUnit) {
                // 同步单位状态
                syncBlueprintUnit(unit, packet);
            }
        });
    }
    
    /**
     * 同步蓝图单位的状态
     */
    public static void syncBlueprintUnit(BlueprintUnit unit, BlueprintUnitSyncPacket packet) {
        if (unit == null) return;
        
        // 同步基本状态
        unit.x = packet.x;
        unit.y = packet.y;
        unit.rotation = packet.rotation;
        unit.health = packet.health;
        
        // 同步虚拟网格状态
        if (packet.gridData != null && packet.gridData.length > 0) {
            if (unit.grid == null || unit.grid.width != packet.gridWidth || unit.grid.height != packet.gridHeight) {
                // 创建新的虚拟网格
                unit.grid = new VirtualTileGrid(packet.gridWidth, packet.gridHeight);
            }
            
            // 同步每个虚拟Tile的状态
            for (int i = 0; i < Math.min(packet.gridData.length, unit.grid.tiles.size); i++) {
                TileData tileData = packet.gridData[i];
                VirtualTile tile = unit.grid.tiles.get(i);
                if (tile != null) {
                    syncVirtualTile(tile, tileData);
                }
            }
        }
    }
    
    /**
     * 同步虚拟Tile的状态
     */
    private static void syncVirtualTile(VirtualTile tile, TileData tileData) {
        if (tile == null || tileData == null) return;
        
        // 同步方块
        if (tileData.blockName != null) {
            Block block = Blocks.get(tileData.blockName);
            if (block != null) {
                tile.setBlock(block, Team.get(tileData.teamId), 0);
            }
        }
        
        // 同步数据
        tile.data = tileData.data;
        tile.floorData = tileData.floorData;
        tile.overlayData = tileData.overlayData;
        tile.extraData = tileData.extraData;
    }
    
    /**
     * 发送蓝图单位的同步数据包
     */
    public static void sendBlueprintUnitSync(BlueprintUnit unit) {
        if (unit == null || !unit.isAdded()) return;
        
        BlueprintUnitSyncPacket packet = new BlueprintUnitSyncPacket();
        packet.unitId = unit.id;
        packet.x = unit.x;
        packet.y = unit.y;
        packet.rotation = unit.rotation;
        packet.health = unit.health;
        
        // 同步虚拟网格状态
        if (unit.grid != null) {
            packet.gridWidth = unit.grid.width;
            packet.gridHeight = unit.grid.height;
            packet.gridData = new TileData[unit.grid.tiles.size];
            
            for (int i = 0; i < unit.grid.tiles.size; i++) {
                VirtualTile tile = unit.grid.tiles.get(i);
                if (tile != null) {
                    packet.gridData[i] = createTileData(tile);
                }
            }
        }
        
        // 发送数据包
        Net.send(packet);
    }
    
    /**
     * 创建TileData对象
     */
    private static TileData createTileData(VirtualTile tile) {
        TileData data = new TileData();
        data.blockName = tile.block.name;
        data.teamId = tile.team.id;
        data.data = tile.data;
        data.floorData = tile.floorData;
        data.overlayData = tile.overlayData;
        data.extraData = tile.extraData;
        return data;
    }
    
    /**
     * 蓝图单位同步数据包
     */
    public static class BlueprintUnitSyncPacket {
        public int unitId;
        public float x, y, rotation, health;
        public int gridWidth, gridHeight;
        public TileData[] gridData;
    }
    
    /**
     * 虚拟Tile数据
     */
    public static class TileData {
        public String blockName;
        public int teamId;
        public byte data, floorData, overlayData;
        public int extraData;
    }
}

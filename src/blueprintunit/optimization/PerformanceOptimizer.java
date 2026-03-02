package blueprintunit.optimization;

import arc.struct.*;
import blueprintunit.unit.*;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.Turret;

public class PerformanceOptimizer {
    // 最大蓝图单位数量限制
    public static final int MAX_BLUEPRINT_UNITS = 20;
    
    // 单位更新优先级
    private static final Seq<BlueprintUnit> updateQueue = new Seq<>();
    
    /**
     * 检查并限制蓝图单位数量
     */
    public static boolean canAddBlueprintUnit() {
        int count = 0;
        for (Unit unit : Groups.unit) {
            if (unit instanceof BlueprintUnit) {
                count++;
                if (count >= MAX_BLUEPRINT_UNITS) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * 优化单位更新逻辑
     */
    public static void optimizeUpdates() {
        // 清空更新队列
        updateQueue.clear();
        
        // 收集所有蓝图单位
        for (Unit unit : Groups.unit) {
            if (unit instanceof BlueprintUnit) {
                updateQueue.add((BlueprintUnit) unit);
            }
        }
        
        // 按距离玩家的远近排序，优先更新近的单位
        updateQueue.sort((a, b) -> {
            float distA = a.dst2(Vars.player);
            float distB = b.dst2(Vars.player);
            return Float.compare(distA, distB);
        });
        
        // 只更新前10个单位，减少更新负担
        int updateCount = Math.min(10, updateQueue.size);
        for (int i = 0; i < updateCount; i++) {
            BlueprintUnit unit = updateQueue.get(i);
            if (unit != null && unit.isAdded()) {
                // 这里可以添加额外的更新逻辑
            }
        }
    }
    
    /**
     * 优化虚拟网格更新
     */
    public static void optimizeGridUpdates(BlueprintUnit unit) {
        if (unit == null || unit.grid == null) return;
        
        // 只在单位在加载区域内时更新
        if (!unit.isInited()) return;
        
        // 只更新关键方块（如生产建筑、炮塔等）
        for (int i = 0; i < unit.grid.tiles.size; i++) {
            var tile = unit.grid.tiles.get(i);
            if (tile != null && tile.build != null) {
                // 检查方块是否是关键方块
                if (isCriticalBlock(tile.block)) {
                    tile.build.update();
                }
            }
        }
    }
    
    /**
     * 检查方块是否是关键方块
     */
    private static boolean isCriticalBlock(Block block) {
        return block.hasItems || block.hasLiquids || block.hasPower || block instanceof Turret;
    }
    
    /**
     * 优化渲染性能
     */
    public static void optimizeRendering() {
        // 这里可以添加渲染优化逻辑，如视距裁剪、批量渲染等
    }
}

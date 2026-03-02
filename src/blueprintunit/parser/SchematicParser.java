package blueprintunit.parser;

import arc.struct.*;
import mindustry.game.*;
import mindustry.type.*;
import mindustry.world.*;

public class SchematicParser {
    public static SchematicData parse(Schematic schematic){
        SchematicData data = new SchematicData();
        data.width = schematic.width;
        data.height = schematic.height;
        data.name = schematic.name();
        
        // 计算材料消耗
        data.requirements = schematic.requirements();
        
        // 计算单位属性
        float totalHealth = 0;
        float totalArmor = 0;
        int totalItemCapacity = 0;
        int totalLiquidCapacity = 0;
        int drillCount = 0;
        float totalMineSpeed = 0;
        int maxMineTier = 0;
        
        for(Schematic.Stile stile : schematic.tiles){
            Block block = stile.block;
            totalHealth += block.health;
            totalArmor += block.armor;
            
            // 计算存储容量
            if(block.hasItems){
                totalItemCapacity += block.itemCapacity;
            }
            if(block.hasLiquids){
                totalLiquidCapacity += block.liquidCapacity;
            }
            
            // 计算挖矿能力
            if(block instanceof Drill drill){
                drillCount++;
                totalMineSpeed += drill.drillTime;
                maxMineTier = Math.max(maxMineTier, drill.tier);
            }
        }
        
        data.health = totalHealth;
        data.armor = totalArmor;
        data.itemCapacity = totalItemCapacity;
        data.liquidCapacity = totalLiquidCapacity;
        data.drillCount = drillCount;
        data.mineSpeed = totalMineSpeed;
        data.mineTier = maxMineTier;
        
        return data;
    }
    
    public static class SchematicData {
        public int width, height;
        public String name;
        public ItemSeq requirements;
        public float health;
        public float armor;
        public int itemCapacity;
        public int liquidCapacity;
        public int drillCount;
        public float mineSpeed;
        public int mineTier;
        
        public float getMoveSpeed(){
            // 基础速度 2.5，可根据蓝图大小调整
            float baseSpeed = 2.5f;
            int totalBlocks = width * height;
            if(totalBlocks > 100){
                return baseSpeed * 0.8f;
            }else if(totalBlocks > 50){
                return baseSpeed * 0.9f;
            }
            return baseSpeed;
        }
    }
}
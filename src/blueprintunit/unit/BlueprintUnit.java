package blueprintunit.unit;

import arc.math.geom.*;
import blueprintunit.grid.*;
import blueprintunit.parser.SchematicParser;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import blueprintunit.optimization.PerformanceOptimizer;

public class BlueprintUnit extends UnitEntity {
    public static final int classId = EntityMapping.register("blueprint-unit", BlueprintUnit::new);
    public VirtualTileGrid grid;
    public SchematicParser.SchematicData schematicData;
    
    @Override
    public int classId() {
        return classId;
    }
    
    @Override
    public void update(){
        super.update();
        
        if(grid != null){
            // 更新虚拟网格的世界坐标
            grid.setWorldPosition(x, y);
            // 优化虚拟网格的更新
            PerformanceOptimizer.optimizeGridUpdates(this);
        }
    }
    
    @Override
    public void draw(){
        super.draw();
        
        if(grid != null){
            // 绘制虚拟网格内的方块
            grid.draw();
        }
    }
    
    @Override
    public void killed(){
        super.killed();
        
        // 执行蓝图内所有方块的销毁逻辑
        if(grid != null){
            for(VirtualTile tile : grid.tiles){
                if(tile.build != null){
                    tile.build.killed();
                }
            }
        }
    }
    
    public void setSchematicData(SchematicParser.SchematicData data){
        this.schematicData = data;
        
        // 根据蓝图数据设置单位属性
        health = data.health;
        armor = data.armor;
        speed = data.getMoveSpeed();
        
        // 创建虚拟网格
        grid = new VirtualTileGrid(data.width, data.height);
    }
    
    public void loadSchematic(Schematic schematic){
        SchematicData data = SchematicParser.parse(schematic);
        setSchematicData(data);
        
        // 将蓝图中的方块加载到虚拟网格
        for(Schematic.Stile stile : schematic.tiles){
            VirtualTile tile = grid.tile(stile.x, stile.y);
            if(tile != null){
                tile.setBlock(stile.block, team, stile.rotation);
                // 设置方块配置
                if(tile.build != null && stile.config != null){
                    tile.build.configure(stile.config);
                }
            }
        }
    }
}
package blueprintunit.blocks;

import arc.graphics.g2d.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import blueprintunit.unit.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.production.*;
import mindustry.Vars;
import blueprintunit.optimization.PerformanceOptimizer;

public class BlueprintAssembler extends ProductionBlock {
    public BlueprintAssembler(String name) {
        super(name);
        hasItems = true;
        hasPower = true;
        hasLiquids = false;
        size = 3;
        itemCapacity = 1000;
        powerConsumption = 5f;
        craftTime = 60f;
    }
    
    @Override
    public void load() {
        super.load();
        // 这里可以加载自定义的建筑纹理
    }
    
    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        super.drawPlanRegion(plan, list);
    }
    
    @Override
    public Building newBuilding() {
        return new BlueprintAssemblerBuild();
    }
    
    public class BlueprintAssemblerBuild extends ProductionBuild {
        public Schematic selectedSchematic;
        public float progress;
        
        @Override
        public void update() {
            super.update();
            
            if (selectedSchematic != null && items.has(selectedSchematic.requirements())) {
                progress += edelta() / craftTime;
                
                if (progress >= 1f) {
                    // 检查是否可以添加新的蓝图单位
                    if (PerformanceOptimizer.canAddBlueprintUnit()) {
                        // 生产完成，创建蓝图单位
                        BlueprintUnit unit = BlueprintUnit.create();
                        unit.team = team;
                        unit.set(x, y);
                        unit.loadSchematic(selectedSchematic);
                        unit.add();
                        
                        // 消耗材料
                        items.remove(selectedSchematic.requirements());
                    }
                    progress = 0f;
                }
            }
        }
        
        @Override
        public void draw() {
            super.draw();
            
            // 绘制生产进度
            if (selectedSchematic != null) {
                Draw.color(team.color);
                Draw.rect(region, x, y);
                Draw.color();
                
                // 绘制进度条
                float barHeight = 4f;
                float barWidth = size * 8f - 8f;
                Draw.rect("block-Progress-back", x, y - size * 4f - barHeight, barWidth, barHeight);
                Draw.color(team.color);
                Draw.rect("block-Progress", x, y - size * 4f - barHeight, barWidth * progress, barHeight);
                Draw.color();
            }
        }
        
        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
            
            // 创建蓝图选择界面
            BlueprintAssemblerDialog dialog = new BlueprintAssemblerDialog(this);
            dialog.show();
        }
    }
}

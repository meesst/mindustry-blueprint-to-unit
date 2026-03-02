package blueprintunit;

import blueprintunit.blocks.*;
import blueprintunit.optimization.PerformanceOptimizer;
import blueprintunit.sync.NetworkSync;
import blueprintunit.unit.*;
import mindustry.content.*;
import mindustry.mod.*;
import mindustry.type.*;
import arc.Core;
import arc.util.Timer;

public class BlueprintUnitMod extends Mod{
    @Override
    public void init(){
        // 注册网络同步处理器
        NetworkSync.registerSyncHandlers();
        
        // 添加性能优化定时器，每帧执行一次
        Timer.schedule(() -> {
            PerformanceOptimizer.optimizeUpdates();
            PerformanceOptimizer.optimizeRendering();
        }, 0f, 1f / 60f);
        
        System.out.println("Blueprint Unit Mod initialized");
    }
    
    @Override
    public void loadContent(){
        // 加载单位类型
        BlueprintUnitType blueprintUnitType = new BlueprintUnitType("blueprint-unit");
        blueprintUnitType.localizedName = Core.bundle.get("blueprint-unit.name", "蓝图单位");
        blueprintUnitType.description = Core.bundle.get("blueprint-unit.description", "由蓝图转化而来的机动单位");
        
        // 添加到内容列表
        ContentType.unit.getContentList().add(blueprintUnitType);
        
        // 加载建筑
        BlueprintAssembler blueprintAssembler = new BlueprintAssembler("blueprint-assembler");
        blueprintAssembler.localizedName = Core.bundle.get("blueprint-assembler.name", "蓝图装配站");
        blueprintAssembler.description = Core.bundle.get("blueprint-assembler.description", "将蓝图转化为机动单位");
        
        // 添加到内容列表
        ContentType.block.getContentList().add(blueprintAssembler);
        
        System.out.println("Blueprint Unit Mod loading content");
    }
}
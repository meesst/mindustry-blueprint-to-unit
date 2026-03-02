package blueprintunit.unit;

import mindustry.type.*;

public class BlueprintUnitType extends UnitType {
    public BlueprintUnitType(String name){
        super(name);
        
        // 设置单位属性
        flying = true;
        speed = 2.5f;
        hitSize = 8f;
        armor = 0f;
        health = 100f;
        
        // 设置构造函数
        constructor = BlueprintUnit::create;
        
        // 设置AI控制器
        aiController = () -> new FlyingAI();
    }
    
    @Override
    public void update(Unit unit){
        super.update(unit);
        
        if(unit instanceof BlueprintUnit blueprintUnit){
            // 可以在这里添加额外的更新逻辑
        }
    }
}
package blueprintunit.blocks;

import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import arc.Core;

public class BlueprintAssemblerDialog extends BaseDialog {
    private final BlueprintAssembler.BlueprintAssemblerBuild build;
    
    public BlueprintAssemblerDialog(BlueprintAssembler.BlueprintAssemblerBuild build) {
        super(Core.bundle.get("blueprint-assembler.select", "蓝图选择"));
        this.build = build;
        
        setup();
    }
    
    private void setup() {
        addCloseButton();
        
        Table content = new Table();
        content.pane(table -> {
            // 列出所有可用的蓝图
            for (Schematic schematic : Vars.player.schematics()) {
                table.button(b -> {
                    b.left();
                    b.add(schematic.name()).growX();
                    b.add("材料: " + schematic.requirements()).right();
                }, () -> {
                    build.selectedSchematic = schematic;
                    hide();
                }).width(400f).height(50f).pad(5f);
            }
        }).size(400f, 400f);
        
        content.row();
        content.add(Core.bundle.get("blueprint-assembler.select", "选择一个蓝图开始生产")).center().pad(10f);
        
        cont.add(content);
    }
}

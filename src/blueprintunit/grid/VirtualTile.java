package blueprintunit.grid;

import arc.math.geom.*;
import arc.struct.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;

public class VirtualTile implements Position {
    public final VirtualTileGrid grid;
    public final int x, y;
    public Block block;
    public Building build;
    public Team team = Team.derelict;
    public byte data, floorData, overlayData;
    public int extraData;
    
    public VirtualTile(VirtualTileGrid grid, int x, int y){
        this.grid = grid;
        this.x = x;
        this.y = y;
        this.block = Blocks.air;
    }
    
    public float worldx(){
        return grid.getWorldX() + x * 8;
    }
    
    public float worldy(){
        return grid.getWorldY() + y * 8;
    }
    
    public float drawx(){
        return worldx() + block.offset;
    }
    
    public float drawy(){
        return worldy() + block.offset;
    }
    
    public Block block(){
        return block;
    }
    
    public void setBlock(Block type, Team team, int rotation){
        this.block = type;
        this.team = team;
        
        if(type.hasBuilding()){
            this.build = type.newBuilding();
            this.build.init(this, team, true, rotation);
        }else{
            this.build = null;
        }
    }
    
    public void setBlock(Block type){
        setBlock(type, Team.derelict, 0);
    }
    
    public Team team(){
        return team;
    }
    
    public VirtualTile nearby(int dx, int dy){
        return grid.tile(x + dx, y + dy);
    }
    
    public VirtualTile nearby(int rotation){
        switch(rotation){
            case 0: return nearby(1, 0);
            case 1: return nearby(0, 1);
            case 2: return nearby(-1, 0);
            case 3: return nearby(0, -1);
            default: return null;
        }
    }
    
    public boolean solid(){
        return block.solid;
    }
    
    public boolean passable(){
        return !block.solid;
    }
    
    @Override
    public float getX(){
        return drawx();
    }
    
    @Override
    public float getY(){
        return drawy();
    }
}
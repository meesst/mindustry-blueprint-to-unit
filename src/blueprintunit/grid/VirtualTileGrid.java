package blueprintunit.grid;

import arc.math.geom.*;
import arc.struct.*;
import mindustry.world.*;

public class VirtualTileGrid {
    public final int width, height;
    public final Seq<VirtualTile> tiles;
    private float worldX, worldY;
    
    public VirtualTileGrid(int width, int height){
        this.width = width;
        this.height = height;
        this.tiles = new Seq<>(width * height);
        
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                tiles.add(new VirtualTile(this, x, y));
            }
        }
    }
    
    public void setWorldPosition(float x, float y){
        this.worldX = x;
        this.worldY = y;
    }
    
    public float getWorldX(){
        return worldX;
    }
    
    public float getWorldY(){
        return worldY;
    }
    
    public VirtualTile tile(int x, int y){
        if(x < 0 || y < 0 || x >= width || y >= height) return null;
        return tiles.get(y * width + x);
    }
    
    public void update(){
        for(VirtualTile tile : tiles){
            if(tile.build != null){
                tile.build.update();
            }
        }
    }
    
    public void draw(){
        for(VirtualTile tile : tiles){
            if(tile.block != null){
                tile.block.draw(tile);
            }
        }
    }
}
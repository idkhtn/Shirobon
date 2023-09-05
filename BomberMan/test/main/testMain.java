package main;

import blocks.Block;
import blocks.BlockProcessor;
import com.google.gson.Gson;
import game.Game;

public class testMain 
{
    public static void main(String[] args) {
        
        BlockProcessor bp = new BlockProcessor(new Game());
        bp.createMap();
        Block[][] obj = bp.map;
        Block[][] obj2;
        
        String ints = "";
        for (Block[] obj1 : obj) {
            for (Block obj11 : obj1) {
                ints += obj11.getType();
            }
        }
        System.out.println(ints);
        
        Gson gson = new Gson();
        String str = gson.toJson(obj);
        
        Gson gson2 = new Gson();
        obj2 = gson2.fromJson(str, Block[][].class);
        String ints2 = "";
        for (int i = 0; i < obj2.length; i++) {
            for (int j = 0; j < obj2[i].length; j++) {
                ints2+=obj2[i][j].getType();
            }
        }
        System.out.println(ints2);
        if(ints.equals(ints2))
        {
            System.out.println("OK");
        }
        String s = "005:sdf";
        System.out.println();
    }
}
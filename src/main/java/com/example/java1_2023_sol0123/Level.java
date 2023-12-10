package com.example.java1_2023_sol0123;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Level {
    private String levelName = "./src/main/resources/com/example/java1_2023_sol0123/LEVEL1.txt";
    public ArrayList<String> levelList = new ArrayList<String>();
    public Level(){
        loadLevel();
    }

    public void loadLevel(){
        try{
            File file = new File(levelName);
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                levelList.add(line);
            }
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}

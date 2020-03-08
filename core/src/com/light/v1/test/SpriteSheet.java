package com.light.v1.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;

public class SpriteSheet {
    private static String TAG="SpriteSheet";
    private static final String PATH="Universal-LPC-spritesheet";
    private int nbSprites=0;

    private ArrayList<String> common=new ArrayList<String>();
    private ArrayList<String>female=new ArrayList<String>();
    private ArrayList<String> male=new ArrayList<String>();

    public SpriteSheet() {
        init();
    }

    private void scanDirectory(String path, int level) {
        FileHandle hfile=Gdx.files.internal(path);
        FileHandle[] data=hfile.list();

        for (int i=0; i<data.length; i++) {
            FileHandle hfiletmp=data[i];
            if (hfiletmp.isDirectory()) {
                scanDirectory(hfiletmp.path(), level+1);
            }
            else {
                affect(path, hfiletmp.name(), level);
            }
        }
    }

    public void affect(String path, String file, int level) {
        nbSprites++;
        String name=path+"/"+file;

        int indexFemale=name.indexOf("female");
        int indexMale=-1;

        if (indexFemale == -1) {
            // on cherche 'male' si on n'a pas trouve 'female' (meme portion de chaine)
            indexMale=name.indexOf("male");
        }

        if (indexMale == -1 && indexFemale == -1) {
            common.add(name);
            //echo(nb+" - C - "+fileToString(path, file, level));
        }
        else if (indexFemale != -1) {
            female.add(name);
            //echo(nb+" - F - "+fileToString(path, file, level));
        }
        else if (indexMale != -1) {
            male.add(name);
            //echo(nb+" - M - "+fileToString(path, file, level));
        }
        else {
            echo("!!!!! "+name+" not used !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    public void getData(ArrayList<String> data) {
        echo("*************** liste des éléments ************");
        for (String element : data) {
            echo(element);
        }
    }

    private void echo(String message) {
        Gdx.app.debug(TAG, message);
    }

    public String fileToString(String directory, String file, int level) {
        return directoryToString(directory, level)+"/"+file;
    }

    public String directoryToString(String directory, int level) {
        return levelToString(level)+"["+level+"]"+directory;
    }

    public String levelToString(int level) {
        if (level == 0) {
            return "";
        }

        return new String(new char[level*4]).replace('\0', '_');
    }

    public void resume() {
        echo("Nombre de fichiers 'male': "+ male.size());
        echo("Nombre de fichiers 'female': "+ female.size());
        echo("Nombre de fichiers 'common': "+ common.size());
    }

    public void init() {
        echo("Lancement du scan de "+PATH);
        scanDirectory(PATH, 0);
        echo("Scan terminé ________________________________");
        echo("Résultats:");
        resume();
        getData(common);
    }
}

package fr.istic.vv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyFileWriter {

    private String filePath;

    public MyFileWriter(String filePath){
        this.filePath = filePath;
        this.createMyFile();
    }

    private void createMyFile() {
        File file = new File(this.filePath);

        try {
            // Supprimez le fichier s'il existe
            if (file.exists() && file.isFile()) {
                file.delete();
            }

            file.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void appendContent(String contentToAppend) {
        // Utilisez un bloc try-with-resources pour garantir la fermeture automatique des ressources
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath), true))) {
            // Ajoutez le contenu à la fin du fichier
            writer.write(contentToAppend + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


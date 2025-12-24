package com.javaPractice.Files;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Desktop;
import java.nio.file.Path;
import java.util.Scanner;

public class FileEngine {

    public void createFile(String path){
        File file;
        try{
            file = new File(path);
            if(file.createNewFile()){
                System.out.println("File Created: " + file);

            } else{
                System.out.println("File Already Exist");
                deleteFile(file);
                file.createNewFile();
            }

        }catch (IOException e){
            System.out.println("An Error Occurred");
            e.printStackTrace();
        }


    }

    private void deleteFile(File file) {
        if(file.delete()){
            System.out.println("The File Was Delete");
        }
    }


    public void writeFile(String path){
        try{
            FileWriter writer = new FileWriter(path);

            writer.write("# <- The lines that starts with this character are comments, will be ignored\n");
            writer.write("# Write the HTTP request body below.\n");
            writer.write("# Save and close the editor to send the request.\n\n");
            writer.close();
            System.out.println("Successfully Wrote to the File");

        }catch (IOException e){
            System.out.printf("An Error Ocurred");
            e.printStackTrace();
        }
    }

    public String readFile(String path){
        File file = new File(path);
        StringBuilder body = new StringBuilder();


        try(Scanner reader = new Scanner(file)){
            while (reader.hasNextLine()){
                String nextLine = reader.nextLine();
                if(!nextLine.startsWith("#")){
                    body.append(nextLine);
                }
            }
        }catch (FileNotFoundException e){
            System.out.println("File Not Found");
            e.printStackTrace();
        }

        return body.toString();
    }

}

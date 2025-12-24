package com.javaPractice.Http;

import com.javaPractice.infra.Exception.EditorCloseWithErrorException;
import com.javaPractice.infra.Exception.EditorNotFoundException;

import java.io.IOException;
import java.util.List;

public class ProcessEngine {


    public String detectEditor(){
        String editor = System.getenv("VISUAL");
        if (editor == null || editor.isBlank()) {
            editor = System.getenv("EDITOR");
        }
        if (editor == null || editor.isBlank()) {
            editor = fallbackByOS();
        }
        if (editor == null || editor.isBlank()) {
            throw new EditorNotFoundException("There is not Found a Text Editor");
        }

        return editor;
    }

    private String fallbackByOS() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return "notepad.exe";
        }
        if (os.contains("mac")) {
            return "vim";
        }
        if (os.contains("nix") || os.contains("nux")) {
            return "vim";
        }

        return null;
    }



    public void launchEditor(String editor, String path){
        List<String> command = List.of(editor, path);


        try{
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO();

            Process process = pb.start();

            int exitCode = process.waitFor();

            if(exitCode != 0){
                throw new EditorCloseWithErrorException("Editor Closed With Error : " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("An Error Was Ocurred (ProcessEngine)");
            throw new RuntimeException(e);
        }


    }


}

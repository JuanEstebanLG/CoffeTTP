package com.javaPractice;

import com.beust.jcommander.JCommander;
import com.javaPractice.Files.FileEngine;
import com.javaPractice.Http.HttpEngine;
import com.javaPractice.data.METHODS;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args){
         String bodyPayloadPath = "bodyPayload.txt";
        HttpEngine engine = new HttpEngine();
        FileEngine fileEngine = new FileEngine();
        JCommander commander = JCommander.newBuilder()
                .addObject(engine)
                .build();

        commander.parse(args);

        fileEngine.createFile(bodyPayloadPath);
        fileEngine.writeFile(bodyPayloadPath);

        if(engine.getHelpValue()) {
            commander.usage();
            System.exit(0);
        }

        String uri = engine.getURI();
        METHODS method = engine.getMethod();
        String body = engine.getBody();

        if(body == null || body.isEmpty()){
            body = engine.writeBody(bodyPayloadPath);
        }else{
            System.out.printf("Body exist : " + body);
        }
        Map<String, String> headers = engine.getHeaders();

        try{

            engine.makeRequest(uri, method, body, headers);
        }catch (Exception e){
            throw new RuntimeException(e);

        }



    }
}
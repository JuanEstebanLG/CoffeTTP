package com.javaPractice.Http;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.javaPractice.Files.FileEngine;
import com.javaPractice.data.METHODS;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpEngine {
    private ProcessEngine pbEngine = new ProcessEngine();
    private FileEngine flEngine = new FileEngine();
    public HttpRequests requests = new HttpRequests();

    @Parameter(names = "--url", description = "URL to Send the Request, include ENDPOINT and PORTS", required = true)
    private  String URI = "";


    @Parameter(names= "-M", description = "Method of Petition/Request, (GET, POST, PUT, DELETE, REST, ETC.)", required = true)
    private METHODS method = METHODS.GET;

    @Parameter(names = "--body", description = "Field to make a body of request, get void it to open the editor" )
    private String body = "";

    @Parameter(names = "--help", help = true)
    private boolean help;

    @DynamicParameter(names = "-H", description = "Fields to Make Body")
    private Map<String, String> headers = new HashMap<>();





    public void makeRequest(String URI, METHODS method, String body, Map<String, String> headers) throws JsonProcessingException {

       /**
        * Pasar todo esto por un evaluador de simbolos es necesario, controlar la entrada debe ser requisito
        * */


        requests.request(URI,body,method,headers);
    }




    public String getURI(){
        return URI;
    }


    public METHODS getMethod(){
        return method;
    }


    public boolean getHelpValue(){
        return help;
    }

    public String getBody(){
        return this.body;
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

    public String writeBody(String path){
        String editor = pbEngine.detectEditor();
        System.out.println("Opening editor to write request body...");
        pbEngine.launchEditor(editor, path);
        return flEngine.readFile(path);
    }
}

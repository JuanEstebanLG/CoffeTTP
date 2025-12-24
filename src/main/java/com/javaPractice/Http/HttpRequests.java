package com.javaPractice.Http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.javaPractice.data.METHODS;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.logging.Logger;

import java.util.stream.Stream;



public class HttpRequests {

    private static final Logger logger = Logger.getLogger(HttpRequest.class.getName());
    private static final Set<String> NO_COMMA_HEADERS = Set.of(
            "set-cookie",
            "content-type",
            "authorization"
    );


    public HttpRequest requestConstructor(String URI, String body, METHODS method, Map<String, String> headers){

        Map<String, List<String>> validHeaders = new HashMap<>();
        headers.forEach((k, vs) -> validHeaders.put(k, parseValuesToArrayStringFormat(vs)));







        String[] headersFlat = validHeaders.entrySet()
                .stream()
                .flatMap(entry -> canConcatWithComma(entry.getKey())
                        ? Stream.of(entry.getKey(), getValuesForHeader(entry.getValue()))
                        : noCommaHeaders(entry))
                .toArray(String[]::new);

        String validMethod = method.name();
        HttpRequest.BodyPublisher publisher =
                body == null
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(body);

        return HttpRequest.newBuilder()
                .uri(java.net.URI.create(URI))
                .headers(headersFlat)
                .method(validMethod, publisher)
                .build();

    }




    private Stream<String> noCommaHeaders(Map.Entry<String, List<String>> entry) {
        String key = entry.getKey();
        List<String> headerValues = entry.getValue();

       if(key.equalsIgnoreCase("content-type") || key.equalsIgnoreCase("authorization")){

           if(headerValues.size() == 1){
                   return Stream.of(key, headerValues.getFirst());
               }else{
                   throw new RuntimeException("El header : " + key + " solo puede contener un valor");
               }

        }else{
           throw new RuntimeException("No es posible asignar cookies con esta herramienta (Set-Cookie Header no está soportado)");
       }
    }

    public void clientAndResponse(HttpRequest request) {

        final String RESET  = "\u001B[0m";
        final String RED    = "\u001B[31m";
        final String GREEN  = "\u001B[32m";
        final String YELLOW = "\u001B[33m";
        final String BLUE   = "\u001B[34m";
        final String CYAN   = "\u001B[36m";
        final String PURPLE = "\u001B[35m";
        final String BOLD   = "\u001B[1m";
        final String DIM    = "\u001B[2m";

        long start = System.currentTimeMillis();

        System.out.println();
        System.out.println(PURPLE + BOLD +
                "███ MYHTTP :: REQUEST DISPATCH ███"
                + RESET);

        System.out.println(DIM + "→ "
                + request.method()
                + " "
                + request.uri()
                + RESET);

        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            long time = System.currentTimeMillis() - start;
            boolean ok = response.statusCode() < 400;

            String statusColor = ok ? GREEN : RED;
            String statusLabel = ok ? "OK" : "ERR";

            System.out.println();
            System.out.println(statusColor + BOLD +
                    "[ " + statusLabel + " ] "
                    + RESET
                    + "STATUS "
                    + response.statusCode()
                    + DIM + " | TIME " + time + " ms" + RESET
            );

            // HEADERS
            System.out.println();
            System.out.println(CYAN + "┌─ HEADERS ─────────────────────────────" + RESET);
            response.headers().map().forEach((k, v) ->
                    System.out.println(CYAN + "│ " + k + " : " + String.join(", ", v) + RESET)
            );
            System.out.println(CYAN + "└──────────────────────────────────────" + RESET);

            // BODY
            System.out.println();
            System.out.println(BLUE + "┌─ BODY ────────────────────────────────" + RESET);
            System.out.println(BLUE +
                    response.body()
                    + RESET);
            System.out.println(BLUE + "└──────────────────────────────────────" + RESET);

        } catch (Exception e) {

            System.out.println();
            System.out.println(RED + BOLD +
                    "███ REQUEST FAILED ███"
                    + RESET);

            System.out.println(RED +
                    "✖ " + e.getMessage()
                    + RESET);

            throw new RuntimeException(e);
        }
    }



    public void request(String URI, String rawbody, METHODS method, Map<String, String> headers) throws JsonProcessingException {

        if(rawbody == null){
            rawbody = "";
        }

        HttpRequest request = requestConstructor(URI, rawbody, method, headers);
        clientAndResponse(request);
    }


    public String getValuesForHeader(List<String> values) {

        return String.join(", ", values);

    }

    public List<String> parseValuesToArrayStringFormat(String values){
        values = values.strip();
        return Arrays.asList(values.split(","));
    }



    public boolean canConcatWithComma(String entry){
        entry = entry.toLowerCase();
        return !NO_COMMA_HEADERS.contains(entry);
    }


}

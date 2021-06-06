package com.example.juiceup;

import android.util.JsonReader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class JsonParser {

    public JsonParser(){

    }


    public Queue<Double> parse_json_distances(JsonReader jsonReader) throws IOException{
        Queue<Double> queue = new LinkedList<Double>();

        parse_json_big_object(jsonReader, queue);

        return queue;
    }


    private void parse_json_big_object(JsonReader jsonReader, Queue<Double> queue) throws IOException{

        jsonReader.beginObject();

        while (jsonReader.hasNext()){
            String token = jsonReader.nextName();
            if (token.equals("rows")){
                parse_array_rows(jsonReader, queue);
            }
            else jsonReader.skipValue();
        }

        jsonReader.endObject();
    }

    private void parse_array_rows(JsonReader jsonReader, Queue<Double> queue) throws IOException{

        jsonReader.beginArray();

        while (jsonReader.hasNext()){
            parse_meaningless_object(jsonReader,queue );
        }
        jsonReader.endArray();

    }

    private void parse_meaningless_object(JsonReader jsonReader, Queue<Double> queue) throws IOException{
        jsonReader.beginObject();

        while (jsonReader.hasNext()){
            String token = jsonReader.nextName();

            if (token.equals("elements"))
                parse_array_elements(jsonReader, queue);

            else
                jsonReader.skipValue();
        }
        jsonReader.endObject();

    }

    private void parse_array_elements(JsonReader jsonReader, Queue<Double> queue) throws IOException{


        jsonReader.beginArray();

        while (jsonReader.hasNext()){
            parse_object_element(jsonReader, queue);
        }
        jsonReader.endArray();

    }

    private void parse_object_element (JsonReader jsonReader, Queue<Double> queue) throws IOException{
        jsonReader.beginObject();

        while (jsonReader.hasNext()){
            String token = jsonReader.nextName();

            if (token.equals("distance"))
                parse_object_distance(jsonReader, queue);
            else if (token.equals("status")){
                if (jsonReader.nextString().equals("ZERO_RESULTS"))
                    queue.add(-1.0);
            }
            else
                jsonReader.skipValue();
        }
        jsonReader.endObject();
    }

    private void parse_object_distance (JsonReader jsonReader, Queue<Double> queue) throws IOException{
        jsonReader.beginObject();

        while (jsonReader.hasNext()){
            String token = jsonReader.nextName();

            if(token.equals("value"))
                queue.add(jsonReader.nextDouble()/1000);
            else
                jsonReader.skipValue();
        }
        jsonReader.endObject();
    }
}

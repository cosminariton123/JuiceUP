package com.example.juiceup;

import android.util.JsonReader;
import android.util.JsonToken;
import android.view.LayoutInflater;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class JsonParser {

    public JsonParser(){

    }

    public LatLng parse_json_geocode(JsonReader jsonReader) throws IOException{
        LatLng result = null;

        result = parse_geocode_json_big_object(jsonReader);

        return result;
    }

    private LatLng parse_geocode_json_big_object(JsonReader jsonReader) throws IOException{
        LatLng result = null;

        jsonReader.beginObject();

        while (jsonReader.hasNext()){
            String token = jsonReader.nextName();
            if (token.equals("results")){
                result = parse_geocode_results_array(jsonReader);
            }
            else jsonReader.skipValue();
        }
        jsonReader.endObject();
        return result;
    }

    private LatLng parse_geocode_results_array(JsonReader jsonReader) throws IOException{
        LatLng result = null;

        jsonReader.beginArray();

        while (jsonReader.hasNext()){
            result = parse_geocode_result_array_object(jsonReader);
        }
        jsonReader.endArray();
        return result;
    }

    private LatLng parse_geocode_result_array_object(JsonReader jsonReader) throws IOException{
        LatLng result = null;

        jsonReader.beginObject();

        while (jsonReader.hasNext()){
            result = parse_geocode_result_object_after_parantheses(jsonReader);
        }

        jsonReader.endObject();
        return result;
    }

    private LatLng parse_geocode_result_object_after_parantheses(JsonReader jsonReader) throws IOException{
         LatLng result = null;

         while (jsonReader.hasNext()){
             if(!jsonReader.nextName().equals("geometry"))
                 jsonReader.skipValue();
             else
                 result = parse_geocode_geometry(jsonReader);
         }
         return result;
    }


    private LatLng parse_geocode_geometry(JsonReader jsonReader) throws IOException{
        LatLng result = null;

        jsonReader.beginObject();

        while (jsonReader.hasNext()){
            if(!jsonReader.nextName().equals("location"))
                jsonReader.skipValue();

            else
                result = parse_geocode_location(jsonReader);
        }

        jsonReader.endObject();

        return result;
    }

    private LatLng parse_geocode_location(JsonReader jsonReader) throws IOException{
        LatLng result = null;

        Double latitude = null;
        Double longitude = null;

        jsonReader.beginObject();
        if (jsonReader.nextName().equals("lat"))
            latitude = jsonReader.nextDouble();

        if (jsonReader.nextName().equals("lng"))
            longitude = jsonReader.nextDouble();
        jsonReader.endObject();

        result = new LatLng(latitude, longitude);

        return result;
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
                    queue.add(9999999.0);
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

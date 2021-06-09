package com.example.juiceup;

import android.app.Dialog;
import android.widget.ArrayAdapter;

import org.w3c.dom.Node;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

//Followed this guide https://stackabuse.com/graphs-in-java-a-star-algorithm and adjusted for my situation
//Altough it is a well known algorithm, pretty easy to implement, I chose to search for the an implementation
//online so I don't have to reinvent the wheel or try to port my own original implementation from Python3 language
//Also MyNode, MyEdge are inspired from the link above
public class AStar  {

    private MyNode starting_point;
    private MyNode end_point;
    private PriorityQueue<MyNode> closed;
    private PriorityQueue<MyNode> open;

    public AStar(){
        starting_point = null;
        end_point = null;
        closed = null;
        open = null;
    }

    public Queue<Integer> calculate_route(ChargingStation start_location, ChargingStation end_location, ArrayList<ChargingStation> chargingStations, ArrayList<Distance> distances){
        Queue<Integer> result = new LinkedList<Integer>();

        ArrayList<ChargingStation> chargingStations_copy = new ArrayList<ChargingStation>(chargingStations);
        select_stations_that_match_user_preferences(chargingStations_copy);
        get_data(start_location,end_location, chargingStations_copy, distances);

        Boolean succes = false;

        //Implementation of AStar
        closed = new PriorityQueue<>();
        open = new PriorityQueue<>();

        starting_point.set_g(0);
        open.add(starting_point);

        while (!open.isEmpty()){
            MyNode node = open.peek();
            if (node.get_id() == end_point.get_id()){
                end_point = node;
                succes = true;
                break;
            }

            for (MyEdge edge:
                 node.getSuccessors()) {
                MyNode successor = edge.get_node();
                Integer distance_with_this_successor = node.get_g() + edge.get_weight();

                if (!open.contains(successor) && !closed.contains(successor)){
                    successor.set_parent(node);
                    successor.set_g(distance_with_this_successor);
                    successor.set_f(successor.get_g() + successor.get_h());
                    open.add(successor);
                }
                else{
                    if (distance_with_this_successor < successor.get_g()){
                        successor.set_parent(node);
                        successor.set_g(distance_with_this_successor);
                        successor.set_f(successor.get_g() + successor.get_h());

                        if(closed.contains(successor)){
                            closed.remove(successor);
                            open.add(successor);
                        }
                    }
                }
            }
            open.remove(node);
            closed.add(node);
        }
        if (succes == true){
            MyNode node = new MyNode();
            node = end_point;
            while (node.get_parent() != null) {
                result.add(node.get_id());
                node = node.get_parent();
            }

            result.add(node.get_id());
        }

        Stack<Integer> aux_for_reverse = new Stack<Integer>();

        while (!result.isEmpty())
            aux_for_reverse.push(result.remove());

        while (!aux_for_reverse.isEmpty())
            result.add(aux_for_reverse.pop());

        return result;
    }



    private void get_data(ChargingStation start_location, ChargingStation end_location, ArrayList<ChargingStation> chargingStations, ArrayList<Distance> distances){

        starting_point = new MyNode();
        end_point = new MyNode();

        start_location.set_id(-1);
        end_location.set_id(0);

        starting_point.setId(-1);
        end_point.setId(0);

        ArrayList<ChargingStation> stations_copy = new ArrayList<>(chargingStations);
        ArrayList<Distance> copy_distances = new ArrayList<>(distances);
        stations_copy.add(start_location);

        DistancesAndGeocodings distancesAndGeocodings = new DistancesAndGeocodings();
        Queue<Integer> spherical_distances_heuristic = distancesAndGeocodings.get_spherical_distances(end_location, stations_copy);
        Queue<Integer> road_distances_to_end_location = distancesAndGeocodings.get_road_distance(end_location, stations_copy);

        for (ChargingStation station:
             stations_copy) {
            Integer distance_value =  road_distances_to_end_location.remove();
            Distance distance = new Distance(end_location.get_id(), station.get_id(), distance_value);
            Distance distance_inverse = new Distance(station.get_id(), end_location.get_id(), distance_value);
            copy_distances.add(distance);
            copy_distances.add(distance_inverse);
        }

        stations_copy = new ArrayList<>(chargingStations);
        Queue<Integer> road_distances_to_start_location = distancesAndGeocodings.get_road_distance(start_location, stations_copy);

        for (ChargingStation station:
             stations_copy) {
            Integer distance_value =  road_distances_to_start_location.remove();
            Distance distance = new Distance(start_location.get_id(), station.get_id(), distance_value);
            Distance distance_inverse = new Distance(station.get_id(), start_location.get_id(), distance_value);
            copy_distances.add(distance);
            copy_distances.add(distance_inverse);
        }

        stations_copy = new ArrayList<>(chargingStations);
        stations_copy.add(end_location);


        starting_point.set_parent(null); // h is aleready max int;

        ArrayList<MyNode> nodes = new ArrayList<MyNode>();

        nodes.add(starting_point);

        for (ChargingStation elem:
             stations_copy) {
            MyNode node = new MyNode();
            node.setId(elem.get_id());
            node.set_h(spherical_distances_heuristic.remove());
            nodes.add(node);
        }

        CurrentUser currentUser = CurrentUser.getInstance();

        for (MyNode node:
             nodes) {
            for (Distance distance:
                 copy_distances) {
                if (distance.getId_from() == node.get_id())
                    for (MyNode node2:
                         nodes) {
                        if (node2.get_id() == distance.getId_to()) {
                            if (distance.getRoad_distance() < currentUser.get_car_max_km_range()) {//We only insert the distances that are smaller than the users car range
                                MyEdge edge = new MyEdge();
                                edge.set_weight(distance.getRoad_distance());
                                edge.set_node(node2);
                                node.add_succesor(edge);
                            }
                        }
                    }
            }
        }
    }


    private void select_stations_that_match_user_preferences(ArrayList<ChargingStation> chargingStations){

        CurrentUser currentUser = CurrentUser.getInstance();

        for(int i = 0 ; i < chargingStations.size(); i++){
            if (chargingStations.get(i).get_wall() == 0 && currentUser.get_wall_preference() == 1) {
                chargingStations.remove(i);
                i--;
            }
            else if (chargingStations.get(i).get_type2() == 0 && currentUser.get_type2_preference() == 1){
                chargingStations.remove(i);
                i--;
            }
            else if (chargingStations.get(i).get_supercharger() == 0 && currentUser.get_supercharger_preference() == 1){
                chargingStations.remove(i);
                i--;
            }
            else if (chargingStations.get(i).get_guarded() == 0 && currentUser.get_guarded_preference() == 1){
                chargingStations.remove(i);
                i--;
            }
            else if (chargingStations.get(i).get_outputkwh() < currentUser.get_min_kwh_preference()){
                chargingStations.remove(i);
                i--;
            }
            else if (chargingStations.get(i).get_parking_number_of_places() < currentUser.get_of_parking_spots_preference()){
                chargingStations.remove(i);
                i--;
            }
            else if (chargingStations.get(i).get_rating() < currentUser.get_min_rating_preference()){
                chargingStations.remove(i);
                i--;
            }
            else{
                Integer trust_score = get_the_users_who_placed_the_station_score(chargingStations.get(i));
                if (trust_score < currentUser.get_min_trust_preference()){
                    chargingStations.remove(i);
                    i--;
                }
            }
        }
    }

    private Integer get_the_users_who_placed_the_station_score(ChargingStation chargingStation){
        ConnectionDB connectionDB = ConnectionDB.getInstance();
        Connection connection = connectionDB.getConnection();

        if (connection != null){

            Statement statement = null;
            try {
                statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT trust_score FROM users WHERE email LIKE '" + chargingStation.get_set_by_user() + "'");
            resultSet.next();

            Double trust_score = resultSet.getDouble(1);

            if (trust_score < 33)
                return  1;

            if (trust_score > 66)
                return 3;

            return 2;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return 1; //If an anomaly happend, we don't have trust
            }
        }
        else
            return 1; //We don't have trust if we can't check it
    }

}

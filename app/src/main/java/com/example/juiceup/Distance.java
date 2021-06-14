package com.example.juiceup;

public class Distance {

    private Integer id_from;
    private Integer id_to;
    private Integer road_distance;

    public Distance(){
        id_from = 0;
        id_to = 0;
        id_to = 0;
    }

    public Distance(Integer idfrom , Integer idto, Integer roaddistance){
        id_from = idfrom;
        id_to = idto;
        road_distance = roaddistance;
    }

    public Integer getId_from() {
        return id_from;
    }

    public void setId_from(Integer id_from) {
        this.id_from = id_from;
    }

    public Integer getId_to() {
        return id_to;
    }

    public void setId_to(Integer id_to) {
        this.id_to = id_to;
    }

    public Integer getRoad_distance() {
        return road_distance;
    }

    public void setRoad_distance(Integer road_distance) {
        this.road_distance = road_distance;
    }
}

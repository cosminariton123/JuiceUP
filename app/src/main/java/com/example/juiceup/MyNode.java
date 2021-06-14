package com.example.juiceup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//inspired from  https://stackabuse.com/graphs-in-java-a-star-algorithm
//See AStar.java for more information
public class MyNode implements Comparable<MyNode> {

    private Integer id;
    private MyNode parent;
    private List<MyEdge> successors;
    private Integer f;
    private Integer g;
    private Integer h;

    public MyNode(){
        id = 0;
        parent = null;
        successors = new ArrayList<>();
        f = Integer.MAX_VALUE;
        g = Integer.MAX_VALUE;
        h = Integer.MAX_VALUE;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void set_parent(MyNode parent) {
        this.parent = parent;
    }

    public void set_f(Integer f) {
        this.f = f;
    }

    public MyNode get_parent() {
        return parent;
    }

    public Integer get_h() {
        return h;
    }

    public Integer get_g() {
        return g;
    }

    public Integer get_f() {
        return f;
    }

    public Integer get_id() {
        return id;
    }

    public void set_h(Integer h) {
        this.h = h;
    }

    public void set_g(Integer g) {
        this.g = g;
    }

    public void add_succesor(MyEdge succesor){
        this.successors.add(succesor);
    }

    public List<MyEdge> getSuccessors() {
        return successors;
    }

    public Integer edge_size(){
        return successors.size();
    }

    @Override
    public int compareTo(MyNode o) {
        if (this.f < o.f)
            return -1;

        if (this.f == o.f)
            if (this.g > o.g)
                return -1;

        if (this.f == o.f)
            if (this.g == o.g)
                return 0;

        return 1;
    }
}

package com.example.juiceup;

public class MyEdge {

    private Integer weight;
    private MyNode node;

    public MyEdge(){
        weight = 0;
        node = null;
    }

    public void set_node(MyNode node) {
        this.node = node;
    }

    public void set_weight(Integer weight) {
        this.weight = weight;
    }

    public Integer get_weight(){
        return weight;
    }

    public MyNode get_node(){
        return node;
    }
}

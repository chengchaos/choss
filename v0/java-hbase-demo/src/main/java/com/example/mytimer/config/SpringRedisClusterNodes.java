package com.example.mytimer.config;

import java.util.List;


public class SpringRedisClusterNodes {
 
    private List<String> nodes;
    
    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }
}

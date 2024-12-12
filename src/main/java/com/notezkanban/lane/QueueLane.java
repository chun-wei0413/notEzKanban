//package com.notezkanban.lane;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Queue;
//
//import com.notezkanban.card.Card;
//
//public class QueueLane implements Lane {
//    private String LaneId;
//    private String LaneName;
//    private int capacity;
//    private Queue<Card> queue;
//
//    public QueueLane(String LaneId, String LaneName, int capacity) {
//        this.LaneId = LaneId;
//        this.LaneName = LaneName;
//        this.capacity = capacity;
//        this.queue = new LinkedList<>();
//    }
//
//    @Override
//    public String getLaneId() {
//        return LaneId;
//    }
//
//    @Override
//    public String getLaneName() {
//        return LaneName;
//    }
//
//    @Override
//    public List<Card> getCards() {
//        return new ArrayList<>(queue);
//    }
//
//    public int getCapacity() {
//        return capacity;
//    }
//
//    @Override
//    public List<Lane> getChildren() {
//        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented method 'getChildren'");
//    }
//}

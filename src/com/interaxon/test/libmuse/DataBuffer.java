package com.interaxon.test.libmuse;

// Someone needs to make this code more portable!

// ArrayList<Double> Packet.getValues();

import java.util.concurrent.ConcurrentLinkedQueue;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;

public class DataBuffer
{
    private final int SIZE_LIMIT = 2048;
    public ConcurrentLinkedQueue<MuseDataPacket> incomingPackets;
    public int size;
    public ArrayList<Double> dataSamples;

    public DataBuffer()
    {
        this.incomingPackets = new ConcurrentLinkedQueue();
        this.size = new Integer();
    }

    public int size() {
        return this.size;
    }

    public MuseDataPacket pop() {
        this.size--;
        return (MuseDataPacket)this.incomingPackets.poll();
    }

    public void receive(MuseDataPacket p)
    {
        getClass(); if (this.incomingPackets.size() > SIZE_LIMIT) {
        this.size--;
        this.incomingPackets.poll();
        this.dataSamples.removeRange(0,16); // ASSUMES 16 values / packet
    }
        this.incomingPackets.add(p);
        this.dataSamples.addAll(p.getValues());
        this.size++;
    }
}
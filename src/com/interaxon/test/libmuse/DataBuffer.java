package com.interaxon.test.libmuse;

// Someone needs to make this code more portable!

import java.util.concurrent.ConcurrentLinkedQueue;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;

public class DataBuffer
{
    private final int SIZE_LIMIT = 2048;
    public ConcurrentLinkedQueue<MuseDataPacket> incomingData;
    public int size;

    public DataBuffer()
    {
        this.incomingData = new ConcurrentLinkedQueue();
        this.size = new Integer();
    }

    public int size() {
        return this.size;
    }

    public MuseDataPacket pop() {
        this.size--;
        return (MuseDataPacket)this.incomingData.poll();
    }

    public void receive(MuseDataPacket p)
    {
        getClass(); if (this.incomingData.size() > SIZE_LIMIT) {
        this.size--;
        this.incomingData.poll();
    }
        this.incomingData.add(p);
        this.size++;
    }
}
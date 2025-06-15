package com.doubleC.Pmanager;

class Partition {
    public int startAddress;
    public int size;
    public boolean isFree;
    public String jobName;


    public Partition(int startAddress, int size, boolean isFree, String jobName) {
        this.startAddress = startAddress;
        this.size = size;
        this.isFree = isFree;
        this.jobName = jobName;
    }

    public void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    @Override
    public String toString() {
        return "[" + startAddress + " - " + (startAddress + size) + "] "
                + (isFree ? "空闲" : "作业：" + jobName) + "，大小：" + size + "KB";
    }
}

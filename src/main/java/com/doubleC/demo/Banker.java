package com.doubleC.demo;


import java.util.List;

class Process {
    private String pID;
    private int[] total;
    private int[] allocation;
    private int[] need;
    private boolean finished;

    //店中店构造函数，这段可以直接不看
    public Process(String pID, int[] total, int[] allocation) {
        this.pID = pID;
        this.total = total;
        this.allocation = allocation;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public int[] getTotal() {
        return total;
    }

    public void setTotal(int[] total) {
        this.total = total;
    }

    public int[] getAllocation() {
        return allocation;
    }

    public void setAllocation(int[] allocation) {
        this.allocation = allocation;
    }

    public int[] getNeed() {
        return need;
    }

    public void setNeed(int[] need) {
        this.need = need;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }


    //进程分配检测函数

    public boolean canBeSatisfied(int[] resources){
        for (int i =0;i < resources.length ; i++){
            if (this.need[i] > resources[i]){
                return false;
            }
        }
        return true;
    }

    //分配，但不多函数(没有一次性满足)

    public void allocation(int[] request) {
        for (int i = 0; i < request.length; i++) {
            if (this.need[i] <= request[i]) {
                this.need[i] -= request[i];
                this.allocation[i] += request[i];


            }
        }
    }



}

public class Banker {
    //系统自带属性： 系统当前资源
    public int[] available;

    List<Process> processes;


}

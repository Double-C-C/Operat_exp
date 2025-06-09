package com.doubleC.demo;


import java.util.ArrayList;
import java.util.List;

class Process {
    private String pID;
    private int[] total;
    private int[] allocation;
    private int[] need;
    private boolean finished;

    //店中店构造函数，这段可以直接不看,唯一需要注意的是计算need的值
    public Process(String pID, int[] total, int[] allocation) {
        this.pID = pID;
        this.total = total;
        this.allocation = allocation;

        this.need = new int[total.length];
        for (int i =0; i < total.length ; i++){
            need[i] = total[i] - allocation[i];
        }

        this.setFinished(false);
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

    public List<Process> processes;

    public Banker(int[] available, List<Process> processes) {
        this.available = available;
        this.processes = processes;
    }

    public void isSafe(){
        List<Process> secureProcess = new ArrayList<>();
        //进行初始化，创建安全序列
        int[] work = available.clone();
        for (Process process : processes){
            process.setFinished(false);
        }

        while (secureProcess.size() < processes.size()){
            boolean safe = false;
            for (Process p : processes){

                if (p.isFinished() == false && p.canBeSatisfied(work)){
                    //将P加入安全队列
                    secureProcess.add(p);

                    //模拟资源释放
                    for (int i = 0; i < work.length ; i++){
                        work[i] += p.getAllocation()[i];
                    }
                    p.setFinished(true);
                    safe = true;
                }

            }

            if (!safe) {
                System.out.println("死锁已发生.");
                return;
            }

        }
        System.out.println("系统顺利运行! 安全队列为 : ");
        for (Process p: secureProcess){
            System.out.print(p.getpID() + " ");

        }
    }


    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        Process p1 = new Process("P1",new int[]{7,5,3},new int[]{0,1,0});
        Process p2 = new Process("P2",new int[]{3,2,2},new int[]{2,0,0});
        Process p3 = new Process("P3",new int[]{9,0,2},new int[]{3,0,2});
        Process p4 = new Process("P4",new int[]{2,2,2},new int[]{2,1,1});
        Process p5 = new Process("P5",new int[]{4,3,3},new int[]{0,0,2});
        processes.add(p1);
        processes.add(p2);
        processes.add(p3);
        processes.add(p4);
        processes.add(p5);

        int[] available = {3,3,2};
        Banker banker = new Banker(available,processes);

        banker.isSafe();
    }
}

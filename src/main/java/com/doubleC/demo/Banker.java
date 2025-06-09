package com.doubleC.demo;


import java.util.ArrayList;
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
                        work[i] += p.getTotal()[i];
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
}

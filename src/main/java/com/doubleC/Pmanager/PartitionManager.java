package com.doubleC.Pmanager;

import java.util.*;
public class PartitionManager {

    public List<Partition> partitions = new LinkedList<>();
    public PartitionManager(){
        partitions.add(new Partition(0,640,true,null));
    }

    //TODO: 1 - 实现链表打印
    // 2 - 实现首次适应和最佳适应
    public void allocateFirstFit(String jobName, int size) {
        for (int i = 0; i < partitions.size(); i++) {
            Partition p = partitions.get(i);
            if (p.isFree && p.size >= size) {
                if (p.size == size) {
                    // 刚好分配
                    p.isFree = false;
                    p.jobName = jobName;
                } else {
                    // 先构造新分区
                    Partition newP = new Partition(p.startAddress, size, false, jobName);
                    // 再更新原空闲块
                    p.startAddress += size;
                    p.size -= size;
                    // 再插入新分区
                    partitions.add(i, newP);
                }
                System.out.println("进程 " + jobName + " 分配成功：" + size + "KB");
                return;
            }
        }
        System.out.println("进程 " + jobName + " 未能找到足够大的内存块分配。");
    }


    public void allocateBestFit(String jobName, int size){
        Partition bestfit = null;
        int bindex = -1;
        for (int i = 0 ;i <partitions.size();i++){
            Partition p = partitions.get(i);

            if (p.isFree == true && p.size >= size){

                if (bestfit == null || bestfit.size >= p.size){
                    bestfit = p;
                    bindex = i;
                }
            }
        }

        //如果没能找到就提示
        if (bestfit == null){
            System.out.println("进程 " + jobName + " 未能找到足够大的内存块分配。");
        }

        if (bestfit.size == size){
            bestfit.isFree = false;
            bestfit.jobName = jobName;
        }else {
            Partition newp = new Partition(bestfit.startAddress,size, false, jobName);
            bestfit.startAddress += size;
            bestfit.size -= size;
            partitions.add(bindex,newp);
        }
    }

    public void free(String jobName){
        for (int i = 0; i < partitions.size(); i++){
            Partition p =partitions.get(i);
            if (jobName.equals(p.jobName)){
                p.isFree = true;
                p.jobName = null;

                //检查边界情况
                //先检查右边可以防止p被remove后为null的情况
                if ( i + 1 <=partitions.size() -1){
                    Partition back = partitions.get(i + 1);

                    if (back.isFree == true){
                        p.size += back.size;
                        partitions.remove(i + 1);
                    }
                }
                if (i - 1 >= 0){
                    Partition front = partitions.get(i - 1);
                    //检测到空闲，进行合并
                    if (front.isFree == true){
                        front.size += p.size;
                        partitions.remove(i);
                        i--;
                    }
                }
                System.out.println("已将进程 " + jobName + "释放。");
                return;
            }
        }
        System.out.println("未找到名字为 " + jobName + "的进程！");
    }

    public void printPartitions() {
        System.out.println("当前内存状态：");
        for (Partition p : partitions) {
            System.out.println(p);
        }
        System.out.println("------------------------------");
    }

    public void analyzeMemory(String tag) {
        int totalMemory = 0;
        int usedMemory = 0;
        int freeBlocks = 0;
        int totalFree = 0;
        int minFreeBlock = Integer.MAX_VALUE;

        for (Partition p : partitions) {
            totalMemory += p.size;
            if (p.isFree) {
                freeBlocks++;
                totalFree += p.size;
                if (p.size < minFreeBlock) minFreeBlock = p.size;
            } else {
                usedMemory += p.size;
            }
        }

        System.out.println("📊 [" + tag + "] 分析报告：");
        System.out.println("总内存：" + totalMemory + "KB");
        System.out.println("已使用：" + usedMemory + "KB");
        System.out.println("空闲块数：" + freeBlocks);
        System.out.println("空闲总量：" + totalFree + "KB");
        System.out.println("最小空闲块：" + (freeBlocks > 0 ? minFreeBlock + "KB" : "无"));
        System.out.printf("内存利用率：%.2f%%\n", (usedMemory * 100.0 / totalMemory));
        System.out.println("-----------------------------");
    }




    public static void main(String[] args) {

    {
        PartitionManager pm = new PartitionManager();
        System.out.println("===== FisrtFit算法 =====");
        pm.allocateFirstFit("Job 1", 130);
        pm.printPartitions();
        pm.allocateFirstFit("Job 2", 60);
        pm.printPartitions();
        pm.allocateFirstFit("Job 3", 100);
        pm.printPartitions();
        pm.free("Job 2");
        pm.printPartitions();
        pm.allocateFirstFit("Job 4", 200);
        pm.printPartitions();
        pm.free("Job 3");
        pm.printPartitions();
        pm.free("Job 1");
        pm.printPartitions();
        pm.allocateFirstFit("Job 5", 140);
        pm.printPartitions();
        pm.allocateFirstFit("Job 6", 60);
        pm.printPartitions();
        pm.allocateFirstFit("Job 7", 50);
        pm.printPartitions();
        pm.allocateFirstFit("Job 8", 60);
        pm.printPartitions();

        pm.analyzeMemory("First Fit");
    }
    {
        PartitionManager pm = new PartitionManager();
        System.out.println("===== BestFit算法 =====");
        pm.allocateBestFit("Job 1", 130);
        pm.printPartitions();
        pm.allocateBestFit("Job 2", 60);
        pm.printPartitions();
        pm.allocateBestFit("Job 3", 100);
        pm.printPartitions();
        pm.free("Job 2");
        pm.printPartitions();
        pm.allocateBestFit("Job 4", 200);
        pm.printPartitions();
        pm.free("Job 3");
        pm.printPartitions();
        pm.free("Job 1");
        pm.printPartitions();
        pm.allocateBestFit("Job 5", 140);
        pm.printPartitions();
        pm.allocateBestFit("Job 6", 60);
        pm.printPartitions();
        pm.allocateBestFit("Job 7", 50);
        pm.printPartitions();
        pm.allocateBestFit("Job 8", 60);
        pm.printPartitions();

        pm.analyzeMemory("Best Fit");
    }


}
}

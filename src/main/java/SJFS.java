
import java.util.*;

class PCB {
    String name;
    int arrivalTime;
    int totalTime;
    int usedTime = 0;
    String state = "W"; // W: 就绪, R: 运行, F: 完成

    int finishTime;
    int turnaroundTime;
    int waitingTime;

    PCB(String name, int arrivalTime, int totalTime) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.totalTime = totalTime;
    }

    boolean isFinished() {
        return usedTime >= totalTime;
    }



    public static void SJFS(List<PCB> processes){
        List<PCB> Waits = new ArrayList<>(processes);

        List<PCB> Finishes = new ArrayList<>();
        PCB run = null;

        int currentTime = 0;
        int totalTurnaround = 0;
        int totalWaiting = 0;
        System.out.println("\n\n===== 短作业优先算法 =====");



        while (!Waits.isEmpty() || run != null){
            List<PCB> Ready = new ArrayList<>();
            for (PCB p : Waits) {
                if (currentTime >= p.arrivalTime && p.state.equals("W")) {
                    Ready.add(p);
                }
            }
            if (run == null && !Ready.isEmpty()) {
                run = Collections.min(Ready, Comparator.comparingInt(p -> p.totalTime));
                run.state = "R";
                Waits.remove(run);
            }

            System.out.println("\n[时间 " + currentTime + "]");
            if (run != null) {
                System.out.println("当前运行进程:\n" + run);
            } else {
                System.out.println("CPU 空闲");
            }

            System.out.println("就绪进程:");
            if (Ready.isEmpty()) {
                System.out.println("无");
            } else {
                for (PCB p : Ready) {
                    System.out.println(p);
                }
            }

            System.out.println("已完成进程:");
            if (Finishes.isEmpty()) {
                System.out.println("无");
            } else {
                for (PCB p : Finishes) {
                    System.out.println(p);
                }
            }

            if (run != null) {
                run.usedTime++;
                if (run.usedTime == run.totalTime) {
                    run.state = "F";
                    int finishTime = currentTime + 1;
                    int turnaroundTime = finishTime - run.arrivalTime;
                    int waitingTime =turnaroundTime - run.totalTime;

                    totalTurnaround += turnaroundTime;
                    totalWaiting += waitingTime;
                    Finishes.add(run);
                    run = null;
                }
            }

            currentTime++;
        }

        int n = Finishes.size();
        System.out.println("\n===== 所有进程已完成 =====");
        System.out.printf("平均等待时间: %.2f\n", totalWaiting * 1.0 / n);
        System.out.printf("平均周转时间: %.2f\n", totalTurnaround * 1.0 / n);

    }

    public static void SRTF(List<PCB> processes){
        List<PCB> all = new ArrayList<>(processes);
        List<PCB> finishes = new ArrayList<>();
        PCB run = null;

        int currentTime = 0;
        int totalTurnaround = 0;
        int totalWaiting = 0;
        System.out.println("\n\n===== 抢占式短作业优先算法（SRTF） =====");

        while (finishes.size() < processes.size()) {
            List<PCB> ready = new ArrayList<>();
            for (PCB p : all) {
                if (currentTime >= p.arrivalTime && !p.state.equals("F") && p.usedTime < p.totalTime) {
                    ready.add(p);
                }
            }

            if (!ready.isEmpty()) {
                PCB next = Collections.min(ready, Comparator.comparingInt(p -> p.totalTime - p.usedTime));
                if (run == null || run != next) {
                    run = next;
                    run.state = "R";
                }
            } else {
                run = null;
            }

            System.out.println("\n[时间 " + currentTime + "]");
            if (run != null) {
                System.out.println("当前运行进程:\n" + run);
            } else {
                System.out.println("CPU 空闲");
            }

            System.out.println("就绪进程:");
            if (ready.isEmpty()) {
                System.out.println("无");
            } else {
                for (PCB p : ready) {
                    if (p != run) {
                        System.out.println(p);
                    }
                }
            }

            System.out.println("已完成进程:");
            if (finishes.isEmpty()) {
                System.out.println("无");
            } else {
                for (PCB p : finishes) {
                    System.out.println(p);
                }
            }

            if (run != null) {
                run.usedTime++;
                if (run.usedTime == run.totalTime) {
                    run.state = "F";
                    run.finishTime = currentTime + 1;
                    run.turnaroundTime = run.finishTime - run.arrivalTime;
                    run.waitingTime = run.turnaroundTime - run.totalTime;
                    totalWaiting += run.waitingTime;
                    totalTurnaround += run.turnaroundTime;
                    finishes.add(run);
                    run = null;
                }
            }

            currentTime++;
        }

        int n = finishes.size();
        System.out.println("\n===== 所有进程已完成 =====");
        System.out.printf("平均等待时间: %.2f\n", totalWaiting * 1.0 / n);
        System.out.printf("平均周转时间: %.2f\n", totalTurnaround * 1.0 / n);

    }
    @Override
    public String toString() {
        return String.format("进程[%s] 到达:%d 总:%d 用:%d 状态:%s",
                name, arrivalTime, totalTime, usedTime, state);
    }
}

public class SJFS {
    public static void main(String[] args) {
        List<PCB> processes = new ArrayList<>();
        PCB P1 = new PCB("P1",0,1);
        PCB P2 = new PCB("P2",0,2);
        PCB P3 = new PCB("P3",1,1);
        PCB P4 = new PCB("P4",3,5);
        PCB P5 = new PCB("P5",5,2);
        PCB P6 = new PCB("P6",10,2);
        processes.add(P1);
        processes.add(P2);
        processes.add(P3);
        processes.add(P4);
        processes.add(P5);
        processes.add(P6);
        //PCB.SJFS(processes);

        PCB.SRTF(processes);
    }
}


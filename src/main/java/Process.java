import java.util.Comparator;
import java.util.*;
import java.util.stream.Collectors;

class Process {
    public String pid;
    public int arriveTime;
    public int burstTime;

    int finishTime;
    int turnaroundTime;
    int waitingTime;

    int priority;

    public Process(String pid, int arriveTime, int burstTime, int priority) {
        this.pid = pid;
        this.arriveTime = arriveTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }


    public static void FCFSScheduling(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arriveTime));

        int currentTime = 0;
        int totalTurnaround = 0;
        int totalWaiting = 0;
        System.out.println("\n\n===== 先来先服务算法 =====");
        System.out.println("PID\tArrival\tBurst\tFinish\tTurnaround\tWaiting");

        for (Process p : processes) {
            if (currentTime < p.arriveTime) {
                currentTime = p.arriveTime;
            }
            int startTime = currentTime;
            int finishTime = startTime + p.burstTime;
            int turnaroundTime = finishTime - p.arriveTime;
            int waitingTime = turnaroundTime - p.burstTime;

            currentTime = finishTime;

            // 累加统计量
            totalTurnaround += turnaroundTime;
            totalWaiting += waitingTime;

            // 输出进程信息
            System.out.printf("%s\t%d\t%d\t%d\t%d\t%d\n",
                    p.pid, p.arriveTime, p.burstTime, finishTime, turnaroundTime, waitingTime);
        }

        int n = processes.size();
        System.out.printf("平均周转时间: %.2f\n", totalTurnaround * 1.0 / n);
        System.out.printf("平均等待时间: %.2f\n", totalWaiting * 1.0 / n);

    }

    public static void sjfScheduling(List<Process> processes) {
        List<Process> completed = new ArrayList<>();
        List<Process> readyQueue = new ArrayList<>(processes); // 未完成的进程列表

        int currentTime = 0;
        int totalTurnaround = 0;
        int totalWaiting = 0;
        System.out.println("\n\n===== 短作业优先算法 =====");
        System.out.println("PID\tArrival\tBurst\tFinish\tTurnaround\tWaiting");

        while (!readyQueue.isEmpty()) {
            // 找出当前时间前到达的进程，加入候选列表
            List<Process> available = new ArrayList<>();
            for (Process p : readyQueue) {
                if (p.arriveTime <= currentTime) {
                    available.add(p);
                }
            }


            // 如果没有进程到达，时间前进
            if (available.isEmpty()) {
                currentTime++;
                continue;
            }

            // 在可执行进程中找 burstTime 最小的
            Process shortest = Collections.min(available, Comparator.comparingInt(p -> p.burstTime));

            // 调度这个进程
            int finishTime = currentTime + shortest.burstTime;
            int turnaroundTime = finishTime - shortest.arriveTime;
            int waitingTime = turnaroundTime - shortest.burstTime;

            // 存入进程对象（可选）
            shortest.finishTime = finishTime;
            shortest.turnaroundTime = turnaroundTime;
            shortest.waitingTime = waitingTime;

            // 输出
            System.out.printf("%s\t%d\t%d\t%d\t%d\t\t%d\n",
                    shortest.pid, shortest.arriveTime, shortest.burstTime,
                    finishTime, turnaroundTime, waitingTime);

            // 更新时间、累计统计、从就绪队列中移除
            currentTime = finishTime;
            totalTurnaround += turnaroundTime;
            totalWaiting += waitingTime;
            readyQueue.remove(shortest);
            completed.add(shortest);
        }

        int n = processes.size();
        System.out.printf("平均周转时间: %.2f\n", totalTurnaround * 1.0 / n);
        System.out.printf("平均等待时间: %.2f\n", totalWaiting * 1.0 / n);
    }

    public static void priorityScheduling(List<Process> processes) {
        List<Process> readyQueue = new ArrayList<>(processes);
        int currentTime = 0;
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;


        System.out.println("\n\n===== 优先级调度算法 =====");
        System.out.println("PID\tArrival\tBurst\tPriority\tFinish\tTurnaround\tWaiting");

        while (!readyQueue.isEmpty()) {
            List<Process> available = new ArrayList<>();
            for (Process p : readyQueue) {
                if (p.arriveTime <= currentTime) {
                    available.add(p);
                }
            }

            if (available.isEmpty()) currentTime++;

            Process highestPp = Collections.min(available, Comparator.comparingInt(p -> p.priority));

            // 调度这个进程
            int finishTime = currentTime + highestPp.burstTime;
            int turnaroundTime = finishTime - highestPp.arriveTime;
            int waitingTime = turnaroundTime - highestPp.burstTime;

            // 存入进程对象（可选）
            highestPp.finishTime = finishTime;
            highestPp.turnaroundTime = turnaroundTime;
            highestPp.waitingTime = waitingTime;

            System.out.printf("%s\t%d\t%d\t%d\t\t%d\t%d\t\t%d\n",
                    highestPp.pid, highestPp.arriveTime, highestPp.burstTime,
                    highestPp.priority, finishTime, turnaroundTime, waitingTime);

            currentTime = finishTime;
            totalTurnaroundTime += turnaroundTime;
            totalWaitingTime += waitingTime;
            readyQueue.remove(highestPp);
        }

        int n = processes.size();
        System.out.printf("平均周转时间: %.2f\n", totalTurnaroundTime * 1.0 / n);
        System.out.printf("平均等待时间: %.2f\n", totalWaitingTime * 1.0 / n);

    }

    public static void HRRN(List<Process> processes) {
        List<Process> readyQueue = new ArrayList<>(processes);
        int currentTime = 0;
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;

        System.out.println("\n\n===== 高响应比优先级调度算法 =====");
        System.out.println("PID\tArrival\tBurst\tPriority\tFinish\tTurnaround\tWaiting");

        while (!readyQueue.isEmpty()) {
            List<Process> available = new ArrayList<>();
            for (Process p : readyQueue) {
                if (p.arriveTime <= currentTime) {
                    int waitingTime = currentTime - p.arriveTime;
                    double responseRatio = (waitingTime + p.burstTime) * 1.0 / p.burstTime;
                    p.priority = (int) responseRatio;

                    available.add(p);
                }
            }

            if (available.isEmpty()) currentTime++;

            Process highestPp = Collections.max(available,Comparator.comparingInt(p -> p.priority));

            // 调度这个进程
            int finishTime = currentTime + highestPp.burstTime;
            int turnaroundTime = finishTime - highestPp.arriveTime;
            int waitingTime = turnaroundTime - highestPp.burstTime;

            // 存入进程对象（可选）
            highestPp.finishTime = finishTime;
            highestPp.turnaroundTime = turnaroundTime;
            highestPp.waitingTime = waitingTime;

            System.out.printf("%s\t%d\t%d\t%d\t\t%d\t%d\t\t%d\n",
                    highestPp.pid, highestPp.arriveTime, highestPp.burstTime,
                    highestPp.priority, finishTime, turnaroundTime, waitingTime);

            currentTime = finishTime;
            totalTurnaroundTime += turnaroundTime;
            totalWaitingTime += waitingTime;
            readyQueue.remove(highestPp);

        }
        int n = processes.size();
        System.out.printf("平均周转时间: %.2f\n", totalTurnaroundTime * 1.0 / n);
        System.out.printf("平均等待时间: %.2f\n", totalWaitingTime * 1.0 / n);


    }

}

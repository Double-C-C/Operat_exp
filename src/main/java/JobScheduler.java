import java.util.*;

class JCB {
    String name;
    int submitTime;
    int runTime;
    String resource;
    String state = "W"; // W: 等待, R: 运行, F: 完成
    int startTime;
    int finishTime;
    double turnaroundTime;
    double weightedTurnaround;

    JCB(String name, int submitTime, int runTime, String resource) {
        this.name = name;
        this.submitTime = submitTime;
        this.runTime = runTime;
        this.resource = resource;
    }

    public String toString() {
        return String.format("作业[%s] 提交:%d 运行:%d 状态:%s 开始:%d 完成:%d 周转:%.2f 带权周转:%.2f",
                name, submitTime, runTime, state, startTime, finishTime, turnaroundTime, weightedTurnaround);
    }
}

public class JobScheduler {
    public static void FCFS(List<JCB> jobs) {
        int currentTime = 0;
        double totalTurnaround = 0;
        double totalWeighted = 0;

        System.out.println("\n===== 先来先服务 FCFS =====");

        for (JCB job : jobs) {
            if (currentTime < job.submitTime) {
                currentTime = job.submitTime;
            }
            job.state = "R";
            job.startTime = currentTime;
            currentTime += job.runTime;
            job.finishTime = currentTime;
            job.state = "F";

            job.turnaroundTime = job.finishTime - job.submitTime;
            job.weightedTurnaround = job.turnaroundTime / job.runTime;

            totalTurnaround += job.turnaroundTime;
            totalWeighted += job.weightedTurnaround;

            System.out.println(job);
        }

        int n = jobs.size();
        System.out.printf("平均周转时间: %.2f\n", totalTurnaround / n);
        System.out.printf("带权平均周转时间: %.2f\n", totalWeighted / n);
    }

    public static void HRN(List<JCB> jobs) {
        int currentTime = 0;
        double totalTurnaround = 0;
        double totalWeighted = 0;

        List<JCB> readyQueue = new ArrayList<>(jobs);

        System.out.println("\n===== 响应比高者优先 HRN =====");

        while (!readyQueue.isEmpty()) {
            List<JCB> available = new ArrayList<>();
            for (JCB job : readyQueue) {
                if (job.submitTime <= currentTime) {
                    available.add(job);
                }
            }

            if (available.isEmpty()) {
                currentTime++;
                continue;
            }
            final int finaltime = currentTime;
            JCB next = Collections.max(available, Comparator.comparingDouble(
                    j -> (finaltime - j.submitTime + j.runTime) * 1.0 / j.runTime));

            next.state = "R";
            next.startTime = currentTime;
            currentTime += next.runTime;
            next.finishTime = currentTime;
            next.state = "F";

            next.turnaroundTime = next.finishTime - next.submitTime;
            next.weightedTurnaround = next.turnaroundTime / next.runTime;

            totalTurnaround += next.turnaroundTime;
            totalWeighted += next.weightedTurnaround;

            System.out.println(next);
            readyQueue.remove(next);
        }

        int n = jobs.size();
        System.out.printf("平均周转时间: %.2f\n", totalTurnaround / n);
        System.out.printf("带权平均周转时间: %.2f\n", totalWeighted / n);
    }

    public static void main(String[] args) {
        List<JCB> jobs1 = new ArrayList<>();
        jobs1.add(new JCB("A", 0, 4, "Printer"));
        jobs1.add(new JCB("B", 1, 3, "Disk"));
        jobs1.add(new JCB("C", 2, 5, "Tape"));
        jobs1.add(new JCB("D", 3, 2, "Printer"));
        FCFS(jobs1);

        List<JCB> jobs2 = new ArrayList<>();
        jobs2.add(new JCB("A", 0, 4, "Printer"));
        jobs2.add(new JCB("B", 1, 3, "Disk"));
        jobs2.add(new JCB("C", 2, 5, "Tape"));
        jobs2.add(new JCB("D", 3, 2, "Printer"));
        HRN(jobs2);
    }
}

import java.util.*;


public class Main {

    public static void main(String[] args) {
        Random random = new Random();
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", random.nextInt(10), random.nextInt(20), random.nextInt(5)));
        processes.add(new Process("P2", random.nextInt(10), random.nextInt(20), random.nextInt(5)));
        processes.add(new Process("P3", random.nextInt(10), random.nextInt(20), random.nextInt(5)));
        processes.add(new Process("P4", random.nextInt(10), random.nextInt(20), random.nextInt(5)));
        processes.add(new Process("P5", random.nextInt(10), random.nextInt(20), random.nextInt(5)));
        Process.FCFSScheduling(processes);

        Process.sjfScheduling(processes);

        Process.priorityScheduling(processes);

        Process.HRRN(processes);
    }
}

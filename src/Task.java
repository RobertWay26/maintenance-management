import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Task {
    private String name;
    private String description;
    private LocalDateTime nextMaintenance;
    private Duration timeInterval;

    public Task(String name, String description, Duration timeInterval) {
        this.name = name;
        this.description = description;
        this.timeInterval = timeInterval;

        // Calculate next maintenance time
        this.nextMaintenance = LocalDateTime.now().plus(timeInterval);
    }

    public static void saveTasks(List<Task> tasks, String filename) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Task task : tasks) {
                pw.println(task.getName());
                pw.println(task.getDescription());
                pw.println(task.getNextMaintenance());
                pw.println(task.getTimeInterval().toMinutes());
            }
        }
    }

    public static List<Task> loadTasks(String filename) throws IOException {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String name;
            while ((name = br.readLine()) != null) {
                String description = br.readLine();
                LocalDateTime nextMaintenance = LocalDateTime.parse(br.readLine());
                Duration timeInterval = Duration.ofMinutes(Long.parseLong(br.readLine()));

                Task task = new Task(name, description, timeInterval);
                task.setNextMaintenancenance(nextMaintenance); // Assumes the constructor doesn't set nextMaintenance based on the current time
                tasks.add(task);
            }
        }
        return tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getNextMaintenance() {
        return nextMaintenance;
    }

    public void setNextMaintenancenance(LocalDateTime nextMaintenance) {
        this.nextMaintenance = nextMaintenance;
    }

    public Duration getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Duration timeInterval) {
        this.timeInterval = timeInterval;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", nextMaintinance=" + nextMaintenance +
                ", timeInterval=" + timeInterval.toMinutes() + " minutes" +
                '}';
    }
}
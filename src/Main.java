import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            List<Task> tasks = Task.loadTasks("tasks.txt");
            displayTasksGraphically(tasks);
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }

    private static void displayTasksGraphically(List<Task> tasks) {
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI(tasks));
    }

    private static void createAndShowGUI(List<Task> tasks) {
        JFrame frame = new JFrame("Task List");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(600, 600);

        // Main panel with BoxLayout for overall organization
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Task panel for task items
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));

        // Scroll pane for the task panel
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        mainPanel.add(scrollPane);

        // Panel for adding new tasks
        JPanel addTaskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField nameField = new JTextField(10);
        JTextField descriptionField = new JTextField(20);
        JTextField timeIntervalField = new JTextField(5); // Time in minutes
        JButton addButton = new JButton("Add Task");

        addTaskPanel.add(new JLabel("Name:"));
        addTaskPanel.add(nameField);
        addTaskPanel.add(new JLabel("Description:"));
        addTaskPanel.add(descriptionField);
        addTaskPanel.add(new JLabel("Time Interval (min):"));
        addTaskPanel.add(timeIntervalField);
        addTaskPanel.add(addButton);

        mainPanel.add(addTaskPanel);

        // Add Task button action
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String description = descriptionField.getText();
            long timeInterval;
            try {
                timeInterval = Long.parseLong(timeIntervalField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid time interval");
                return;
            }

            Task newTask = new Task(name, description, Duration.ofMinutes(timeInterval));
            tasks.add(newTask);
            addTaskToPanel(taskPanel, newTask, tasks);
            nameField.setText("");
            descriptionField.setText("");
            timeIntervalField.setText("");
        });

        // Initialize the list with existing tasks
        for (Task task : tasks) {
            addTaskToPanel(taskPanel, task, tasks);
        }

        frame.add(mainPanel);
        frame.pack();

// Save on close
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    Task.saveTasks(tasks, "tasks.txt");
                    System.out.println("Tasks saved successfully.");
                } catch (IOException e) {
                    System.err.println("Error saving tasks: " + e.getMessage());
                }
                System.exit(0);
            }
        });

// Display the window
        frame.setVisible(true);
    }

    private static void addTaskToPanel(JPanel taskPanel, Task task, List<Task> tasks) {
        JPanel taskItemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        taskItemPanel.add(new JLabel(task.toString()));
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            tasks.remove(task);
            taskPanel.remove(taskItemPanel);
            taskPanel.revalidate();
            taskPanel.repaint();
        });

        taskItemPanel.add(deleteButton);
        taskPanel.add(taskItemPanel);
        taskPanel.revalidate();
        taskPanel.repaint();
    }
}
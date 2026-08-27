package woofer.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the storage and manipulation of tasks in a task list.
 */
public class TaskListTest {
    /**
     * Verifies that tasks are added and retrieved in insertion order.
     */
    @Test
    public void addTaskStoresTasksInInsertionOrder() {
        TaskList taskList = new TaskList();
        Task firstTask = new Todo("first task");
        Task secondTask = new Todo("second task");

        assertTrue(taskList.addTask(firstTask));
        assertTrue(taskList.addTask(secondTask));

        assertEquals(2, taskList.size());
        assertEquals(firstTask, taskList.getTask(1));
        assertEquals(secondTask, taskList.getTask(2));
        assertEquals(List.of(firstTask, secondTask), taskList.getTasks());
    }

    /**
     * Verifies that deleting a task removes it and shifts later tasks forward.
     */
    @Test
    public void deleteTaskRemovesTaskAndShiftsRemainingTasks() {
        TaskList taskList = new TaskList();
        Task firstTask = new Todo("first task");
        Task secondTask = new Todo("second task");
        taskList.addTask(firstTask);
        taskList.addTask(secondTask);

        assertEquals(firstTask, taskList.deleteTask(1));
        assertEquals(1, taskList.size());
        assertEquals(secondTask, taskList.getTask(1));
        assertNull(taskList.deleteTask(2));
    }

    /**
     * Verifies that task numbers outside the list return no task.
     */
    @Test
    public void getTaskReturnsNullForInvalidTaskNumbers() {
        TaskList taskList = new TaskList();
        taskList.addTask("read book");

        assertNull(taskList.getTask(0));
        assertNull(taskList.getTask(2));
        assertNull(taskList.getTask(-1));
    }

    /**
     * Verifies that the task list rejects tasks beyond its capacity.
     */
    @Test
    public void addTaskRejectsTasksBeyondCapacity() {
        TaskList taskList = new TaskList();

        for (int index = 0; index < 100; index++) {
            assertTrue(taskList.addTask("task " + index));
        }

        assertFalse(taskList.addTask("one task too many"));
        assertEquals(100, taskList.size());
    }
}

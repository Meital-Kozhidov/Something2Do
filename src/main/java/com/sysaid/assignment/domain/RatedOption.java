package com.sysaid.assignment.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/****************************************************************************/

public class RatedOption {

    private static final Random random = new Random();
    private final List<Task> tasks;

    public RatedOption(List<Task> tasks) {
      this.tasks = tasks;
    }

    public List<Task> getRatedTasks(Integer amount) throws Exception {
      List<Task> ratedTasks = new ArrayList<Task>(); 
      for (int i = 0; i < amount; ++i) {
        ratedTasks.add(getRatedTask());
      }

      return ratedTasks;
    }

    private Task getRatedTask() throws Exception {
        this.tasks.sort(Comparator.comparingInt(Task::getRating).reversed());

        int totalProbability = 100;

        int[] probabilityRanges = new int[tasks.size() + 1];
        probabilityRanges[0] = 0;
        probabilityRanges[1] = 20; // 20%
        probabilityRanges[2] = probabilityRanges[1] + 20; // 20%
        probabilityRanges[3] = probabilityRanges[2] + 10; // 10%
        probabilityRanges[4] = probabilityRanges[3] + 5; // 5%
        probabilityRanges[5] = probabilityRanges[4] + 5; // 5% 
        probabilityRanges[6] = 100; // 40% for random task

        int randomNumber = random.nextInt(totalProbability);

        return tasks.stream()
            .filter(task -> randomNumber >= probabilityRanges[tasks.indexOf(task)] && 
            randomNumber < probabilityRanges[tasks.indexOf(task) + 1])
            .findFirst()
            .orElseThrow(() -> new Exception());

       //TODO throw exception for null
    }
}
import model.Epic;
import model.SubTask;
import model.Task;
import service.InMemoryTaskManager;
import service.TaskManager;
import status.TaskStatus;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();


        ///  ПРОВЕРКА ПО ТАСКАМ

        ///  проверяем удаление по айди
        System.out.println("проверяем удаление по айди");

        Integer taks1 = manager.createTask(new Task("Task1", "Desc1"));

        System.out.println(manager.getTasks());
        manager.deleteTaskById(taks1);
        System.out.println("Результат " + manager.getTasks());
        System.out.println();


        ///  проверяем удаление всего списка
        System.out.println("проверяем удаление всего списка");

        Integer taks2 = manager.createTask(new Task("Task2", "Desc2"));
        Integer taks3 = manager.createTask(new Task("Task3", "Desc3"));

        System.out.println(manager.getTasks());
        manager.removeAllTasks();
        System.out.println("Результат " + manager.getTasks());
        System.out.println();

        /// получаем таск по айди
        System.out.println("получаем таск по айди");

        Integer taks4 = manager.createTask(new Task("Task4", "Desc4"));
        Integer taks5 = manager.createTask(new Task("Task5", "Desc5"));

        System.out.println("Результат " + manager.getTaskById(taks4));
        System.out.println();


        ///  обновляем такс
        System.out.println("обновляем таск");
        System.out.println(manager.getTasks());

        Task taks6 = new Task("Task6", "Desc6", taks4, TaskStatus.DONE);

        manager.taskUpdate(taks6);

        System.out.println("Результат " + manager.getTasks());


        ///  ПРОВЕРКА ПО ТАСКАМ ЭПИКАМ
        System.out.println();
        System.out.println("ПРОВЕРКА ПО ЭПИКАМ");
        System.out.println();


        ///  проверяем удаление по айди
        System.out.println("проверяем удаление по айди");

        Integer epic1 = manager.createEpic(new Epic("Epic1", "Desc1"));

        System.out.println(manager.getEpics());
        manager.deleteEpicById(epic1);
        System.out.println("Результат " + manager.getEpics());
        System.out.println();


        ///  проверяем удаление всего списка
        System.out.println("проверяем удаление всего списка");

        Integer epic2 = manager.createEpic(new Epic("Epic2", "Desc2"));
        Integer epic3 = manager.createEpic(new Epic("Epic3", "Desc3"));

        System.out.println(manager.getEpics());
        manager.removeAllEpics();
        System.out.println("Результат " + manager.getEpics());
        System.out.println();

        /// получаем таск по айди
        System.out.println("получаем таск по айди");

        Integer epic4 = manager.createEpic(new Epic("Epic4", "Desc4"));
        Integer epic5 = manager.createEpic(new Epic("Epic5", "Desc5"));

        System.out.println("Результат " + manager.getEpicById(epic4));
        System.out.println();


        ///  обновляем такс
        System.out.println("обновляем епик");
        System.out.println(manager.getEpics());

        Epic epic6 = new Epic("Epic6", "Desc6", epic4, TaskStatus.DONE);

        manager.epicUpdate(epic6);

        System.out.println("Результат " + manager.getEpics());





        ///  ПРОВЕРКА ПО САБТАСКАМ

        System.out.println();
        System.out.println("ПРОВЕРКА ПО САБТАСКАМ");
        System.out.println();


        ///  проверяем удаление по айди
        System.out.println("проверяем удаление по айди");

        Integer subtaks1 = manager.createSubTask(new SubTask("subTask1", "Desc1" , epic4));

        System.out.println(manager.getSubTasks());
        manager.deleteSubTaskById(subtaks1);
        System.out.println("Результат " + manager.getSubTasks());
        System.out.println();


        ///  проверяем удаление всего списка
        System.out.println("проверяем удаление всего списка");

        Integer subtaks2 = manager.createSubTask(new SubTask("subTask2", "Desc2", epic5));
        Integer subtaks3 = manager.createSubTask(new SubTask("subTask3", "Desc3", epic4));

        System.out.println(manager.getSubTasks());
        manager.removeAllSubTasks();
        System.out.println("Результат " + manager.getSubTasks());
        System.out.println();

        /// получаем таск по айди
        System.out.println("получаем таск по айди");

        Integer subtaks4 = manager.createSubTask(new SubTask("subTask4", "Desc4", epic4));
        Integer subtaks5 = manager.createSubTask(new SubTask("subTask5", "Desc5", epic5));
        Integer subtaks7 = manager.createSubTask(new SubTask("subTask4", "Desc4", epic4));

        System.out.println("Результат " + manager.getSubTaskById(subtaks5));
        System.out.println("Результат " + manager.getSubTaskById(subtaks4));
        System.out.println();


        ///  обновляем такс
        System.out.println("обновляем таск");
        System.out.println("Результат " + manager.getSubTasks());

        SubTask subtaks6 = new SubTask("subTask6", "Desc6", 16, TaskStatus.DONE, 9);

        manager.subTaskUpdate(subtaks6);

        System.out.println("Результат " + manager.getSubTasks());

        System.out.println("Результат " + manager.getEpics());


        System.out.println();
        System.out.println("Получаем все сабтаски из одного эпика");



        System.out.println("Результат " + manager.getSubTasksByEpicId(10));


        ///  проверка на статус
        System.out.println();
        System.out.println("ПРОВЕРКА НА СТАТУСЫ");


        Integer epicStatus1 = manager.createEpic(new Epic("EpicStatus", "DescStatus"));
        Integer subtaskStatus1 = manager.createSubTask(new SubTask("subTaskStatus1", "DescStatus", epicStatus1));
        Integer subtaskStatus2 = manager.createSubTask(new SubTask("subTaskStatus2", "DescStatus", epicStatus1));
        Integer subtaskStatus3 = manager.createSubTask(new SubTask("subTaskStatus3", "DescStatus", epicStatus1));
        Integer subtaskStatus4 = manager.createSubTask(new SubTask("subTaskStatus4", "DescStatus", epicStatus1));

        System.out.println(manager.getSubTasksByEpicId(epicStatus1));
        System.out.println(manager.getEpicById(epicStatus1));

        System.out.println();
        System.out.println("получаем статус эпика Done");

        SubTask subtaskStatus11 = new SubTask("subTask66", "Desc66", 18, TaskStatus.DONE, 17);
        SubTask subtaskStatus22 = new SubTask("subTask77", "Desc6", 19, TaskStatus.DONE, 17);
        SubTask subtaskStatus33 = new SubTask("subTask88", "Desc6", 20, TaskStatus.DONE, 17);
        SubTask subtaskStatus44 = new SubTask("subTask99", "Desc6", 21, TaskStatus.DONE, 17);

        manager.subTaskUpdate(subtaskStatus11);
        manager.subTaskUpdate(subtaskStatus22);
        manager.subTaskUpdate(subtaskStatus33);
        manager.subTaskUpdate(subtaskStatus44);

        System.out.println(manager.getEpicById(epicStatus1));

        printAllTasks(manager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubTasksByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}

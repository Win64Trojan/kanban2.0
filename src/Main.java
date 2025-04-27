import model.Epic;
import model.SubTask;
import model.Task;
import service.TaskManager;
import status.TaskStatus;

public class Main {

    public static void main(String[] args) {
        TaskManager tm = new TaskManager();


        ///  ПРОВЕРКА ПО ТАСКАМ

        ///  проверяем удаление по айди
        System.out.println("проверяем удаление по айди");

        Integer taks1 = tm.createTask(new Task("Task1", "Desc1"));

        System.out.println(tm.getTasks());
        tm.deleteTaskById(taks1);
        System.out.println("Результат " + tm.getTasks());
        System.out.println();


        ///  проверяем удаление всего списка
        System.out.println("проверяем удаление всего списка");

        Integer taks2 = tm.createTask(new Task("Task2", "Desc2"));
        Integer taks3 = tm.createTask(new Task("Task3", "Desc3"));

        System.out.println(tm.getTasks());
        tm.removeAllTasks();
        System.out.println("Результат " + tm.getTasks());
        System.out.println();

        /// получаем таск по айди
        System.out.println("получаем таск по айди");

        Integer taks4 = tm.createTask(new Task("Task4", "Desc4"));
        Integer taks5 = tm.createTask(new Task("Task5", "Desc5"));

        System.out.println("Результат " + tm.getTaskById(taks4));
        System.out.println();


        ///  обновляем такс
        System.out.println("обновляем таск");
        System.out.println(tm.getTasks());

        Task taks6 = new Task("Task6", "Desc6", taks4, TaskStatus.DONE);

        tm.taskUpdate(taks6);

        System.out.println("Результат " + tm.getTasks());


        ///  ПРОВЕРКА ПО ТАСКАМ ЭПИКАМ
        System.out.println();
        System.out.println("ПРОВЕРКА ПО ЭПИКАМ");
        System.out.println();


        ///  проверяем удаление по айди
        System.out.println("проверяем удаление по айди");

        Integer epic1 = tm.createEpic(new Epic("Epic1", "Desc1"));

        System.out.println(tm.getEpics());
        tm.deleteEpicById(epic1);
        System.out.println("Результат " + tm.getEpics());
        System.out.println();


        ///  проверяем удаление всего списка
        System.out.println("проверяем удаление всего списка");

        Integer epic2 = tm.createEpic(new Epic("Epic2", "Desc2"));
        Integer epic3 = tm.createEpic(new Epic("Epic3", "Desc3"));

        System.out.println(tm.getEpics());
        tm.removeAllEpics();
        System.out.println("Результат " + tm.getEpics());
        System.out.println();

        /// получаем таск по айди
        System.out.println("получаем таск по айди");

        Integer epic4 = tm.createEpic(new Epic("Epic4", "Desc4"));
        Integer epic5 = tm.createEpic(new Epic("Epic5", "Desc5"));

        System.out.println("Результат " + tm.getEpicById(epic4));
        System.out.println();


        ///  обновляем такс
        System.out.println("обновляем епик");
        System.out.println(tm.getEpics());

        Epic epic6 = new Epic("Epic6", "Desc6", epic4, TaskStatus.DONE);

        tm.epicUpdate(epic6);

        System.out.println("Результат " + tm.getEpics());





        ///  ПРОВЕРКА ПО САБТАСКАМ

        System.out.println();
        System.out.println("ПРОВЕРКА ПО САБТАСКАМ");
        System.out.println();


        ///  проверяем удаление по айди
        System.out.println("проверяем удаление по айди");

        Integer subtaks1 = tm.createSubTask(new SubTask("subTask1", "Desc1" , epic4));

        System.out.println(tm.getSubTasks());
        tm.deleteSubTaskById(subtaks1);
        System.out.println("Результат " + tm.getSubTasks());
        System.out.println();


        ///  проверяем удаление всего списка
        System.out.println("проверяем удаление всего списка");

        Integer subtaks2 = tm.createSubTask(new SubTask("subTask2", "Desc2", epic5));
        Integer subtaks3 = tm.createSubTask(new SubTask("subTask3", "Desc3", epic4));

        System.out.println(tm.getSubTasks());
        tm.removeAllSubTasks();
        System.out.println("Результат " + tm.getSubTasks());
        System.out.println();

        /// получаем таск по айди
        System.out.println("получаем таск по айди");

        Integer subtaks4 = tm.createSubTask(new SubTask("subTask4", "Desc4", epic4));
        Integer subtaks5 = tm.createSubTask(new SubTask("subTask5", "Desc5", epic5));
        Integer subtaks7 = tm.createSubTask(new SubTask("subTask4", "Desc4", epic4));

        System.out.println("Результат " + tm.getSubTaskById(subtaks5));
        System.out.println("Результат " + tm.getSubTaskById(subtaks4));
        System.out.println();


        ///  обновляем такс
        System.out.println("обновляем таск");
        System.out.println("Результат " + tm.getSubTasks());

        SubTask subtaks6 = new SubTask("subTask6", "Desc6", 14, TaskStatus.DONE, 100);

        tm.subTaskUpdate(subtaks6);

        System.out.println("Результат " + tm.getSubTasks());

        System.out.println("Результат " + tm.getEpics());


        System.out.println();
        System.out.println("Получаем все сабтаски из одного эпика");



        System.out.println("Результат " + tm.getSubTasksByEpicId(10));
    }
}

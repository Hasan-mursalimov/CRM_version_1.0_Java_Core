package repositories;


import models.Task;

public interface TaskRepository extends CrudRepository<Task>{

    void taskChange(Long idTask, int whatToChange, String newMeaning);

}

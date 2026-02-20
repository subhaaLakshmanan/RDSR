package com.example.dsr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.dsr.model.SubTask;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask,Long> {

	@Query(value = "select sum(estimated_hours) from sub_task where taskid =:taskid	and isdelete = 0 and id <> :id",nativeQuery = true)
	int getTotalSubTaskEstimationTimeByTaskId(Long taskid,Long id);

	@Query(value = "select count(id) from sub_task where taskid =:taskid",nativeQuery = true)
	int getCountByTaskId(Long taskid);

	SubTask getByCode(String code);

	@Query(value = "select * from sub_task where taskid =:taskid and isdelete = 0 order by id desc",nativeQuery = true)
	List<SubTask> getSubTaskListbyTask(Long taskid);

//	@Query(value = "update sub_task set completion_percentage =:completion_percentage where id =:subtaskid",nativeQuery = true)
	
	@Query(value = "UPDATE sub_task SET completion_percentage = :completion_percentage,worked_hours = (SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(worked_hours))) FROM dsr WHERE subtaskid = :subtaskid)WHERE id = :subtaskid",nativeQuery = true)
	void updateCompletionPercentageForSubTask(Long subtaskid, int completion_percentage);

	@Query(value = "update sub_task set isdelete = 1,updated_date = now() where taskid =:task_id",nativeQuery = true)
	void updateIsDeleteForSubtask(Long task_id);



}

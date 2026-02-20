package com.example.dsr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dsr.model.Comments;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long>{

	@Query(value = "select max(id) from comments",nativeQuery = true)
	int getMaxId();

	@Query(value = "select * from comments where if(:type = 'task',taskid =:id,if(:type = 'sub_task',sub_taskid =:id,if(:type = 'phase',phaseid =:id,if(:type = 'release',releaseid =:id,id != 0)))) and isdelete = 0 order by id desc",nativeQuery = true)
	List<Comments> getCommentsByIdAndType(String type, Long id);

}

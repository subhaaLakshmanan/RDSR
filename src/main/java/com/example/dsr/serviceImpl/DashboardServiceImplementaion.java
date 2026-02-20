package com.example.dsr.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.dsr.DTO.ReleaseDTO;
import com.example.dsr.model.Department;
import com.example.dsr.model.Phase;
import com.example.dsr.model.Task;
import com.example.dsr.repository.DepartmentRepository;
import com.example.dsr.repository.EmployeeRepository;
import com.example.dsr.repository.PhaseRepository;
import com.example.dsr.repository.ProjectRepository;
import com.example.dsr.repository.ReleaseDetailsRepository;
import com.example.dsr.repository.TaskRepository;
import com.example.dsr.service.DashboardService;

@Repository
public class DashboardServiceImplementaion implements DashboardService{
	
	@Autowired
	ProjectRepository projectRepo;
	
	@Autowired
	TaskRepository taskRepo;
	
	@Autowired
	PhaseRepository phaseRepo;
	
    @Autowired
	EmployeeRepository employeeRepo;
    
    @Autowired
    DepartmentRepository departmentRepo;
    
    @Autowired
    ReleaseDetailsRepository releaseDetailsRepo;
    
	@Override
	public Map getDashBoardDetails(Long employee_id,Long project_id) {
		Map map = new HashMap<>();
		try {
			/** To collect active and completed task count by employee role and project  **/
            Map <String,String> task = taskRepo.getTaskCountForDashboardByRole(employee_id,project_id);
			
            /** To collect active,completed and droped project count by employee role  **/
			Map <String,String> project = projectRepo.getProjectCountForDashboardByRole(employee_id);
			
			/** Collects active and completed phase counts based on employee role and project */
            Map <String,String> phase = phaseRepo.getPhaseCountForDashboardByRole(employee_id,project_id);
			
            /** Collects active and deactive employee counts based on employee role */
			Map <String,String> employee = employeeRepo.getEmployeeCountForDashboardByRole();
			
			Map <String,String> release = releaseDetailsRepo.getReleaseCountForDashboardByRole(employee_id);	
			
			map.put("active_task", task.get("active_task"));
			map.put("closed_task", task.get("closed_task"));
			map.put("active_issue", task.get("active_issue"));
			map.put("closed_issue", task.get("closed_issue"));
			
			map.put("active_project", project.get("active_project"));
			map.put("completed_project", project.get("completed_project"));
			map.put("dropped_projects", project.get("dropped_projects"));
			
			map.put("active_phase", phase.get("active_phase"));
			map.put("completed_phase", phase.get("completed_phase"));
			
			map.put("active_employees", employee.get("active_employees"));
			map.put("deactive_employees", employee.get("deactive_employees"));
			
			map.put("total_release", release.get("total_release"));
			map.put("passed_release", release.get("passed_release"));
			map.put("failed_release", release.get("failed_release"));
			
			List<Map> departmentwise_project_details = new ArrayList<>();
			map.put("departmentwise_project_details", departmentwise_project_details);
			
			List<Department> department_list = departmentRepo.findAll();
			
			Map m = new HashMap<>();
			Map<String, String> object = projectRepo.getProjectCountForPublicProjects();
			
			m.put("departmen_name", "Public Project");
			m.put("active_project", object.get("active_project"));
			m.put("completed_project", object.get("completed_project"));
			m.put("dropped_projects", object.get("dropped_projects"));
			departmentwise_project_details.add(m);
			
			department_list.stream()
		    .filter(department -> !department.isIsdelete())
		    .forEach(department -> {
		    	Map m1 = new HashMap<>();
		    	
		        Map<String, String> object1 = projectRepo.getProjectCountByDepartment(department.getId());
		        
		        m1.put("departmen_name", department.getDepartment_name());
		        m1.put("active_project", object1.get("active_project"));
		        m1.put("completed_project", object1.get("completed_project"));
		        m1.put("dropped_projects", object1.get("dropped_projects"));
				departmentwise_project_details.add(m1);
		    });
			
			List<Map<String, String>> task_list = taskRepo.getTaskListForDashboard(employee_id,"Requirement",project_id,"all");
			List<Map<String, String>> bug_list = taskRepo.getTaskListForDashboard(employee_id,"Bug",project_id,"all");
			
			map.put("task_list", task_list);
			map.put("bug_list", bug_list);
			
			List<Map<String, String>> open_task_list = taskRepo.getTaskListForDashboard(employee_id,"Requirement",project_id,"open");
			List<Map<String, String>> open_bug_list = taskRepo.getTaskListForDashboard(employee_id,"Bug",project_id,"open");
			
			map.put("open_task_list", open_task_list);
			map.put("open_bug_list", open_bug_list);
			
			List<Map<String, String>> closed_task_list = taskRepo.getTaskListForDashboard(employee_id,"Requirement",project_id,"close");
			List<Map<String, String>> closed_bug_list = taskRepo.getTaskListForDashboard(employee_id,"Bug",project_id,"close");
			
			map.put("closed_task_list", closed_task_list);
			map.put("closed_bug_list", closed_bug_list);
			
			List<Phase> phase_list = phaseRepo.getPhaseListForDashboard(employee_id,project_id);
			map.put("phase_list", phase_list);
			
			List<Map<String, String>> overdue_items = new ArrayList<>();
			
			List<Map<String, String>> overdue_tasks = taskRepo.getOverDueTaskListForDashboard(employee_id,project_id);
//			List<Map<String, String>> overdue_phase = phaseRepo.getOverDuePhaseListForDashboard(employee_id);
			
			overdue_items.addAll(overdue_tasks);
//			overdue_items.addAll(overdue_phase);
			
			map.put("overdue_items", overdue_items);
			
			List<Map<String, String>> today_due = new ArrayList<>();
			
			List<Map<String, String>> today_due_tasks = taskRepo.getTodayDueTaskListForDashboard(employee_id,project_id);
//			List<Map<String, String>> today_due_phase = phaseRepo.getTodayDuePhaseListForDashboard(employee_id);
			
			today_due.addAll(today_due_tasks);
//			today_due.addAll(today_due_phase);
			
			map.put("today_due", today_due);
			
			List<ReleaseDTO> upcomming_list = releaseDetailsRepo.getReleaseDetailsForDashboardByType(employee_id,"upcomming");
			List<ReleaseDTO> released_list = releaseDetailsRepo.getReleaseDetailsForDashboardByType(employee_id,"released");
			
			map.put("upcomming_release", upcomming_list);
			map.put("completed_release", released_list);
			
			List<Map<String, String>> total_release_for_this_month = releaseDetailsRepo.totalReleaseForThisMonth(employee_id);
			List<Map<String, String>> passed_release_for_this_month = releaseDetailsRepo.passedReleaseForThisMonth(employee_id);
			
			map.put("total_release_list", total_release_for_this_month);
			map.put("passed_release_list", passed_release_for_this_month);
		}catch(Exception e) {
			System.out.println("Error in getDashBoardDetails : "+e.getMessage());
		}
		return map;
	}
	
	

}

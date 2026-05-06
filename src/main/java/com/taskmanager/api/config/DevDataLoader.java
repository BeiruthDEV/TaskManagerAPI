package com.taskmanager.api.config;

import com.taskmanager.api.task.Task;
import com.taskmanager.api.task.TaskPriority;
import com.taskmanager.api.task.TaskRepository;
import com.taskmanager.api.task.TaskStatus;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevDataLoader {

    @Bean
    CommandLineRunner loadDemoTasks(TaskRepository taskRepository) {
        return args -> {
            if (taskRepository.count() > 0) {
                return;
            }

            taskRepository.save(new Task("Migrate server to new infrastructure", "Move legacy services to the new cluster",
                    "Michael Ardi", "DODO System Upgrade", 0, TaskStatus.PENDENTE, TaskPriority.MEDIA, LocalDate.of(2026, 6, 10)));
            taskRepository.save(new Task("Set up new authentication system", "Prepare login policies and token rotation",
                    "Lisa Kim", "IT Compliance Review", 0, TaskStatus.PENDENTE, TaskPriority.ALTA, LocalDate.of(2026, 6, 2)));
            taskRepository.save(new Task("Test API integration with updated modules", "Validate API contracts after module updates",
                    "Lisa Kim", "Email Marketing Launch", 0, TaskStatus.PENDENTE, TaskPriority.BAIXA, LocalDate.of(2026, 5, 31)));

            taskRepository.save(new Task("Distribute participation surveys", "Send pulse survey to all departments",
                    "Timmy Tom", "Employee Wellness Week", 80, TaskStatus.EM_PROGRESSO, TaskPriority.MEDIA, LocalDate.of(2026, 5, 25)));
            taskRepository.save(new Task("Schedule yoga and meditation sessions", "Confirm instructors and participant capacity",
                    "Nina Ross", "Go Healthy!", 55, TaskStatus.EM_PROGRESSO, TaskPriority.ALTA, LocalDate.of(2026, 5, 24)));
            taskRepository.save(new Task("Coordinate with external speakers", "Align topics, bios and event sequence",
                    "Leo Garcia", "Safe Work, Safe People", 76, TaskStatus.EM_PROGRESSO, TaskPriority.BAIXA, LocalDate.of(2026, 5, 30)));

            taskRepository.save(new Task("Draft onboarding checklist for new hires", "Prepare first-week checklist and owner map",
                    "Amira William", "Employee Onboarding", 90, TaskStatus.EM_REVISAO, TaskPriority.MEDIA, LocalDate.of(2026, 5, 26)));
            taskRepository.save(new Task("Gather system requirements from Marketing", "Collect campaign workflow requirements",
                    "Lala Wink", "Social Media Rocket", 86, TaskStatus.EM_REVISAO, TaskPriority.ALTA, LocalDate.of(2026, 5, 28)));
            taskRepository.save(new Task("Design wellness week poster", "Create internal announcement poster",
                    "Jesslyn Tan", "Wilto System Revamp", 89, TaskStatus.EM_REVISAO, TaskPriority.MEDIA, LocalDate.of(2026, 5, 22)));

            taskRepository.save(new Task("Publish payroll export checklist", "Finalize monthly payroll export validation",
                    "Peter Gabrielle", "Payroll Automation", 100, TaskStatus.CONCLUIDO, TaskPriority.MEDIA, LocalDate.of(2026, 5, 15)));
            taskRepository.save(new Task("Archive completed recruitment reports", "Move closed recruitment reports to archive",
                    "Nina Ross", "Recruitment & Hiring", 100, TaskStatus.CONCLUIDO, TaskPriority.BAIXA, LocalDate.of(2026, 5, 12)));
        };
    }
}

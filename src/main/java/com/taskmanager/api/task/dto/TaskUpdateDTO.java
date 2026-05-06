package com.taskmanager.api.task.dto;

import com.taskmanager.api.task.TaskPriority;
import com.taskmanager.api.task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Schema(description = "Payload para atualizacao parcial ou completa de tarefa")
public record TaskUpdateDTO(
        @Pattern(regexp = ".*\\S.*", message = "O titulo nao pode ser vazio")
        @Size(max = 255, message = "O titulo deve ter no maximo 255 caracteres")
        @Schema(example = "Finalizar API ToDo")
        String title,

        @Size(max = 2000, message = "A descricao deve ter no maximo 2000 caracteres")
        @Schema(example = "Revisar tratamento de erros e OpenAPI")
        String description,

        @Size(max = 120, message = "O responsavel deve ter no maximo 120 caracteres")
        @Schema(example = "Lisa Kim")
        String assignee,

        @Size(max = 160, message = "O projeto deve ter no maximo 160 caracteres")
        @Schema(example = "IT Compliance Review")
        String projectName,

        @Min(value = 0, message = "O progresso minimo e 0")
        @Max(value = 100, message = "O progresso maximo e 100")
        @Schema(example = "75")
        Integer progress,

        @Schema(example = "EM_PROGRESSO", allowableValues = {"PENDENTE", "EM_PROGRESSO", "EM_REVISAO", "CONCLUIDO"})
        TaskStatus status,

        @Schema(example = "MEDIA", allowableValues = {"BAIXA", "MEDIA", "ALTA"})
        TaskPriority priority,

        @Schema(example = "2026-06-30")
        LocalDate dueDate
) {
}

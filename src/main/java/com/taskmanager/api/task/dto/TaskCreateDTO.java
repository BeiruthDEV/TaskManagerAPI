package com.taskmanager.api.task.dto;

import com.taskmanager.api.task.TaskPriority;
import com.taskmanager.api.task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Schema(description = "Payload para criacao de tarefa")
public record TaskCreateDTO(
        @NotBlank(message = "O titulo e obrigatorio")
        @Size(max = 255, message = "O titulo deve ter no maximo 255 caracteres")
        @Schema(example = "Finalizar API ToDo")
        String title,

        @Size(max = 2000, message = "A descricao deve ter no maximo 2000 caracteres")
        @Schema(example = "Implementar endpoints REST, validacoes e testes")
        String description,

        @Size(max = 120, message = "O responsavel deve ter no maximo 120 caracteres")
        @Schema(example = "Michael Ardi")
        String assignee,

        @Size(max = 160, message = "O projeto deve ter no maximo 160 caracteres")
        @Schema(example = "DODO System Upgrade")
        String projectName,

        @Min(value = 0, message = "O progresso minimo e 0")
        @Max(value = 100, message = "O progresso maximo e 100")
        @Schema(example = "40")
        Integer progress,

        @Schema(example = "PENDENTE", allowableValues = {"PENDENTE", "EM_PROGRESSO", "EM_REVISAO", "CONCLUIDO"})
        TaskStatus status,

        @Schema(example = "ALTA", allowableValues = {"BAIXA", "MEDIA", "ALTA"})
        TaskPriority priority,

        @Schema(example = "2026-06-30")
        LocalDate dueDate
) {
}

package com.taskmanager.api.presentation.task.dto;

import com.taskmanager.api.application.pagination.PageResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Pagina de tarefas")
public record TaskPageResponseDTO(
        List<TaskResponseDTO> content,
        int number,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static TaskPageResponseDTO fromDomain(PageResult<TaskResponseDTO> result) {
        return new TaskPageResponseDTO(
                result.content(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages(),
                result.first(),
                result.last()
        );
    }
}

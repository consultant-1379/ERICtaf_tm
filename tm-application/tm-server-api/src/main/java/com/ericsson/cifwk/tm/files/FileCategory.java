package com.ericsson.cifwk.tm.files;

import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.ws.rs.BadRequestException;
import java.util.Arrays;

public enum FileCategory {

    TEST_CASE_FILE("test-cases"),
    TEST_EXECUTION_FILE("test-executions");

    private String category;

    FileCategory(String category) {
        this.category = category;
    }

    public static FileCategory get(String category) {
        return Arrays.stream(FileCategory.values())
                .filter(c -> category.equals(c.getCategory()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(Responses.badRequest("File category not found")));
    }

    public String getCategory() {
        return category;
    }
}

package com.natwest.kata.submersible.api.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collections;
import java.util.List;

@Schema(description = "Standard API error response format.")
public class ErrorResponse {

    @Schema(description = "Wrapper around error metadata.")
    private ErrorBody error;

    public ErrorResponse() {
    }

    public ErrorResponse(ErrorBody error) {
        this.error = error;
    }

    // ✅ Convenience factory methods for validation errors
    public static ErrorResponse validation(String msg) {
        return validation(msg, Collections.emptyList());
    }

    public static ErrorResponse validation(String msg, List<ErrorDetail> details) {
        return new ErrorResponse(new ErrorBody("VALIDATION_ERROR", msg, details, null));
    }

    public ErrorBody getError() {
        return error;
    }

    public void setError(ErrorBody error) {
        this.error = error;
    }

    // --- Nested Classes ---

    @Schema(description = "Detailed error metadata container.")
    public static class ErrorBody {

        @Schema(description = "Error code category.", example = "VALIDATION_ERROR")
        private String code;

        @Schema(description = "Human readable error message.", example = "Payload validation failed")
        private String message;

        @Schema(description = "List of field-level validation issues.")
        private List<ErrorDetail> details;

        @Schema(description = "Trace ID useful for log correlation.", example = "abc123", nullable = true)
        private String traceId;

        public ErrorBody() {
        }

        public ErrorBody(String code, String message, List<ErrorDetail> details, String traceId) {
            this.code = code;
            this.message = message;
            this.details = details;
            this.traceId = traceId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<ErrorDetail> getDetails() {
            return details;
        }

        public void setDetails(List<ErrorDetail> details) {
            this.details = details;
        }

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }
    }

    @Schema(description = "Represents a single validation or domain error for a specific field.")
    public static class ErrorDetail {

        @Schema(description = "Field name that caused the error.", example = "grid")
        private String field;

        @Schema(description = "Description of the validation issue.", example = "must not be null")
        private String issue;

        public ErrorDetail() {
        }

        public ErrorDetail(String field, String issue) {
            this.field = field;
            this.issue = issue;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getIssue() {
            return issue;
        }

        public void setIssue(String issue) {
            this.issue = issue;
        }
    }
}

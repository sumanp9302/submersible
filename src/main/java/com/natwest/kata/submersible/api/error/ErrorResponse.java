package com.natwest.kata.submersible.api.error;

import java.util.Collections;
import java.util.List;

public class ErrorResponse {

    private ErrorBody error;

    public ErrorResponse() {
    }

    public ErrorResponse(ErrorBody error) {
        this.error = error;
    }

    /**
     * Convenience overload: validation error with no details.
     */
    public static ErrorResponse validation(String msg) {
        return validation(msg, Collections.emptyList());
    }

    /**
     * Full validation error with details.
     */
    public static ErrorResponse validation(String msg, List<ErrorDetail> details) {
        return new ErrorResponse(new ErrorBody("VALIDATION_ERROR", msg, details, null));
    }

    public ErrorBody getError() {
        return error;
    }

    public void setError(ErrorBody error) {
        this.error = error;
    }

    public static class ErrorDetail {
        private String field;
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

    public static class ErrorBody {
        private String code;
        private String message;
        private List<ErrorDetail> details;
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
}

package com.poc.backend.utility;

import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r4.model.OperationOutcome.IssueType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.poc.backend.exception.BadRequestException;
import com.poc.backend.exception.BusinessException;
import com.poc.backend.exception.DatabaseException;
import com.poc.backend.exception.FHIRClientException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final FhirContext context = FhirContext.forR4();

    // Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException exception){
        return buildFHIRResponse(exception, IssueType.INVALID, HttpStatus.BAD_REQUEST);
    }

    // Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException exception){
        return buildFHIRResponse(exception, IssueType.NOTFOUND, HttpStatus.NOT_FOUND);
    }

    // FHIR error
    @ExceptionHandler(FHIRClientException.class)
    public ResponseEntity<String> handleFhirClient(FHIRClientException exception){
        return buildFHIRResponse(exception, IssueType.EXCEPTION, HttpStatus.BAD_GATEWAY);
    }

    // DB error
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<String> handleDB(DatabaseException exception){
        return buildFHIRResponse(exception, IssueType.EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Business
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusiness(BusinessException exception){
        return buildFHIRResponse(exception, IssueType.PROCESSING, HttpStatus.CONFLICT);
    }

    // Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception exception){
        return buildFHIRResponse(exception, IssueType.UNKNOWN, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // Build FHIR Response
    private ResponseEntity<String> buildFHIRResponse(Exception exception, IssueType type, HttpStatus status){
        OperationOutcome outcome = new OperationOutcome();

        outcome.addIssue()
                .setSeverity(IssueSeverity.ERROR)
                .setCode(type)
                .setDiagnostics(exception.getMessage());
        String body = context
                        .newJsonParser()
                        .setPrettyPrint(true)
                        .encodeResourceToString(outcome);
        return ResponseEntity.status(status).body(body);
    }

}

package com.scrumpoker.response;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scrumpoker.utils.Translator;

import lombok.Getter;

@Getter
public class ErrorResponse {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;
	
	private List<String> errors;
	
	public ErrorResponse setErrors(final BindingResult bindingResult) {
		this.errors = bindingResult.getAllErrors()
				.stream()
				.map(ObjectError::getDefaultMessage)
				.collect(Collectors.toList());
		return this;
	}
	
	public ErrorResponse setErrors(final String errorKey) {
		this.errors = Arrays.asList(Translator.toLocale(errorKey));
		return this;
	}
	
	public ErrorResponse setErrors(final List<String> error) {
		this.errors = error;
		return this;
	}
	
	
	public ErrorResponse build() {
		this.timestamp = timestamp == null ? LocalDateTime.now() : timestamp;
		return this;
	}
}

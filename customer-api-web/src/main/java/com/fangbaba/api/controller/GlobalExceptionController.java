package com.fangbaba.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fangbaba.api.domain.open.OpenResponse;
import com.fangbaba.api.exception.OpenException;

/**
 * 系统例外处理
 *
 * @author nolan.
 *
 */
@ControllerAdvice
public class GlobalExceptionController {
	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);


	@ExceptionHandler(OpenException.class)
	public ResponseEntity<OpenResponse<Object>> handleOmsException(OpenException ex) {
		
		
		OpenResponse<Object> omsResponse = new OpenResponse<Object>();
		omsResponse.setResult("false");
		omsResponse.setErrormessage(ex.getMessage());
		omsResponse.setErrorcode(ex.getCode());
		
		logger.error("异常",ex);
		return new ResponseEntity<OpenResponse<Object>>(omsResponse, HttpStatus.OK);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<OpenResponse<Object>> handleAllException(Exception ex) {


		OpenResponse<Object> omsResponse = new OpenResponse<Object>();
		omsResponse.setResult("false");
		omsResponse.setErrormessage(ex.getMessage());
		omsResponse.setErrorcode("");
	
		logger.error("异常",ex);
		return new ResponseEntity<OpenResponse<Object>>(omsResponse, HttpStatus.OK);
	}
	
	
	

}
package com.capgemini.hospital_management_system.exception;

public class EntityAlreadyExist extends RuntimeException {
	public EntityAlreadyExist(String message) {
		super(message);
	}
}

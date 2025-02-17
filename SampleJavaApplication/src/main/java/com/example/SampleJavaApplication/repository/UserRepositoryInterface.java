package com.example.SampleJavaApplication.repository;

public interface UserRepositoryInterface {

	boolean checkUserCredentials(String username, String password);

	void insertUser(String username, String password);

}

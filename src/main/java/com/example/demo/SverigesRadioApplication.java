package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SverigesRadioApplication {

	public static void main(String[] args) {
		SpringApplication.run(SverigesRadioApplication.class, args);
	}

	/*
 		MVC - Model View Controller

 		Model - Store data
 		For example: the database
 		Spring - repositories

 		View - the interface where the uswe (or programmer) can interact with
 		For example: REST-apo
 		Spring - controllers

 		Controller - Is the logics, change of data
 		For example:
 		Spring - services
 */

}

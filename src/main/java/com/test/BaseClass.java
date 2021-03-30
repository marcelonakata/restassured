package com.test;

import org.testng.annotations.BeforeTest;

import io.restassured.RestAssured;

public class BaseClass {
	@BeforeTest
	public void setup() {
		RestAssured.baseURI = "http://ergast.com/api";	}
}

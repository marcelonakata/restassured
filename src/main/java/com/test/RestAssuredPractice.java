package com.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;


public class RestAssuredPractice extends BaseClass{
	
	@Test
	public void test1() {
		
		Response response = given().
				pathParam("circuitId", "monaco").
				//contentType(ContentType.JSON).
	    when().
	        get("/f1/2017/circuits/{circuitId}.json").
	    then().
	    	assertThat().
	    	contentType(ContentType.JSON).
	    	statusCode(200).
	    	and().
	    	extract().response();
	    
		System.out.println(response.statusCode());
		System.out.println(response.getBody().asString());
		System.out.println(response.getBody().jsonPath().get("MRData.CircuitTable.Circuits.Location.country"));
	}
	
	@Test
	public void test2() {
		
		Response response = given().
				pathParam("circuitId", "monaco").
				//contentType(ContentType.JSON).
	    when().
	        get("/f1/2017/circuits/{circuitId}.json").
	    then().
	    	assertThat().
	    	contentType(ContentType.JSON).
	    	statusCode(200).
	    	and().
	    	extract().response();
	    
		System.out.println(response.statusCode());
		System.out.println(response.getBody().asString());
		System.out.println(response.getBody().jsonPath().get("MRData.CircuitTable.Circuits.Location.country"));
	}
}

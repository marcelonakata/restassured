package com.test;
import static io.restassured.RestAssured.given;

import org.json.JSONObject;
import org.testng.annotations.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class BaseClass {
	
	static String token = null;
	
	@BeforeClass
	public void setup() {
		RestAssured.baseURI = "https://restful-booker.herokuapp.com/";	
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("username", "admin");
		jsonObj.put("password", "password123");
	
		token = 
			given().
				contentType(ContentType.JSON).
				body(jsonObj.toString()).
			when().
				post("/auth").
			then().
				extract().
				response().getBody().jsonPath().get("token");	
		
		System.out.println("Token: " + token);
	
	}
	
	public Response getBookingId(String bookingId) {
		return  
			given().
				pathParam("id", bookingId).
		    when().
		        get("/booking/{id}").
		    then().
		    	statusCode(200).
		    	and().
		    	extract().response();
	}
	
	public String getJsonValue(Response response, String field) {
		return response.getBody().jsonPath().get(field);
	}
}

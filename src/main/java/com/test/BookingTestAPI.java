package com.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookingTestAPI extends BaseClass{
	
	String bookingId;
	
	@Test (priority=1)
	public void validateGetAllBookings() {
		
		Response response = 
		given().
	    when().
	        get("/booking").
	    then().
	    	assertThat().
	    	contentType(ContentType.JSON).
	    	statusCode(200).
	    	//body("bookingid", hasItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).
	    	and().
	    	extract().response();
	    
		// Quantity of records 
		List<String> list = response.getBody().jsonPath().get("$");
		System.out.println("Quantity of bookings: " + list.size());
	}
	
	@Test (priority=2)
	public void validatePostBooking() {	
		
		JSONObject obj = new JSONObject();
		JSONObject dates = new JSONObject();
		obj.put("firstname", "Marcelo");
		obj.put("lastname", "Brown");
		obj.put("totalprice", "111");
		obj.put("depositpaid", Boolean.TRUE);
		obj.put("additionalneeds", "Breakfast");
		dates.put("checkin", "2020-10-01");
		dates.put("checkout", "2020-10-02");
		obj.put("bookingdates",dates);
		
		Response response = 
		given().
			contentType(ContentType.JSON).
			body(obj.toString()).
		when().
			post("/booking").
		then().
	    	contentType(ContentType.JSON).
	    	statusCode(200).
	    	and().
	    	extract().response();
	    
		bookingId = response.getBody().jsonPath().getString("bookingid");
		System.out.println("new Booking ID created: " + bookingId);
	}
	
	@Test (priority=3)
	public void verifyPostBooking() {	
		
		Response response = getBookingId(bookingId);
	    
		Assert.assertEquals(getJsonValue(response, "firstname"), "Marcelo");
	}
	
	@Test (priority=4)
	public void validateInvalidBookingDate() {
		
		JSONObject obj = new JSONObject();
		JSONObject dates = new JSONObject();
		obj.put("firstname", "Marcelo");
		obj.put("lastname", "Brown");
		obj.put("totalprice", "111");
		obj.put("depositpaid", Boolean.TRUE);
		obj.put("additionalneeds", "Breakfast");
		dates.put("checkin", "x");
		dates.put("checkout", "x");
		obj.put("bookingdates",dates);
		
		Response response =
		given().
			contentType(ContentType.JSON).
			body(obj.toString()).
		when().
			post("/booking").
		then().
			statusCode(200).
			and().
			extract().response();
		
		Assert.assertEquals(response.getBody().asString(), "Invalid date");
	}
	
	@Test (priority=5)
	public void validatePutBooking() {
		JSONObject obj = new JSONObject();
		JSONObject dates = new JSONObject();
		obj.put("firstname", "Marcelo");
		obj.put("lastname", "Update");
		obj.put("totalprice", "111");
		obj.put("depositpaid", Boolean.TRUE);
		obj.put("additionalneeds", "Breakfast");
		dates.put("checkin", "2020-10-01");
		dates.put("checkout", "2020-10-02");
		obj.put("bookingdates",dates);
		
		given().
			header("Cookie","token="+token).
			contentType(ContentType.JSON).
			body(obj.toString()).
			pathParam("id", bookingId).
		when().
			put("/booking/{id}").
		then().
			statusCode(200);		
	}
	
	@Test(priority=6)
	public void verifyPutBooking() {
		Response response = getBookingId(bookingId);
		
		Assert.assertEquals(getJsonValue(response, "lastname"), "Update");
	}	
	
	@Test(priority=7)
	public void validatePatchBooking() {
		
		final String newName = "Steve";
		JSONObject obj = new JSONObject();
		obj.put("firstname", newName);
		
		given().
			header("Cookie", "token=" + token).
			contentType(ContentType.JSON).
			pathParam("id", bookingId).
			body(obj.toString()).
		when().
			patch("/booking/{id}").
		then().
			statusCode(200);
		
		Response response = getBookingId(bookingId);
		assertEquals(getJsonValue(response, "firstname"), newName);
	}
	
	@Test(priority=8)
	public void validateDeleteBooking() {
		
		given().
			header("Cookie", "token="+token).
			pathParam("id", bookingId).
		when().
			delete("/booking/{id}").
		then().
			statusCode(201);
		
		given().
			pathParam("id", bookingId).
		when().
        	get("/booking/{id}").
        then().
        	assertThat().
    		statusCode(404);
	}
}

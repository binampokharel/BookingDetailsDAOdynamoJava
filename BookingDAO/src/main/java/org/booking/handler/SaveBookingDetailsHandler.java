package org.booking.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.booking.DTO.ResponseDTO;
import org.booking.entities.AppointmentSegmentDetails;
import org.booking.entities.BookingDetails;
import org.slf4j.Logger;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SaveBookingDetailsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @SneakyThrows
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        // Step 1: Parse the request body and extract the ZipcodeRequest
        BookingDetails request = parseRequest(input.getBody());

        // Step 2: Create DynamoDbClient instance
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        // Step 3: Insert item into DynamoDB table
        Map<String, AttributeValue> itemValues = new HashMap<>();

        List<String> timeSlots = new ArrayList<>();
        timeSlots = request.getTime_slots();
        String timeSlotsAsString = "";
        if (timeSlots != null && !timeSlots.isEmpty()) {
            timeSlotsAsString = String.join(",", timeSlots);
        }

        List<AppointmentSegmentDetails> appointmentSegments = new ArrayList<>();
        appointmentSegments = request.getAppointment_segments();
        String appointmentSegmentsAsString = "";
        if (request.getAppointment_segments() != null) {
            appointmentSegmentsAsString = appointmentSegments.stream().map(AppointmentSegmentDetails::toString).collect(Collectors.joining(","));
        }

        itemValues.put("id", AttributeValue.builder().s(request.getId()).build());
        itemValues.put("assigned_technician", AttributeValue.builder().s(request.getAssigned_technician()).build());
        itemValues.put("booking_id", AttributeValue.builder().s(request.getBooking_id()).build());
        itemValues.put("customer_id", AttributeValue.builder().s(request.getCustomer_id()).build());
        itemValues.put("customer_name", AttributeValue.builder().s(request.getCustomer_name()).build());
        itemValues.put("email", AttributeValue.builder().s(request.getEmail()).build());
        itemValues.put("phone", AttributeValue.builder().s(request.getPhone()).build());
        itemValues.put("zip_code", AttributeValue.builder().s(request.getZip_code()).build());
        itemValues.put("items_booked", AttributeValue.builder().s(request.getItems_booked()).build());
        itemValues.put("appointment_segments", AttributeValue.builder().s(appointmentSegmentsAsString).build());
        ;
//        itemValues.put("time_slot_response", AttributeValue.builder().s(request.getBooking_segments()).build());
        itemValues.put("time_slots", AttributeValue.builder().s(timeSlotsAsString).build());
        ;
        itemValues.put("booking_status", AttributeValue.builder().bool(request.isBooking_status()).build());
        itemValues.put("created_date", AttributeValue.builder().s(request.getCreated_date()).build());
        itemValues.put("appointment_date", AttributeValue.builder().s(request.getAppointment_date()).build());
        itemValues.put("booking_response", AttributeValue.builder().s(request.getBooking_response()).build());
        itemValues.put("request_type", AttributeValue.builder().s(request.getRequest_type()).build());
        itemValues.put("branch", AttributeValue.builder().s(request.getBranch()==null ? "": request.getBranch()).build());
        itemValues.put("total", AttributeValue.builder().n(Double.toString(request.getTotal())).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("BOOKING_DETAILS")
                .item(itemValues)
                .build();

        try {
            PutItemResponse putItemResponse = dynamoDbClient.putItem(putItemRequest);

            // Step 4: Create the response message based on the result
            String responseMessage;
            String statusCode;
            if (putItemResponse.sdkHttpResponse().isSuccessful()) {
                responseMessage = "Data Inserted Successfully";
                statusCode = "200";
            } else {
                responseMessage = "Error Inserting data";
                statusCode = "500";
            }

            // Step 5: Create the ResponseDTO
            ResponseDTO responseDTO = new ResponseDTO(statusCode, responseMessage);

            // Step 6: Create and return the APIGatewayProxyResponseEvent
            APIGatewayProxyResponseEvent apiResponse = new APIGatewayProxyResponseEvent();
            apiResponse.setStatusCode(200);

            // Add CORS headers
            Map<String, String> headers = new HashMap<>();
            headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
            headers.put("Access-Control-Allow-Methods", "OPTIONS,POST");
            headers.put("Access-Control-Allow-Origin", "*");

            apiResponse.setHeaders(headers);

            ObjectMapper objectMapper = new ObjectMapper();
            String responseJson = objectMapper.writeValueAsString(responseDTO);

            apiResponse.setBody(responseJson);

            return apiResponse;
        } catch (DynamoDbException | JsonProcessingException e) {
            // Handle exception
            e.printStackTrace();
            // Step 5: Create a more informative error message
            String errorMessage = "An error occurred while processing your request.";

            if (e instanceof DynamoDbException) {
                DynamoDbException dbException = (DynamoDbException) e;
                errorMessage = "DynamoDB Error: " + dbException.awsErrorDetails().errorMessage();
            } else if (e instanceof JsonProcessingException) {
                JsonProcessingException jsonException = (JsonProcessingException) e;
                errorMessage = "JSON Processing Error: " + jsonException.getMessage();
            }

            // Step 6: Create the ResponseDTO for the exception case
            ResponseDTO responseDTO = new ResponseDTO("500", errorMessage);

            // Step 7: Create and return the APIGatewayProxyResponseEvent for the exception case
            APIGatewayProxyResponseEvent apiResponse = new APIGatewayProxyResponseEvent();
            apiResponse.setStatusCode(500); // Use the appropriate HTTP status code for internal server error

            ObjectMapper objectMapper = new ObjectMapper();
            String responseJson = objectMapper.writeValueAsString(responseDTO);

            apiResponse.setBody(responseJson);

            return apiResponse;
        }
    }

    private BookingDetails parseRequest(String requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(requestBody, BookingDetails.class);
        } catch (JsonProcessingException e) {
            // Handle exception
            e.printStackTrace();
            // Return a default ZipcodeRequest or throw an exception based on your requirement
            return new BookingDetails();
        }
    }

}

package org.booking.entities;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class BookingDetails {
    private String id;

    @Getter(onMethod = @__({@DynamoDbAttribute("assigned_technician")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("assigned_technician")}))
    private String assigned_technician;

    @Getter(onMethod = @__({@DynamoDbAttribute("booking_id")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("booking_id")}))
    private String booking_id;

    @Getter(onMethod = @__({@DynamoDbAttribute("customer_id")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("customer_id")}))
    private String customer_id;

    @Getter(onMethod = @__({@DynamoDbAttribute("customer_name")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("customer_name")}))
    private String customer_name;

    @Getter(onMethod = @__({@DynamoDbAttribute("email")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("email")}))
    private String email;

    @Getter(onMethod = @__({@DynamoDbAttribute("phone")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("phone")}))
    private String phone;

    @Getter(onMethod = @__({@DynamoDbAttribute("zip_code")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("zip_code")}))
    private String zip_code;

    @Getter(onMethod = @__({@DynamoDbAttribute("items_booked")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("items_booked")}))
    private String items_booked;

//    @Getter(onMethod = @__({@DynamoDbAttribute("time_slot_response")}))
//    @Setter(onMethod = @__({@DynamoDbAttribute("time_slot_response")}))
//    private String time_slot_response;

    @Getter(onMethod = @__({@DynamoDbAttribute("appointment_segments")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("appointment_segments")}))
    private List<AppointmentSegmentDetails> appointment_segments;

    @Getter(onMethod = @__({@DynamoDbAttribute("time_slots")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("time_slots")}))
    private List<String> time_slots;

    @Getter(onMethod = @__({@DynamoDbAttribute("booking_status")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("booking_status")}))
    private boolean booking_status;

    @Getter(onMethod = @__({@DynamoDbAttribute("booking_response")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("booking_response")}))
    private String booking_response;

    @Getter(onMethod = @__({@DynamoDbAttribute("appointment_date")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("appointment_date")}))
    private String appointment_date;

    @Getter(onMethod = @__({@DynamoDbAttribute("created_date")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("created_date")}))
    private String created_date;

    @Getter(onMethod = @__({@DynamoDbAttribute("request_type")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("request_type")}))
    private String request_type;

    @Getter(onMethod = @__({@DynamoDbAttribute("branch")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("branch")}))
    private String branch;

    @Getter(onMethod = @__({@DynamoDbAttribute("total")}))
    @Setter(onMethod = @__({@DynamoDbAttribute("total")}))
    private double total;
}

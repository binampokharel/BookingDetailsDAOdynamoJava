package org.booking.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentSegmentDetails {
    private int duration_minutes;
    private String service_variation_id;
    private long service_variation_version;
    private String team_member_id;

    @Override
    public String toString() {
        // Create a custom string representation for the object
        return "AppointmentSegmentDetails [duration_minutes=" + duration_minutes +
                ", service_variation_id=" + service_variation_id +
                ", service_variation_version=" + service_variation_version +
                ", team_member_id=" + team_member_id + "]";
    }
}


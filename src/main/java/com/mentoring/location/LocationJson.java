package com.mentoring.location;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * @author ivanovaolyaa
 * @version 10/12/2017
 */
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LocationJson {

    private String country;

    private String countryCode;

    private String regionName;

    private String timezone;

}

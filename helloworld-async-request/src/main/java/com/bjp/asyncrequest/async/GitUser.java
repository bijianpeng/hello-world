package com.bjp.asyncrequest.async;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitUser {
    private String name;
    private String blog;
}

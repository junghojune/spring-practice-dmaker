package com.dmaker.dto;

import com.dmaker.exception.DMakerErrorCode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DMakerErrorResponse {
    private DMakerErrorCode errorCode;
    private String errorMessage;
}

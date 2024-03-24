package org.example.dtos;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class FileDataDTO {
    private byte[] data;
    private String extension;
    private String contentType;
}

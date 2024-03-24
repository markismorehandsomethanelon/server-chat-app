package org.example.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
public class MultimediaMessageDTO extends MessageDTO {
    private String fileName;

    private FileDataDTO dataFile;

    private String type;
}

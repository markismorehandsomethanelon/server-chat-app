package org.example.mappers;

import org.example.dtos.MultimediaMessageDTO;
import org.example.dtos.TextMessageDTO;
import org.example.entities.MultimediaMessageEntity;
import org.example.entities.TextMessageEntity;
import org.example.utils.FileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MultiMediaMessageMapper {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileUtil fileUtil;

    public MultimediaMessageEntity convertToEntity(MultimediaMessageDTO multimediaMessageDTO) {
        MultimediaMessageEntity multimediaMessageEntity = modelMapper.map(multimediaMessageDTO, MultimediaMessageEntity.class);

        if (multimediaMessageDTO.getSender() != null) {
            multimediaMessageEntity.setSender(userMapper.convertToEntity(multimediaMessageDTO.getSender()));
        }

        return multimediaMessageEntity;
    }

    public MultimediaMessageDTO convertToDTO(MultimediaMessageEntity multimediaMessageEntity) {

        MultimediaMessageDTO multimediaMessageDTO = modelMapper.map(multimediaMessageEntity, MultimediaMessageDTO.class);

        if (multimediaMessageEntity.getSender() != null) {
            multimediaMessageDTO.setSender(userMapper.convertToDTO(multimediaMessageEntity.getSender()));
        }

        multimediaMessageDTO.setDataFile(fileUtil.getFile(multimediaMessageEntity.getFileCode()));

        multimediaMessageDTO.setInstanceOf("MULTIMEDIA");

        return multimediaMessageDTO;
    }
}

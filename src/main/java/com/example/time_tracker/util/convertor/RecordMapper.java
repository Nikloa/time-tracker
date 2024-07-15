package com.example.time_tracker.util.convertor;

import com.example.time_tracker.model.Record;
import com.example.time_tracker.model.dto.RecordDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecordMapper {
    Record dtoToModel(RecordDto dto);
    RecordDto modelToDto(Record model);
    List<RecordDto> toListDto(List<Record> models);
}

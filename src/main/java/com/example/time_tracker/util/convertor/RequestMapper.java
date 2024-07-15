package com.example.time_tracker.util.convertor;

import com.example.time_tracker.model.User;
import com.example.time_tracker.model.auth.RegistrationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    User requestToModel(RegistrationRequest request);
}

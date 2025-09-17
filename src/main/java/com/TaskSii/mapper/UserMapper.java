package com.TaskSii.mapper;

import com.TaskSii.dto.RegisterOwnerDTO;
import com.TaskSii.dto.RegisterRequestDTO;
import com.TaskSii.dto.UserDto;
import com.TaskSii.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "user_id", ignore = true)
    @Mapping(target = "ownerProfile", ignore = true)
    @Mapping(target = "volunteer", ignore = true)
    User fromRegisterRequestDTO(RegisterRequestDTO registerRequestDTO);


//    @Mapping(target = "roles", ignore = true)
//    @Mapping(target = "password", ignore = true)
//    @Mapping(target = "enabled", constant = "true")
//    User fromRegisterVolunteerDTO(RegisterVolunteerDTO dto);

}

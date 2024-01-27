package az.dev.smallbankingapp.mapper;

import az.dev.smallbankingapp.dto.request.UserRequest;
import az.dev.smallbankingapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends GenericMapper<UserRequest, User> {
}

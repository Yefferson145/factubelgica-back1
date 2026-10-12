package com.factubelgica.api.contexts.user_management.persistance;

import com.factubelgica.api.contexts.user_management.models.User;
import com.factubelgica.api.shared.utils.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepository implements IUserRepository {

  private final JpaUserRepository jpaUserRepository;

  @Override
  public Optional<User> save(User user) {
    try {
      UserSchema newUser = UserSchema.fromUser(user);
      UserSchema savedUser = jpaUserRepository.save(newUser);
      return Optional.of(savedUser.toUser());
    } catch (Exception e) {
      Slf4j.logger.warn("Error saving user", e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<User> findByEmail(String email) {
    try {
      return jpaUserRepository
          .findByEmail(email)
          .map(UserSchema::toUser);
    }
    catch (Exception e) {
      Slf4j.logger.warn("Error saving user", e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<List<User>> getUsersPublicInfo(UUID lastID, int limit) {
    try {
      return Optional.of(
          jpaUserRepository
          .findUsersPaginated(lastID, limit)
          .stream()
          .map(UserSchema::toUser)
          .toList()
      );
    } catch (Exception e) {
      Slf4j.logger.warn("Error searching users from lastId: {} with limit of {}", lastID, limit);
      return Optional.empty();
    }
  }
}

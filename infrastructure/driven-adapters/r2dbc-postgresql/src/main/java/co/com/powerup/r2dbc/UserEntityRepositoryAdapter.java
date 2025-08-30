package co.com.powerup.r2dbc;

import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.r2dbc.entity.UserEntity;
import co.com.powerup.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserEntityRepositoryAdapter extends ReactiveAdapterOperations<
        User/* change for domain model */,
        UserEntity/* change for adapter model */,
        Long,
        UserEntityRepository
        > implements UserRepository {
    public UserEntityRepositoryAdapter(UserEntityRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, User.class/* change for domain model */));
    }

    @Override
    @Transactional
    public Mono<User> save(User user) {
        return super.save(user);
    }

    @Override
    public Flux<User> findAll() {
        return super.findAll();
    }

    @Override
    public Mono<User> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(entity -> super.mapper.map(entity, User.class));
    }

}

package co.com.powerup.r2dbc;

import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.r2dbc.entity.UserEntity;
import co.com.powerup.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserEntityRepositoryAdapter extends ReactiveAdapterOperations<
        User/* change for domain model */,
        UserEntity/* change for adapter model */,
        String,
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
    public Mono<User> save(User User) {
        return super.save(User);
    }
    @Override
    public Flux<User> findAll() {
        return super.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return super.findById(id);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

}

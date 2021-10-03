package com.presto.auth.spec;


import com.presto.auth.entity.User;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Or({
        @Spec(path = "phone", params="phone", spec = EqualIgnoreCase.class),
        @Spec(path = "email", params="email", spec = EqualIgnoreCase.class),
        @Spec(path = "firstName", params="first_name", spec = LikeIgnoreCase.class),
        @Spec(path = "lastName", params="last_name", spec = LikeIgnoreCase.class),
})
public interface SearchUserSpec extends Specification<User> {
}

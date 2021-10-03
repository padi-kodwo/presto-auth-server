package com.presto.auth.spec;

import com.presto.auth.entity.Role;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Or({
        @Spec(path = "code", params="code", spec = LikeIgnoreCase.class),
        @Spec(path = "name", params="name", spec = LikeIgnoreCase.class),
        @Spec(path = "description", params="description", spec = In.class)
})
public interface RoleSpec extends Specification<Role> {
}
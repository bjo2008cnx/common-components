package com.github.common.componets.dds.repository;

import com.github.common.componets.dds.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
}

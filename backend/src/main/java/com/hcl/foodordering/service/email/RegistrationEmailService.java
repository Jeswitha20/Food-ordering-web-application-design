package com.hcl.foodordering.service.email;

import com.hcl.foodordering.entity.User;

public interface RegistrationEmailService {
    boolean sendRegistrationSuccess(User user);
}


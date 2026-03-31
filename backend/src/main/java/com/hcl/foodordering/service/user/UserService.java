package com.hcl.foodordering.service.user;

import com.hcl.foodordering.dto.user.UpdateUserRequest;
import com.hcl.foodordering.dto.user.UserMeResponse;

public interface UserService {
    UserMeResponse getMe();

    UserMeResponse updateMe(UpdateUserRequest request);

    void deleteMe();
}


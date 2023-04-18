package org.example.backend.service.user;

import org.example.backend.persistence.entity.MemberEntity;
import org.example.backend.service.user.CustomUserDetails;

public class UserAdapter extends CustomUserDetails {
    private MemberEntity member;

    public UserAdapter(MemberEntity member) {
        super(member);
        this.member = member;
    }
}

package org.example.backend.config;

import org.example.backend.persistence.entity.MemberEntity;
import org.example.backend.service.CustomUserDetails;

public class UserAdapter extends CustomUserDetails {
    private MemberEntity member;

    public UserAdapter(MemberEntity member) {
        super(member);
        this.member = member;
    }
}

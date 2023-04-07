package org.example.backend.controller;

import org.example.backend.controller.dto.MemberDTO;
import org.example.backend.persistence.entity.MemberEntity;
import org.example.backend.service.MemberService;
import org.example.backend.service.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody MemberDTO memberDTO) {
        return ResponseEntity.ok().body(memberService.createMember(memberDTO));
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<?> signin(@RequestBody MemberDTO memberDTO) {
        MemberEntity entity = memberService
                .getByCredentials(memberDTO.getEmail(), memberDTO.getPassword());
        if (entity == null)
            throw new IllegalArgumentException("login fail");
        final String token = tokenProvider.createToken(entity);
        return ResponseEntity.ok().body(makeMemberDTOWithToken(entity,token));
    }

    private MemberDTO makeMemberDTOWithToken(MemberEntity member, String token) {
        return MemberDTO.builder()
                .email(member.getEmail())
                .id(member.getId())
                .nickname(member.getNickname())
                .token(token)
                .build();
    }
}

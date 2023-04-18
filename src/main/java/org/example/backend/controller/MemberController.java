package org.example.backend.controller;

import org.example.backend.service.UserAdapter;
import org.example.backend.controller.dto.MemberDTO;
import org.example.backend.persistence.entity.MemberEntity;
import org.example.backend.service.MemberService;
import org.example.backend.service.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/users/new")
    public ResponseEntity<?> signup(@RequestBody MemberDTO memberDTO) {
        return ResponseEntity.ok().body(memberService.createMember(memberDTO));
    }

    @PostMapping("/users")
    public ResponseEntity<?> signin(@RequestBody MemberDTO memberDTO) {
        MemberEntity entity = memberService
                .getByCredentials(memberDTO.getEmail(), memberDTO.getPassword());
        if (entity == null)
            throw new IllegalArgumentException("login fail");
        final String token = tokenProvider.createToken(entity);
        return ResponseEntity.ok().body(makeMemberDTOWithToken(entity,token));
    }

    @PostMapping("/users/find/password")
    public ResponseEntity<?> findPassword(@RequestBody MemberDTO memberDTO) {
        MemberEntity entity = memberService.getByEmail(memberDTO.getEmail());
        if (entity == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong email");
        memberService.makeEmailCode(memberDTO.getEmail());
        return ResponseEntity.ok().body("exists email");
    }

    @PostMapping("/users/find/password/code")
    public ResponseEntity<?> findPasswordWithCode(@RequestBody MemberDTO memberDTO) {
        MemberEntity entity = memberService.matchEmailCode(
                memberDTO.getEmail(),
                memberDTO.getEmailCode());
        if (entity == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong email");
        final String token = tokenProvider.createToken(entity);
        return ResponseEntity.ok().body(makeMemberDTOWithToken(entity, token));
    }

    @PostMapping("/users/change/password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserAdapter user,
                                            @RequestBody MemberDTO memberDTO) {
        if (!user.getEmail().equals(memberDTO.getEmail()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong Email");
        return ResponseEntity.ok().body(memberService.changePassword(memberDTO));
    }

    private MemberDTO makeMemberDTOWithToken(MemberEntity member, String token) {
        return MemberDTO.builder()
                .email(member.getEmail())
                .id(member.getId())
                .username(member.getUsername())
                .token(token)
                .gender(member.getGender())
                .birthday(member.getBirthday())
                .build();
    }
}

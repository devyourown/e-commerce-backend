package org.example.backend.service;

import org.example.backend.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    private MemberService memberService = new MemberService();

    @Test
    @DisplayName("MakeEmailAuthorizationCode Test")
    void makeEmailAuthorizationCode() {
        String code = memberService.makeEmailAuthorizationCode("test@naver.com");
        for (int i=0; i<1000; i++)
            assertTrue(code.length() == 6);
    }
}
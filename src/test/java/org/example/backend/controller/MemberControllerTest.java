package org.example.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.controller.dto.MemberDTO;
import org.example.backend.persistence.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Signup Test")
    public void testSignup() throws Exception {
        MemberDTO memberDTO = makeTestMemberDTO();
        String body = mapper.writeValueAsString(memberDTO);
        mvc.perform(post("/auth/signup")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertNotNull(memberRepository.findByEmail(memberDTO.getEmail()));
    }

    private MemberDTO makeTestMemberDTO() {
        return MemberDTO.builder()
                .email("test@naver.com")
                .nickname("test1")
                .password("1234")
                .build();
    }

    @Test
    @DisplayName("Signin Test")
    public void testSignin() throws Exception {
        String body = mapper.writeValueAsString(makeTestMemberDTO());
        mvc.perform(post("/auth/signin")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
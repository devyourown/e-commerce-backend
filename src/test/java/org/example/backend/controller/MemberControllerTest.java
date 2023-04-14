package org.example.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.controller.dto.Gender;
import org.example.backend.controller.dto.MemberDTO;
import org.example.backend.persistence.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
        mvc.perform(post("/users/new")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertNotNull(memberRepository.findByEmail(memberDTO.getEmail()));
    }

    private MemberDTO makeTestMemberDTO() {
        return MemberDTO.builder()
                .email("test@naver.com")
                .username("test1")
                .password("1234")
                .birthday("20110931")
                .gender(Gender.MALE)
                .build();
    }

    @Test
    @DisplayName("Signin Test")
    public void testSignin() throws Exception {
        testSignup();
        String body = mapper.writeValueAsString(makeTestMemberDTO());
        mvc.perform(post("/users")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Find Password Test")
    public void testFindPassword() throws Exception {
        testSignup();
        String body = mapper.writeValueAsString(makePasswordTestMemberDTO());
        mvc.perform(post("/users/find/password")
                        .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Match Email Code Test ")
    public MvcResult testMatchEmailCode() throws Exception {
        testFindPassword();
        String body = mapper.writeValueAsString(makePasswordTestMemberDTO());
        return mvc.perform(post("/users/find/password/code")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Change Password Test")
    public void testChangePassword() throws Exception {
        String token = getBearerToken(testMatchEmailCode());
        String body = mapper.writeValueAsString(makePasswordTestMemberDTO());
        mvc.perform(post("/users/change/password")
                .content(body)
                        .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private String getBearerToken(MvcResult result) throws Exception {
        return "Bearer " + result.getResponse().getContentAsString()
                .replace("{\"token\":\"", "")
                .split("\",")[0];
    }

    private MemberDTO makePasswordTestMemberDTO() {
        return MemberDTO.builder()
                .email("test@naver.com")
                .emailCode("123456")
                .password("newPassword")
                .build();
    }
}
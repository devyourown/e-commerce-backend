package org.example.backend.service.member;

import org.example.backend.controller.dto.MemberDTO;
import org.example.backend.persistence.MemberRepository;
import org.example.backend.persistence.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    private Map<String, String> emailCode = new HashMap<>();


    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public MemberEntity getByEmail(String email) {
        final MemberEntity entity = memberRepository.findByEmail(email);
        if (entity == null)
            throw new IllegalArgumentException("Email is wrong");
        return entity;
    }

    public MemberDTO createMember(MemberDTO memberDTO) {
        MemberEntity entity = MemberEntity.builder()
                .email(memberDTO.getEmail())
                .username(memberDTO.getUsername())
                .password(memberDTO.getPassword())
                .gender(memberDTO.getGender())
                .birthday(memberDTO.getBirthday())
                .build();
        MemberEntity registeredMember = createEncodedMember(entity);
        return memberEntityToDTO(registeredMember);
    }

    private MemberEntity createEncodedMember(final MemberEntity entity) {
        validateMemberEntity(entity);
        entity.setPassword(encoder.encode(entity.getPassword()));
        return memberRepository.save(entity);
    }

    private void validateMemberEntity(MemberEntity entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity is null.");
        else if (entity.getEmail() == null)
            throw new IllegalArgumentException("Email is null.");
        else if (memberRepository.existsByEmail(entity.getEmail()))
            throw new IllegalArgumentException("Email already exists.");
    }

    private MemberDTO memberEntityToDTO(MemberEntity entity) {
        return MemberDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .birthday(entity.getBirthday())
                .gender(entity.getGender())
                .build();
    }

    public MemberEntity getByCredentials(final String email, final String password) {
        final MemberEntity original = memberRepository.findByEmail(email);
        if (original != null && encoder.matches(password, original.getPassword()))
            return original;
        return null;
    }

    public MemberEntity getById(final String id) {
        final Optional<MemberEntity> original = memberRepository.findById(id);
        if (original.isPresent())
            return original.get();
        return null;
    }

    public String makeEmailAuthorizationCode(final String email) {
        int code = (int) Math.floor((Math.random() * 899999)+ 100000);
        String strCode = String.valueOf(code);
        emailCode.put(email, strCode);
        return strCode;
    }

    public MemberEntity matchEmailCode(final String email, final String code) {
        if (!emailCode.containsKey(email))
            return null;
        if (!emailCode.get(email).equals(code))
            return null;
        return memberRepository.findByEmail(email);
    }

    public MemberDTO changePassword(MemberDTO memberDTO) {
        MemberEntity origin = memberRepository.findByEmail(memberDTO.getEmail());
        origin.setPassword(memberDTO.getPassword());
        return memberEntityToDTO(changeEncodedPassword(origin));
    }

    private MemberEntity changeEncodedPassword(MemberEntity entity) {
        entity.setPassword(encoder.encode(entity.getPassword()));
        return memberRepository.save(entity);
    }

    public String getEmailCode(String email) {
        return emailCode.get(email);
    }
}

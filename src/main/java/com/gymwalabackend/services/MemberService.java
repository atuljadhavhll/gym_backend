package com.gymwalabackend.services;

import com.gymwalabackend.entity.Member;
import com.gymwalabackend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member saveOrUpdateMember(Member member) {
        return memberRepository.findByEmail(member.getEmail())
                .map(existing -> {
                    // update existing user instead of inserting
                    existing.setFirstName(member.getFirstName());
                    existing.setLastName(member.getLastName());
                    existing.setFullName(member.getFullName());
                    existing.setPictureUrl(member.getPictureUrl());
                    existing.setProvider(member.getProvider());
                    existing.setRole_type(member.getRole_type());
                    return memberRepository.save(existing);
                })
                .orElseGet(() -> memberRepository.save(member)); // insert new
    }

    public Optional<Member> getUserByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
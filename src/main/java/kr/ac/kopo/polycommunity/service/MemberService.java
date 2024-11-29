package kr.ac.kopo.polycommunity.service;

import kr.ac.kopo.polycommunity.dto.MemberDTO;
import kr.ac.kopo.polycommunity.entity.Member;
import kr.ac.kopo.polycommunity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // 회원가입 처리
    public void save(MemberDTO memberDTO) {
        // 비밀번호 암호화 처리
        memberDTO.setMemberPassword(passwordEncoder.encode(memberDTO.getMemberPassword()));

        // 회원 객체 생성 및 저장
        Member member = new Member(
                memberDTO.getMemberId(),
                memberDTO.getMemberPassword(),
                memberDTO.getMemberName(),
                memberDTO.getMemberPhone()
        );

        memberRepository.save(member);
    }

    // 아이디 존재 여부 확인
    public boolean isMemberIdExists(String memberId) {
        return memberRepository.findByMemberId(memberId).isPresent();
    }
    public boolean login(MemberDTO memberDTO) {
        Optional<Member> memberOptional = memberRepository.findByMemberId(memberDTO.getMemberId());
        if (memberOptional.isEmpty()) {
            return false;
        }

        Member member = memberOptional.get();
        return passwordEncoder.matches(memberDTO.getMemberPassword(), member.getMemberPassword());
    }



}

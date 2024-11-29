package kr.ac.kopo.polycommunity.service;

import kr.ac.kopo.polycommunity.entity.Member;
import kr.ac.kopo.polycommunity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + username));

        return new User(member.getMemberId(),
                member.getMemberPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getRole().name())));
    }
}
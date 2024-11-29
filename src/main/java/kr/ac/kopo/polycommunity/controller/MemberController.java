package kr.ac.kopo.polycommunity.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.ac.kopo.polycommunity.dto.MemberDTO;
import kr.ac.kopo.polycommunity.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    // 회원가입 폼
    @GetMapping("/save")
    public String saveForm() {
        return "membership";
    }

    // 회원가입 처리
    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO, Model model) {
        // 아이디 중복 검사
        if (memberService.isMemberIdExists(memberDTO.getMemberId())) {
            model.addAttribute("error", "이미 존재하는 아이디입니다.");
            return "membership";
        }
        memberService.save(memberDTO);
        return "redirect:/member/login";  // 회원가입 후 로그인 페이지로 리다이렉트
    }

    // 로그인 폼
    @GetMapping("/login")
    public String loginForm() {
        return "loginpage";
    }
    // 로그인 처리
    // 로그인 처리
    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, Model model, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            memberDTO.getMemberId(),
                            memberDTO.getMemberPassword()
                    )
            );

            // 로그인 성공 시 SecurityContextHolder에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 세션에 로그인 상태와 사용자 ID 저장
            HttpSession session = request.getSession();
            session.setAttribute("loggedIn", true);
            session.setAttribute("userId", memberDTO.getMemberId()); // 사용자 ID 저장

            return "redirect:/customerservice/";  // 로그인 후 고객센터로 리다이렉트
        } catch (AuthenticationException e) {
            model.addAttribute("error", "로그인에 실패했습니다.");
            return "loginpage";  // 로그인 실패 시 다시 로그인 페이지로
        }

    }
    // 로그인 상태 확인 API
    @GetMapping("/check-login")
    public ResponseEntity<Map<String, Object>> checkLoginStatus(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 기존 세션 가져오기 (없으면 null)
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;
        boolean loggedIn = userId != null;

        Map<String, Object> response = new HashMap<>();
        response.put("loggedIn", loggedIn);
        response.put("userId", userId); // 사용자 ID 반환

        return ResponseEntity.ok(response);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // 로그아웃 성공 응답 반환
        return ResponseEntity.ok().build();
    }
}

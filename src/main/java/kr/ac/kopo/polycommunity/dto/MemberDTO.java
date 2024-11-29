package kr.ac.kopo.polycommunity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.ac.kopo.polycommunity.entity.Member;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long id;
    private String memberId;
    private String memberPassword;
    private String memberName;
    private String memberPhone;


    public MemberDTO(Member member) {
        this.memberId = member.getMemberId();
        this.memberPassword = member.getMemberPassword();
        this.memberName = member.getMemberName();
        this.memberPhone = member.getMemberPhone();
    }


}

package kr.ac.kopo.polycommunity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TravelHomeController {

    @GetMapping("/pageAssignment")
    public String pageAssignment(){
        return "pageAssignment";
    }

    @GetMapping("/loginpage")
    public String loginpage(){return "loginpage";}

    @GetMapping("/membership")
    public String membership(){return "membership";}

    @GetMapping("/subpage01")
    public String subpage01(){return "subpage01";}

    @GetMapping("/subpage02")
    public String subpage02(){return "subpage02";}

    @GetMapping("/subpage03")
    public String subpage03(){return "subpage03";}

    @GetMapping("/subpage04")
    public String subpage04(){return "subpage04";}

    @GetMapping("/subpage05")
    public String subpage05(){return "subpage05";}

    @GetMapping("/subpage06")
    public String subpage06(){return "subpage06";}

    @GetMapping("/subpage07")
    public String subpage07(){return "subpage07";}

    @GetMapping("/subpage08")
    public String subpage08(){return "subpage08";}

}

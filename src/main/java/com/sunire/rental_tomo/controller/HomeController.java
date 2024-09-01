package com.sunire.rental_tomo.controller;

import com.sunire.rental_tomo.domain.entity.Category;
import com.sunire.rental_tomo.domain.entity.Follow;
import com.sunire.rental_tomo.domain.entity.SellerItem;
import com.sunire.rental_tomo.domain.entity.User;
import com.sunire.rental_tomo.enumFile.TokenName;
import com.sunire.rental_tomo.service.*;
import com.sunire.rental_tomo.utils.CookieUtil;
import com.sunire.rental_tomo.utils.setLoginStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final SellerService sellerService;
    private final JwtTokenService jwtTokenService;
    private final SmallService smallService;
    private final FollowService followService;

    //region 회원가입,로그인,메인페이지
    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {

        String name = userService.isLogin(request);
        if (name != null) {
            model = setLoginStatus.setLogin(model, name);
        }
        return "index.th.html";
    }

    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if (name != null) {
            model = setLoginStatus.setLogin(model, name);

        }
        return "realindex.th.html";
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if (name != null) {
            model = setLoginStatus.setLogin(model, name);

        }
        return "user/login.th.html";
    }

    @GetMapping("/join/1")
    public String join(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if (name != null) {
            model = setLoginStatus.setLogin(model, name);

        }
        return "user/join_step1.th.html";
    }

    @GetMapping("/join/2")
    public String join2(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if (name != null) {
            model = setLoginStatus.setLogin(model, name);

        }
        return "user/join_step2.th.html";
    }
    //endregion

    //region 마이페이지
    @GetMapping("/mypage")
    public String mypage(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if (name != null) {
            model = setLoginStatus.setLogin(model, name);
        }
        User user = userService.userInfo(name).orElse(null);
        model.addAttribute("user", user);

        Map<String, Integer> follow_cnt = followService.getAllFollowCount(user);
        model.addAttribute("follow_cnt", follow_cnt);

//        들어가야 하는 정보 아이디, 휴대폰번호, 성별, SNS, 닉네임, 이메일, 생일
        //자기소개 테이블 추가해야하고, 프로필사진 기능 추가하고 불러와야함
        return "user/mypage_intro.th.html";
    }

    @GetMapping("/mypage/edit")
    public String mypage_edit(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if (name != null) {
            model = setLoginStatus.setLogin(model, name);
        }
        User user = userService.userInfo(name).orElse(null);
        model.addAttribute("user", user);

        Map<String, Integer> follow_cnt = followService.getAllFollowCount(user);
        model.addAttribute("follow_cnt", follow_cnt);

//        들어가야 하는 정보 아이디, 휴대폰번호, 성별, SNS, 닉네임, 이메일, 생일
        //자기소개 테이블 추가해야하고, 프로필사진 기능 추가하고 불러와야함
        return "user/mypage_editinfo.th.html";
    }

    @GetMapping("/mypage/deleteid")
    public String deleteid(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if (name != null) {
            model = setLoginStatus.setLogin(model, name);
        }
        User user = userService.userInfo(name).orElse(null);
        model.addAttribute("user", user);

        return "user/mypage_deleteid1.th.html";
    }

    @GetMapping("/mypage/deleteid2")
    public ResponseEntity<String> deleteid2(HttpServletRequest request) {
        String token = CookieUtil.getCookie_Bearer(TokenName.ACCESS_TOKEN.getName(), request);
        String refresh = CookieUtil.getCookie(TokenName.REFRESH_TOKEN.getName(), request);
        Long id = userService.getPk(token);
        //foreign키때문에 user테이블부터 날리면 이슈임.
        //결국 user테이블에도 userid가 있음.
        //어차피 토큰테이블만 특별하게 userid를 받아온거니, 토큰테이블에서 아예 지워버린 뒤 userid를 바꿔야함
        jwtTokenService.deleteRefreshToken(refresh);

        userService.deleteId(id);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("/"))
                .build();
    }
    //endregion

    //region 판매자센터
    @GetMapping("/seller")
    public String seller(Model model, HttpServletRequest request) {
        String name = userService.isLogin(request);
        if (name != null) {
            model = setLoginStatus.setLogin(model, name);
        }
        User user = userService.userInfo(name).orElse(null);
        model.addAttribute("user", user);

        //여기에서 타임리프에 필요한걸 다 넣어줘야함.
        //카테고리도 불러와야함. 사유 : on해둔게 없으면 카테고리 이름 자체를 안들고있음
        Map<String, List<Category>> cat = smallService.getAllCategories();
        List<Category> parent = cat.get("parent");
        List<Category> children = cat.get("children");

        List<SellerItem> items = sellerService.getUserSelling(user);
        model.addAttribute("item", items);
        model.addAttribute("parent", parent);
        model.addAttribute("children", children);

        return "seller/seller_intro.th.html";
    }
    //endregion

    //region 타 유저 정보
    @GetMapping("/info")
    public ResponseEntity<String> info_blank(Model model, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("/"))
                .build();
    }

    @GetMapping("/info/{userid}")
    public String info(Model model, HttpServletRequest request, @PathVariable(value = "userid") String userid) {
        String name = userService.isLogin(request);
        if (name != null) {
            model = setLoginStatus.setLogin(model, name);
        }
        User user = userService.userInfo(name).orElse(null);
        model.addAttribute("user", user);

        User another = userService.userInfoWithUserId(userid).orElse(null);
        model.addAttribute("another", another);

        if (user == another) {
            return "redirect:/mypage";
        }

        //팔로우 유무
        boolean isFollow = followService.isFollow(user, another);
        model.addAttribute("isFollow", isFollow);

        //팔로워, 팔로우 수
        Map<String, Integer> follow_cnt = followService.getAllFollowCount(another);
        model.addAttribute("follow_cnt", follow_cnt);

        return "user/other_info.th.html";
    }

    @GetMapping("/info/item/{userid}")
    public String info_item(Model model, HttpServletRequest request, @PathVariable(value = "userid") String userid) {
        String name = userService.isLogin(request);
        if (name != null) {
            model = setLoginStatus.setLogin(model, name);
        }
        User user = userService.userInfo(name).orElse(null);
        model.addAttribute("user", user);

        //이제 타 유저 정보와 타 유저의 seller_item을 보내야함
        User another = userService.userInfoWithUserId(userid).orElse(null);
        List<SellerItem> item = sellerService.getUserSelling(another);

        model.addAttribute("another", another);
        model.addAttribute("items", item);

        return "user/other_item.th.html";
    }
    //endregion

    //region 팔로우,팔로워 목록

    @GetMapping("/follow/follower/{userid}")
    public String follower(Model model, HttpServletRequest request, @PathVariable(value = "userid") String userid) {
        String name = userService.isLogin(request);
        if (name != null) {
            model = setLoginStatus.setLogin(model, name);
        }
        User user = userService.userInfo(name).orElse(null);
        model.addAttribute("user", user);

        User target;
        if(Objects.equals(userid, user.getUserid())){
            target=user;
        }else{
            target=userService.userInfoWithUserId(userid).orElse(null);
        }
        List<Follow> follower = followService.getFollowerList(target);
        model.addAttribute("target", target);
        model.addAttribute("follower",follower);
        model.addAttribute("title", "팔로워");
        model.addAttribute("isEr", true);

        return "user/follower.th.html";
    }

    @GetMapping("/follow/followed/{userid}")
    public String followed(Model model, HttpServletRequest request, @PathVariable(value = "userid") String userid) {
        String name = userService.isLogin(request);
        if (name != null) {
            model = setLoginStatus.setLogin(model, name);
        }
        User user = userService.userInfo(name).orElse(null);
        model.addAttribute("user", user);

        User target;
        if(Objects.equals(userid, user.getUserid())){
            target=user;
        }else{
            target=userService.userInfoWithUserId(userid).orElse(null);
        }
        List<Follow> followed = followService.getFollowedList(target);
        model.addAttribute("target", target);
        model.addAttribute("follower",followed);
        model.addAttribute("title", "팔로우");
        model.addAttribute("isEr", false);

        return "user/follower.th.html";
    }

    //endregion

//    String name = userService.isLogin(request);
//        if (name != null) {
//        model = setLoginStatus.setLogin(model, name);
//    }
//    User user = userService.userInfo(name).orElse(null);
//        model.addAttribute("user", user);
}

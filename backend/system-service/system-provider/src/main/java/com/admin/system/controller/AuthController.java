package com.admin.system.controller;

import com.admin.common.Result;
import com.admin.system.entity.SysUser;
import com.admin.system.service.SysUserService;
import com.admin.system.vo.LoginVo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final SysUserService sysUserService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public AuthController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @PostMapping("/login")
    public Result<LoginVo> login(@RequestBody Map<String, String> loginForm) {
        String username = loginForm.get("username");
        String password = loginForm.get("password");

        System.out.println("=== 登录请求 ===");
        System.out.println("用户名: " + username);
        System.out.println("密码: " + password);

        SysUser user = sysUserService.getByUsername(username);

        if (user == null) {
            System.out.println("用户不存在");
            return Result.error("用户名或密码错误");
        }

        System.out.println("数据库用户: " + user.getUsername());
        System.out.println("数据库密码哈希: " + user.getPassword());
        System.out.println("用户状态: " + user.getStatus());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("=== 生成新的正确哈希 ===");
        System.out.println("admin123 的新哈希: " + encoder.encode("admin123"));
        System.out.println("======================");

        boolean matches = encoder.matches(password, user.getPassword());
        System.out.println("密码匹配结果: " + matches);

        if (!matches) {
            return Result.error("用户名或密码错误");
        }

        if (user.getStatus() != 1) {
            return Result.error(Result.FORBIDDEN, "账号已被禁用");
        }

        // 生成Token
        String token = Jwts.builder()
                .setSubject(username)
                .claim("userId", String.valueOf(user.getId()))
                .claim("roles", Arrays.asList("admin"))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();

        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        loginVo.setUserId(user.getId());
        loginVo.setUsername(user.getUsername());
        loginVo.setNickname(user.getNickname());

        return Result.success("登录成功", loginVo);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success("退出成功", null);
    }

    @GetMapping("/user")
    public Result<Map<String, Object>> getUserInfo(@RequestHeader("X-Username") String username) {
        SysUser user = sysUserService.getByUsername(username);

        if (user == null) {
            return Result.notFound("用户不存在");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("nickname", user.getNickname());
        data.put("email", user.getEmail());
        data.put("phone", user.getPhone());
        data.put("roles", Arrays.asList("admin"));

        return Result.success(data);
    }
}

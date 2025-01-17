package com.github.WeeiaEduTeam.InfinityFinanceAPI.security;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserCredentialsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
class SecurityController {
    private final SecurityService securityService;

    @PostMapping("/login")
    public void login(@RequestBody AppUserCredentialsDTO credentials) {
        System.out.println("sdsds");
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/security/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(AUTHORIZATION);
        String accessToken = securityService.refreshAccessToken(refreshToken, request.getRequestURL().toString());

        response.setHeader("access_token", accessToken);
        response.setHeader("refresh_token", refreshToken);
    }
}
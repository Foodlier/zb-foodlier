package com.zerobase.foodlier.common.websocket.config;

import com.zerobase.foodlier.common.security.provider.JwtTokenProvider;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.zerobase.foodlier.common.security.constants.AuthorizationConstants.TOKEN_HEADER;
import static com.zerobase.foodlier.common.security.constants.AuthorizationConstants.TOKEN_PREFIX;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String jwt = accessor.getFirstNativeHeader(TOKEN_HEADER);
            if (StringUtils.hasText(jwt) && jwt.startsWith(TOKEN_PREFIX)) {
                jwt = jwt.substring(TOKEN_PREFIX.length()).trim();
            }
            Authentication authentication = jwtTokenProvider
                    .getAuthentication(jwt);
            MemberAuthDto memberAuthDto = (MemberAuthDto) authentication.getPrincipal();

            if (memberAuthDto != null) {
                Member member = memberRepository.findById(memberAuthDto.getId())
                        .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
            }
        }
        return message;
    }
}

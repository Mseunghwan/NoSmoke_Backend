package org.example.nosmoke.dto.smokingInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class SmokingInfoResponseDto {
    private final Long id;
    private final Long userId;
    private final String cigaretteType;
    private final int dailyConsumption;
    private final LocalDateTime quitStartDate;
    private final LocalDateTime targetDate;
    private final String quitGoal;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    // 추가 계산 필드들
    private final int quitDays;        // 금연 일수
    private final int moneySaved;      // 절약한 돈
    private final int cigarettesNotSmoked; // 피우지 않은 담배 개수
}

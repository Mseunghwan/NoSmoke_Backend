package org.example.nosmoke.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.example.nosmoke.dto.dashboard.DashboardResponseDto;
import org.example.nosmoke.entity.QuitSurvey;
import org.example.nosmoke.entity.SmokingInfo;
import org.example.nosmoke.entity.User;
import org.example.nosmoke.repository.QuitSurveyRepository;
import org.example.nosmoke.repository.SmokingInfoRepository;
import org.example.nosmoke.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {
    public final UserRepository userRepository;
    private final SmokingInfoRepository smokingInfoRepository;
    private final QuitSurveyRepository quitSurveyRepository;

    private static final int CIGARETTES_PER_PACK = 20;
    private static final int PRICE_PER_PACK = 4500;

    public DashboardResponseDto getDashboardInfo(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        SmokingInfo smokingInfo = smokingInfoRepository.findByUserId(userId)
                .orElse(null); // 흡연 정보 기 미등록 시 null 허용해야

        List<QuitSurvey> surveys = quitSurveyRepository.findByUserId(userId);

        // 기본 정보 계산
        long quitDays = 0;
        long cigarettesNotSmoked = 0;
        if (smokingInfo != null && smokingInfo.getQuitStartDate() != null) {
            // 오늘 일자 까지의 LocalDate 날짜 수 센다
            quitDays = ChronoUnit.DAYS.between(smokingInfo.getQuitStartDate(), LocalDate.now());
            cigarettesNotSmoked = quitDays * smokingInfo.getDailyConsumption();
        }

        // 절약금액 계산
        long savedMoney = (cigarettesNotSmoked / CIGARETTES_PER_PACK) * PRICE_PER_PACK;

        // 연속 성공일자 계산
        int currentStreak = 0;
        int longestStreak = 0;

        // survey를 날짜순 정렬, 가져와서 isSuccess가 얼마나 유지되는지 확인해보자
        surveys.sort((s1, s2) -> s1.getCreatedAt().compareTo(s2.getCreatedAt()));

        for (QuitSurvey survey : surveys) {
            if(survey.isSuccess()){
                currentStreak++;
            } else {
                if (currentStreak > longestStreak) {
                longestStreak = currentStreak;
                }
                currentStreak = 0;
            }
        }
        if (currentStreak > longestStreak) {
            longestStreak = currentStreak;
        }

        // 건강 개선 효과
        List<String> healthImprovements = calculateHealthImprovements(quitDays);

        return DashboardResponseDto.builder()
                .quitDays(quitDays)
                .savedMoney(savedMoney)
                .cigaretteNotSmoked(cigarettesNotSmoked)
                .currentStreak(currentStreak)
                .longestStreak(longestStreak)
                .points(user.getPoint())
                .build();
    }

    // 아직까진 간단하게
    private List<String> calculateHealthImprovements(long quitDays) {
        List<String> improvements = new ArrayList<>();

        if(quitDays >= 1) {
            improvements.add("심박수와 혈압이 정상으로 돌아오기 시작합니다.");
        }
        if(quitDays >= 14) {
            improvements.add("폐 기능이 향상되기 시작합니다.");
        }

        if(quitDays >= 30) {
            improvements.add("혈액순환이 좋아지고 걷는 것이 원활해집니다.");
        }
        return improvements;

    }
}
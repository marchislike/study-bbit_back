package com.jungle.studybbitback.domain.room.controller.schedule;

import com.jungle.studybbitback.domain.room.entity.schedule.Schedule;
import com.jungle.studybbitback.domain.room.respository.schedule.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleCycleIdGeneratorImpl implements ScheduleCycleIdGenerator {
    private final ScheduleRepository scheduleRepository;

    @Override
    public Long generate(){
        return scheduleRepository.findAll()
                .stream()
                .map(Schedule::getScheduleCycleId)
                .filter(id -> id != null)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }
}

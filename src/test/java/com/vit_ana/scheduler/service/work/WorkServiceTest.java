package com.vit_ana.scheduler.service.work;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.vit_ana.scheduler.dao.WorkRepository;
import com.vit_ana.scheduler.entity.Work;
import com.vit_ana.scheduler.service.impl.WorkServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class WorkServiceTest {

    @Mock
    private WorkRepository workRepository;

    @InjectMocks
    private WorkServiceImpl workService;

    private Work work;
    private UUID workId;
    private Optional<Work> workOptional;
    private List<Work> works;

    @Before
    public void initObjects() {
        work = new Work();
        workId = UUID.randomUUID();
        workOptional = Optional.of(work);
    }

    @Test
    public void shouldSaveWork() {
    	work.setId(workId);
        workService.createNewWork(work);
        verify(workRepository, times(1)).save(work);
    }

    @Test
    public void shouldFindWorkById() {
        when(workRepository.findById(workId)).thenReturn(workOptional);
        assertEquals(workOptional.get(), workService.getWorkById(workId));
        verify(workRepository, times(1)).findById(workId);
    }

    @Test
    public void shouldFindAllWorks() {
        when(workRepository.findAll()).thenReturn(works);
        assertEquals(works, workService.getAllWorks());
        verify(workRepository, times(1)).findAll();
    }

    @Test
    public void shouldDeleteWorkById() {
        workService.deleteWorkById(workId);
        verify(workRepository).deleteById(workId);
    }
}

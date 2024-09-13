package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SessionMapperImplUnitTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    private SessionDto sessionDto;
    private Session session;
    private Teacher teacher;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        teacher = new Teacher();
        teacher.setId(1L);

        user = new User();
        user.setId(2L);

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Session Name");
        sessionDto.setDescription("Session Description");
        sessionDto.setDate(new Date());
        sessionDto.setCreatedAt(LocalDateTime.now());
        sessionDto.setUpdatedAt(LocalDateTime.now());
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Collections.singletonList(2L));

        session = Session.builder()
                .id(1L)
                .name("Session Name")
                .description("Session Description")
                .date(new Date())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .teacher(teacher)
                .users(Collections.singletonList(user))
                .build();
    }

    @Test
    public void testToEntity() {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(2L)).thenReturn(user);

        Session result = sessionMapper.toEntity(sessionDto);

        assertNotNull(result);
        assertEquals(sessionDto.getId(), result.getId());
        assertEquals(sessionDto.getName(), result.getName());
        assertEquals(sessionDto.getDescription(), result.getDescription());
        assertEquals(sessionDto.getDate(), result.getDate());
        assertEquals(sessionDto.getCreatedAt(), result.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), result.getUpdatedAt());
        assertNotNull(result.getTeacher());
        assertEquals(teacher.getId(), result.getTeacher().getId());
        assertNotNull(result.getUsers());
        assertEquals(1, result.getUsers().size());
        assertEquals(user.getId(), result.getUsers().get(0).getId());
    }

    @Test
    public void testToDto() {
        SessionDto result = sessionMapper.toDto(session);

        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        assertEquals(session.getName(), result.getName());
        assertEquals(session.getDescription(), result.getDescription());
        assertEquals(session.getDate(), result.getDate());
        assertEquals(session.getCreatedAt(), result.getCreatedAt());
        assertEquals(session.getUpdatedAt(), result.getUpdatedAt());
        assertNotNull(result.getTeacher_id());
        assertEquals(session.getTeacher().getId(), result.getTeacher_id());
        assertNotNull(result.getUsers());
        assertEquals(1, result.getUsers().size());
        assertEquals(session.getUsers().get(0).getId(), result.getUsers().get(0));
    }

    @Test
    public void testToEntityList() {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(2L)).thenReturn(user);

        List<SessionDto> dtoList = Collections.singletonList(sessionDto);
        List<Session> resultList = sessionMapper.toEntity(dtoList);

        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        Session result = resultList.get(0);
        assertEquals(sessionDto.getId(), result.getId());
        assertEquals(sessionDto.getName(), result.getName());
        assertEquals(sessionDto.getDescription(), result.getDescription());
        assertEquals(sessionDto.getDate(), result.getDate());
        assertEquals(sessionDto.getCreatedAt(), result.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), result.getUpdatedAt());
        assertNotNull(result.getTeacher());
        assertEquals(teacher.getId(), result.getTeacher().getId());
        assertNotNull(result.getUsers());
        assertEquals(1, result.getUsers().size());
        assertEquals(user.getId(), result.getUsers().get(0).getId());
    }

    @Test
    public void testToDtoList() {
        List<Session> entityList = Collections.singletonList(session);
        List<SessionDto> resultList = sessionMapper.toDto(entityList);

        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        SessionDto result = resultList.get(0);
        assertEquals(session.getId(), result.getId());
        assertEquals(session.getName(), result.getName());
        assertEquals(session.getDescription(), result.getDescription());
        assertEquals(session.getDate(), result.getDate());
        assertEquals(session.getCreatedAt(), result.getCreatedAt());
        assertEquals(session.getUpdatedAt(), result.getUpdatedAt());
        assertNotNull(result.getTeacher_id());
        assertEquals(session.getTeacher().getId(), result.getTeacher_id());
        assertNotNull(result.getUsers());
        assertEquals(1, result.getUsers().size());
        assertEquals(session.getUsers().get(0).getId(), result.getUsers().get(0));
    }

    @Test
    public void testToEntity_WithNulls() {
        SessionDto dto = new SessionDto();
        dto.setUsers(null);

        when(teacherService.findById(anyLong())).thenReturn(null);
        when(userService.findById(anyLong())).thenReturn(null);

        Session result = sessionMapper.toEntity(dto);

        assertNotNull(result);
        assertNull(result.getTeacher());
        assertNotNull(result.getUsers());
        assertTrue(result.getUsers().isEmpty());
    }
}

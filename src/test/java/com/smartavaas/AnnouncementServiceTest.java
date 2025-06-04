package com.smartavaas;


import com.smartavaas.model.Announcement;
import com.smartavaas.model.Announcement.Status;
import com.smartavaas.repository.AnnouncementRepository;
import com.smartavaas.service.AnnouncementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnnouncementServiceTest {

    @InjectMocks
    private AnnouncementService announcementService;

    @Mock
    private AnnouncementRepository announcementRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testGetTopOpenAnnouncements_shouldReturnAnnouncements() {
        // Arrange
        Announcement a = new Announcement();
        a.setId(1L);
        a.setDescription("Test Announcement");
        a.setStatus(Status.OPEN);
        when(announcementRepository.findTop15ByStatusOrderByCreatedAtDesc(Status.OPEN))
                .thenReturn(List.of(a));

        // Act
        List<Announcement> result = announcementService.getTopOpenAnnouncements();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Announcement", result.get(0).getDescription());
    }

    @Test
    void testCreateAnnouncement_shouldSaveAnnouncement() {
        // Arrange
        Announcement a = new Announcement();
        a.setDescription("New One");
        a.setDurationInMinutes(20);
        when(announcementRepository.save(any(Announcement.class))).thenReturn(a);

        // Act
        Announcement result = announcementService.createAnnouncement("New One", 20);

        // Assert
        assertNotNull(result);
        assertEquals("New One", result.getDescription());
        assertEquals(20, result.getDurationInMinutes());
    }

    @Test
    void testCloseExpiredAnnouncements_shouldCloseOldOnes() {
        // Arrange
        Announcement expired = new Announcement();
        expired.setStatus(Status.OPEN);
        expired.setDurationInMinutes(5);
        expired.setCreatedAt(LocalDateTime.now().minusMinutes(10));

        when(announcementRepository.findAllByStatus(Status.OPEN)).thenReturn(List.of(expired));
        when(announcementRepository.save(any())).thenReturn(expired);

        // Act
        announcementService.closeExpiredAnnouncements();

        // Assert
        assertEquals(Status.CLOSED, expired.getStatus());
        verify(announcementRepository, times(1)).save(expired);
    }
}
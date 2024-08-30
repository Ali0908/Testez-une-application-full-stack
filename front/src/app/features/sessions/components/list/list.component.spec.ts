import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { RouterTestingModule } from '@angular/router/testing';  // Import this module
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { ListComponent } from './list.component';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockSessions: Session[] = [
    {
      id: 1,
      name: 'Morning Yoga',
      description: 'A relaxing morning yoga session.',
      date: new Date('2024-08-29'),
      teacher_id: 1,
      users: [1, 2],
    },
    {
      id: 2,
      name: 'Evening Meditation',
      description: 'A calming evening meditation session.',
      date: new Date('2024-09-01'),
      teacher_id: 2,
      users: [3, 4],
    }
  ];

  const mockSessionApiService = {
    all: () => of(mockSessions)
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        RouterTestingModule  // Add this import
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('unit test: should display session information correctly', () => {
    fixture.detectChanges(); // Trigger change detection to update the DOM

    const sessionCards = fixture.debugElement.queryAll(By.css('.item'));

    // Check that the correct number of session cards are rendered
    expect(sessionCards.length).toBe(mockSessions.length);

    // Check that the session information is correctly displayed
    sessionCards.forEach((card, index) => {
      const session = mockSessions[index];

      const nameElement = card.query(By.css('mat-card-title')).nativeElement;
      const dateElement = card.query(By.css('mat-card-subtitle')).nativeElement;
      const descriptionElement = card.query(By.css('mat-card-content p')).nativeElement;

      // Check the name
      expect(nameElement.textContent).toContain(session.name);

      // Check the date (formatting as 'longDate')
      const formattedDate = new Date(session.date).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
      expect(dateElement.textContent).toContain(formattedDate);

      // Check the description
      expect(descriptionElement.textContent).toContain(session.description);
    });
  });
});

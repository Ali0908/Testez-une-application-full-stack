import { HttpClientModule } from '@angular/common/http';
import {ComponentFixture, fakeAsync, TestBed, tick, flush} from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { By } from '@angular/platform-browser';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { ActivatedRoute } from '@angular/router';
import { DetailComponent } from './detail.component';
import {Router} from "@angular/router";
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiService: SessionApiService;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const mockSession = {
    id: '1',
    name: 'Test Session',
    date: new Date('2021-12-12'),
    description: 'A detailed description of the session.',
    teacher_id: 1,
    users: [1, 2],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        NoopAnimationsModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        {
          provide: SessionApiService,
          useValue: {
            delete: jest.fn().mockReturnValue(of(null)), // Mocking the delete method
            detail: jest.fn().mockReturnValue(of(mockSession)),
            participate: jest.fn().mockReturnValue(of(null)),
            unParticipate: jest.fn().mockReturnValue(of(null))
          }
        },
        {
          provide: ActivatedRoute, // Mock the ActivatedRoute to provide the 'id' parameter
          useValue: {
            snapshot: {
              paramMap: {
                get: (key: string) => {
                  if (key === 'id') return '1'; // Return the mock session ID
                  return null;
                },
              },
            },
          },
        }
      ],
    }).compileComponents();

    sessionApiService = TestBed.inject(SessionApiService);
    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Integration test: should verify delete session', fakeAsync(() => {
    // Spy on the delete method
    const deleteSpy = jest.spyOn(sessionApiService, 'delete').mockReturnValue(of(null));

    // Spy on the router navigate method
    const navigateSpy = jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));

    // Spy on the snackbar open method
    const snackBar = TestBed.inject(MatSnackBar);
    const snackBarSpy = jest.spyOn(snackBar, 'open');

    // Trigger the delete action
    const deleteButton = fixture.debugElement.query(By.css('button[color="warn"]'));
    deleteButton.nativeElement.click();

    // Simulate passage of time to allow async operations to complete
    fixture.detectChanges();
    tick(); // Move time forward for async calls

    // Verify that the delete method was called with the correct sessionId
    expect(deleteSpy).toHaveBeenCalledWith('1');

    // Verify that the snackbar notification appeared
    expect(snackBarSpy).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });

    // Flush remaining async tasks to ensure everything is processed
    flush();

    // Verify that navigation happened after the delete
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  }));
});

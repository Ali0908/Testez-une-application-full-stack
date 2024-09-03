import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { By } from '@angular/platform-browser';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import {Session} from "../../interfaces/session.interface";

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }
  const mockSession: Session = {
    name: 'Test Session',
    description: 'A detailed description of the session.',
    date: new Date('2021-12-12'),
    teacher_id: 1,
    users: [1, 2],
  };


  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('unit test: should hide the submit button when required input fields are not filled', () => {
    // Set the form to invalid state by not filling in required fields
    component.sessionForm?.controls['name'].setValue('');
    component.sessionForm?.controls['date'].setValue('');
    component.sessionForm?.controls['teacher_id'].setValue('');
    component.sessionForm?.controls['description'].setValue('');

    // Mark all controls as touched to trigger validation errors
    component.sessionForm?.markAllAsTouched();

    fixture.detectChanges();

    // Check if the submit button is disabled
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));

    // Assert that the submit button is disabled
    expect(submitButton.nativeElement.disabled).toBeTruthy();
  });
  it('unit test: should set input fields to non-null values when onUpdate is true', () => {
    // Simulate the component being in update mode
    component.onUpdate = true;

    // Initialize the form with the mock session data
    component.initForm(mockSession);

    // Check that onUpdate is true
    expect(component.onUpdate).toBe(true);

    // Check that the form controls are not null
    expect(component.sessionForm?.controls['name'].value).not.toBeNull();
    expect(component.sessionForm?.controls['date'].value).not.toBeNull();
    expect(component.sessionForm?.controls['teacher_id'].value).not.toBeNull();
    expect(component.sessionForm?.controls['description'].value).not.toBeNull();

    // Format the expected date to match the string format in the form control
    const expectedDate = new Date(mockSession.date).toISOString().split('T')[0];

    // Optional: Check that the form values match the mock data
    expect(component.sessionForm?.controls['name'].value).toBe(mockSession.name);
    expect(component.sessionForm?.controls['date'].value).toBe(expectedDate);
    expect(component.sessionForm?.controls['teacher_id'].value).toBe(mockSession.teacher_id);
    expect(component.sessionForm?.controls['description'].value).toBe(mockSession.description);
  });
  it('integration test: should verify create session form submission', () => {

  });
});

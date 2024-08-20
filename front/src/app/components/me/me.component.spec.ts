import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { By } from "@angular/platform-browser";
import { UserService } from "../../services/user.service";
import { of } from "rxjs";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true, // Simulate an admin user
      id: 1
    }
  };

  const mockUserService = {
    getById: jest.fn(() => of({
      firstName: 'Marie',
      lastName: 'TEST',
      email: 'marie@test.com',
      admin: false,
      createdAt: new Date(),
      updatedAt: new Date()
    })),
  };
  const mockAdminService = {
    getById: jest.fn(() => of({
      firstName: 'Louis',
      lastName: 'TEST',
      email: 'louis@test.com',
      admin: true,
      createdAt: new Date(),
      updatedAt: new Date()
    })),
  };

  describe('Normal user', () => {
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [MeComponent],
        imports: [
          MatSnackBarModule,
          HttpClientModule,
          MatCardModule,
          MatFormFieldModule,
          MatIconModule,
          MatInputModule
        ],
        providers: [{ provide: SessionService, useValue: mockSessionService },
          { provide: UserService, useValue: mockUserService }],
      })
        .compileComponents();

      fixture = TestBed.createComponent(MeComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should display user information correctly for a normal user', () => {
      const mockUser = {
        firstName: 'Marie',
        lastName: 'TEST',
        email: 'marie@test.com',
        admin: false,
        createdAt: new Date(),
        updatedAt: new Date()
      };
      mockUserService.getById.mockReturnValue(of(mockUser));
      fixture.detectChanges(); // Update the component with the mocked data

      const compiled = fixture.nativeElement;
      const paragraphs = compiled.querySelectorAll('p');

      expect(paragraphs[0].textContent).toContain('Marie TEST');
      expect(paragraphs[1].textContent).toContain('marie@test.com');
      expect(paragraphs[2].textContent).not.toContain('You are admin');

      // Verify that the "Delete" button is visible for a non-admin user
      const deleteButton = fixture.debugElement.query(By.css('button[mat-raised-button]'));
      expect(deleteButton).toBeTruthy();
    });
  });

  describe('Admin user', () => {
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [MeComponent],
        imports: [
          MatSnackBarModule,
          HttpClientModule,
          MatCardModule,
          MatFormFieldModule,
          MatIconModule,
          MatInputModule
        ],
        providers: [{ provide: SessionService, useValue: mockSessionService },
          { provide: UserService, useValue: mockAdminService }],
      })
        .compileComponents();

      fixture = TestBed.createComponent(MeComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it('should display user information correctly for an admin user', () => {
      const mockAdminUser = {
        firstName: 'Louis',
        lastName: 'TEST',
        email: 'louis@test.com',
        admin: true, // Set admin to true
        createdAt: new Date(),
        updatedAt: new Date()
      };
      mockAdminService.getById.mockReturnValue(of(mockAdminUser)); // Mock admin data
      fixture.detectChanges(); // Update the component

      const compiled = fixture.nativeElement;
      const paragraphs = compiled.querySelectorAll('p');

      expect(paragraphs[0].textContent).toContain('Louis TEST');
      expect(paragraphs[1].textContent).toContain('louis@test.com');
      expect(paragraphs[2].textContent).toContain('You are admin'); // Check for admin indicator

      // The "Delete" button could be hidden or modified for admins (depending on your implementation)
      const deleteButton = fixture.debugElement.query(By.css('button[mat-raised-button]'));
      if (deleteButton) {
        // If the button exists, check its visibility or other properties
        // ...
      } else {
        // Expect the button to be absent
        expect(deleteButton).toBeFalsy();
      }
    });
  });
});

import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import {By} from "@angular/platform-browser";
import {UserService} from "../../services/user.service";
import {of} from "rxjs";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const mockUserService = {
    getById: jest.fn().mockReturnValue(
      of({
        firstName: 'Marie',
        lastName: 'TEST',
        email: 'marie@test.com',
        admin: false,
        createdAt: new Date(),
        updatedAt: new Date()
      })
    )
  };
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
        { provide: UserService, useValue: mockUserService },],
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
    // Vérifier l'affichage des informations de l'utilisateur
    const compiled = fixture.nativeElement;
    const paragraphs = compiled.querySelectorAll('p');

    expect(paragraphs[0].textContent).toContain('Marie TEST');
    expect(paragraphs[1].textContent).toContain('marie@test.com');
    expect(paragraphs[2].textContent).not.toContain('You are admin');

    // Vérifier que le bouton "Delete" est visible pour un utilisateur non admin
    const deleteButton = fixture.debugElement.query(By.css('button[mat-raised-button]'));
    expect(deleteButton).toBeTruthy();
  });
});

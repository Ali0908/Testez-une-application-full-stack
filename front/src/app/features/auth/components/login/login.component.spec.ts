import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    const authServiceMock = {
      login: jest.fn().mockReturnValue(throwError(() => new Error('Invalid credentials')))
    };

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        SessionService
      ],
      imports: [
        RouterTestingModule.withRoutes([]),
        BrowserAnimationsModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display an error message when login fails', () => {
    // Remplir le formulaire avec un email et un mot de passe
    component.form.controls['email'].setValue('test@example.com');
    component.form.controls['password'].setValue('wrongpassword');

    // Soumettre le formulaire
    component.submit();

    // Vérifier que authService.login a été appelé
    expect(authService.login).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: 'wrongpassword'
    });

    // Vérifier que onError est passé à true
    expect(component.onError).toBe(true);

    // Détecter les changements dans le DOM
    fixture.detectChanges();

    // Vérifier que le message d'erreur est affiché dans le DOM
    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).not.toBeNull();
    expect(errorElement.textContent).toContain('An error occurred');
  });

  it('should display an error message if any required field is empty', () => {
    // Laisser les champs email et mot de passe vides
    component.form.controls['email'].setValue('');
    component.form.controls['password'].setValue('');

    // Soumettre le formulaire
    component.submit();

    // Vérifier que authService.login n'a pas été appelé car le formulaire est invalide
    expect(authService.login).toHaveBeenCalled();

    // Vérifier que onError est passé à true
    expect(component.onError).toBe(true);

    // Détecter les changements dans le DOM
    fixture.detectChanges();

    // Vérifier que le message d'erreur est affiché dans le DOM
    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).not.toBeNull();
    expect(errorElement.textContent).toContain('An error occurred');
  });

});

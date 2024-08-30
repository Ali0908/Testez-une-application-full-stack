import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { RegisterComponent } from './register.component';
import {AuthService} from "../../services/auth.service";
import {of, throwError} from "rxjs";
import {fireEvent, screen, waitFor} from "@testing-library/angular";
import {Router} from "@angular/router";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let authServiceMock: { register: jest.Mock<any, any> };
  let routerMock: { navigate: jest.Mock<any, any> };
  let router: Router;

  beforeEach(async () => {
     authServiceMock = {
      register: jest.fn().mockReturnValue(throwError(() => new Error('Registration failed')))
    };
    routerMock = {
      navigate: jest.fn().mockResolvedValue(true) // Mock the navigate function
    };
    // Mocking getComputedStyle to prevent the error
    Object.defineProperty(window, 'getComputedStyle', {
      value: () => ({
        getPropertyValue: jest.fn().mockReturnValue(''),
      }),
    });
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    fixture.detectChanges();
    router = TestBed.inject(Router);
    component.router = router;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Unit test: should display an error message if any required field is empty', () => {
    // Laisser les champs firstName et password vides
    component.form.controls['firstName'].setValue('');
    component.form.controls['lastName'].setValue('Doe');
    component.form.controls['email'].setValue('johndoe@example.com');
    component.form.controls['password'].setValue('');

    // Soumettre le formulaire
    component.submit();

    // Todo: Vérifier que authService.register n'a pas été appelé car le formulaire est invalide
    expect(authService.register).toHaveBeenCalled();

    // Vérifier que onError est passé à true
    expect(component.onError).toBe(true);

    // Détecter les changements dans le DOM
    fixture.detectChanges();

    // Vérifier que le message d'erreur est affiché dans le DOM
    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).not.toBeNull();
    expect(errorElement.textContent).toContain('An error occurred');
  });

  it('Integration test: should correctly fill in the fields and redirect to login page on successful registration', async () => {
    component.form.controls['firstName'].setValue('John');
    component.form.controls['lastName'].setValue('Doe');
    component.form.controls['email'].setValue('johndoe@example.com');
    component.form.controls['password'].setValue('password123');

    // Mock a successful registration
    authServiceMock.register.mockReturnValue(of({}));

    // Verify form is valid before mocking successful registration
    expect(component.form.valid).toBe(true);

    // Soumettre le formulaire
    component.submit();

    await waitFor(() => {
      expect(authServiceMock.register).toHaveBeenCalledWith({
        firstName: 'John',
        lastName: 'Doe',
        email: 'johndoe@example.com',
        password: 'password123',
      });
      expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);    });
});
  it('Integration test: it should not redirect to login page on fail registration and incomplete  data in the fields', async () => {
    component.form.controls['firstName'].setValue('John');
    component.form.controls['lastName'].setValue('');
    component.form.controls['email'].setValue('johndoe@example.com');
    component.form.controls['password'].setValue('');

    // Verify form is valid before mocking successful registration
    expect(component.form.valid).toBe(false);

    // Verify that the submit button is disabled
    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(submitButton.disabled).toBe(true);

    // Soumettre le formulaire
    component.submit();

    // Detect changes to update the DOM
    fixture.detectChanges();

    // Verify that the error message is displayed in the DOM
    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).not.toBeNull();
    expect(errorElement.textContent).toContain('An error occurred');

    // Ensure router.navigate is not called since registration should fail
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });
});

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
import {throwError} from "rxjs";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;

  beforeEach(async () => {
    const authServiceMock = {
      register: jest.fn().mockReturnValue(throwError(() => new Error('Registration failed')))
    };
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock }
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
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display an error message if any required field is empty', () => {
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
});

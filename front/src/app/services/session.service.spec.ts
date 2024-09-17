import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { BehaviorSubject, of } from 'rxjs';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('$isLogged()', () => {
    it('should return an observable of the current logged in state', (done) => {
      service.$isLogged().subscribe(isLoggedIn => {
        expect(isLoggedIn).toBe(false); // Initially logged out
        done();
      });
    });

    it('should emit a new value when logged in state changes', (done) => {
      const mockSessionInfo: SessionInformation = { token: 'test', type: 'test', id: 1, username: 'test', firstName: 'test', lastName: 'test', admin: false };

      service.$isLogged().subscribe(isLoggedIn => {
        if (isLoggedIn) {
          expect(isLoggedIn).toBe(true);
          done();
        }
      });

      service.logIn(mockSessionInfo);
    });
  });

  describe('logIn()', () => {
    it('should update session information and logged in state', () => {
      const mockSessionInfo: SessionInformation = { token: 'test', type: 'test', id: 1, username: 'test', firstName: 'test', lastName: 'test', admin: false };

      service.logIn(mockSessionInfo);

      expect(service.sessionInformation).toEqual(mockSessionInfo);
      expect(service.isLogged).toBe(true);
    });

    it('should emit a new value on the isLoggedSubject', () => {
      const spy = jest.spyOn(service.isLoggedSubject, 'next');
      const mockSessionInfo: SessionInformation = { token: 'test', type: 'test', id: 1, username: 'test', firstName: 'test', lastName: 'test', admin: false };

      service.logIn(mockSessionInfo);

      expect(spy).toHaveBeenCalledWith(true);
    });
  });

  describe('logOut()', () => {
    it('should clear session information and set logged in state to false', () => {
      const mockSessionInfo: SessionInformation = { token: 'test', type: 'test', id: 1, username: 'test', firstName: 'test', lastName: 'test', admin: false };
      service.logIn(mockSessionInfo);

      service.logOut();

      expect(service.sessionInformation).toBeUndefined();
      expect(service.isLogged).toBe(false);
    });

    it('should emit a new value on the isLoggedSubject', () => {
      const spy = jest.spyOn(service.isLoggedSubject, 'next');
      service.logOut();

      expect(spy).toHaveBeenCalledWith(false);
    });
  });
});

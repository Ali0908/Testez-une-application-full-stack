import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';

describe('SessionsService', () => {
  let service: SessionApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule
      ]
    });
    service = TestBed.inject(SessionApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('Integration test: should return a list of sessions', () => {
    service.all().subscribe(sessions => {
      expect(sessions).toBeTruthy();
    });
  });

  it('Integration test: should return a session', () => {
    service.detail('1').subscribe(session => {
      expect(session).toBeTruthy();
    });
  });

  it('Integration test: should delete a session', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeTruthy();
    });
  });

  it('Integration test: should create a session', () => {
    service.create({
      teacher_id: 1,
      users: [],
      name: 'Session test', date: new Date(), description: 'Description test'
    }).subscribe(session => {
      expect(session).toBeTruthy();
    });
  });

  it('Integration test: should update a session', () => {
    service.update('1', {
      teacher_id: 1,
      users: [],
      name: 'Session test',
      date: new Date(), description: 'Description test updated'
    }).subscribe(session => {
      expect(session).toBeTruthy();
    });
  });

  it('Integration test: should participate in a session', () => {
    service.participate('1', '1').subscribe(response => {
      expect(response).toBeTruthy();
    });
  });

  it('Integration test: should unparticipate in a session', () => {
    service.unParticipate('1', '1').subscribe(response => {
      expect(response).toBeTruthy();
    });
  });
});

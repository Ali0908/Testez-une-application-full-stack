describe('User participation in a session', () => {
  beforeEach(() => {
    // Visit login page
    cy.visit('/login');

    // Mock the login API response
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false, // User is not an admin for this test
      },
    }).as('login');

    // Mock session API data
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false, // User is not an admin
      },
    }).as('session');

    // Mock the list of sessions (move this up before login action)
    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 101,
          name: 'Yoga Session 1',
          date: '2024-09-09T10:00:00',
          description: 'A relaxing yoga session.',
          users: [],
        },
      ],
    }).as('getSessions');

    // Perform login
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('form').submit(); // Explicit form submit

    // Wait for login and session intercepts
    cy.wait('@login');
    cy.wait('@session');

    // Ensure the user is redirected to the session list page after login
    cy.url({ timeout: 10000 }).should('include', '/sessions');
  });

  it('should allow a user to participate in a session', () => {
    // Ensure the sessions are loaded
    cy.wait('@getSessions', { timeout: 10000 }); // Now wait for the sessions to be loaded

    // Verify that session details are displayed
    cy.get('mat-card').contains('Yoga Session 1').should('be.visible');

    // Click on the Detail button of the session
    cy.get('mat-card-actions button').contains('Detail').click();

    // Mock the session detail API response
    cy.intercept('GET', '/api/sessions/101', {
      body: {
        id: 101,
        name: 'Yoga Session 1',
        date: '2024-09-09T10:00:00',
        description: 'A relaxing yoga session.',
        users: [], // No participants initially
        teacher_id: 1,
      },
    }).as('getSessionDetail');

    // Mock the teacher detail API response
    cy.intercept('GET', '/api/teachers/1', {
      body: {
        firstName: 'John',
        lastName: 'Doe',
      },
    }).as('getTeacherDetail');

    // Ensure the session detail is loaded
    cy.wait('@getSessionDetail');
    cy.wait('@getTeacherDetail');

    // Verify that the Participate button is visible and click it
    cy.get('button').contains('Participate').should('be.visible').click();

    // Mock the participation API response
    cy.intercept('POST', '/api/sessions/101/participate', {}).as('participate');

    // Ensure the participation is processed
    cy.wait('@participate');
  });
});

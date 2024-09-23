describe('User participation in a session', () => {
  beforeEach(() => {

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
    });

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
    });

    // Mock the session detail API response
    cy.intercept('GET', '/api/session/101', {
      body: {
        id: 101,
        name: 'Yoga Session 1',
        date: '2024-09-09T10:00:00',
        description: 'A relaxing yoga session.',
        users: [],
        teacher_id: 1,
      },
    }).as('getSessionDetail');
    // Mock the teacher detail API response
    cy.intercept('GET', '/api/teacher/1', {
      body: {
        firstName: 'John',
        lastName: 'Doe',
      },
    }).as('getTeacherDetail');

    cy.intercept("GET", "/api/user", {
      username: 'userName',
      firstName: 'firstName',
      lastName: 'lastName',
    })
    cy.visit('/login');
    // Mock the login API response
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false,
      },
    });

    // Perform login
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');
    cy.get('form').submit(); // Explicit form submit

    // Ensure the user is redirected to the session list page after login
    cy.url({ timeout: 10000 }).should('include', '/sessions');
  });

  it('should allow a user to participate in a session', () => {

    // Verify that session details are displayed
    cy.get('mat-card').contains('Yoga Session 1').should('be.visible');

    // Click on the Detail button of the session
    cy.get('button[mat-raised-button]').contains('Detail').click();

    // Mock the participation API response
    cy.intercept('POST', '/api/session/101/participate/1', {});

    // Verify that the Participate button is visible and click it
    cy.get('button[mat-raised-button]').contains('Participate').should('be.visible');
  });
});

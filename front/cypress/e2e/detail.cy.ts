describe('Detail Component', () => {
  beforeEach(() => {

    // Mock session API data
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false,
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
    });
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

  it('should display session details', () => {

    // Click on the Detail button of the session
    cy.get('button[mat-raised-button]').contains('Detail').click();
    // Verify session title
    cy.get('mat-card-title h1').should('be.visible');

    // Verify back button presence
    cy.get('button[mat-icon-button]').should('be.visible');

    // Verify session image
    cy.get('img.picture').should('be.visible');

    // Verify session description
    cy.get('.description p').contains('Description:').should('be.visible');

    // Verify creation and update date
    cy.get('.created').should('be.visible');
    cy.get('.updated').should('be.visible');
  });
});

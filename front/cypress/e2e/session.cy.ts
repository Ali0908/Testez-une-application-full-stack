describe('Session', () => {
  beforeEach(() => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 3,
        firstName: 'Louis',
        lastName: 'Test',
        admin: true,
        email: "louis@test.com",
        password: "password"
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [
        {
          id: 1,
          name: 'Yoga Session 1',
          date: '2024-09-09',
          description: 'A relaxing yoga session.',
          users: [3],
          teacher_id: 1,
        },
      ]
    ).as('session');

    cy.intercept('GET', '/api/auth/me', {
      body: {
        id: 3,
        firstName: 'Louis',
        lastName: 'Test',
        email: "louis@test.com",
        password: "password"
      },
    });
    cy.intercept('GET', '/api/user/3', {
      body: {
        id: 3,
        firstName: 'Louis',
        lastName: 'Test',
        email: "louis@test.com",
        password: "password"
      },
    });

    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          firstName: 'John',
          lastName: 'Doe',
        },
        {
          id: 2,
          firstName: 'Jane',
          lastName: 'Smith',
        },
        {
          id: 3,
          firstName: 'Michael',
          lastName: 'Johnson',
        },
        {
          id: 4,
          firstName: 'Emily',
          lastName: 'Davis',
        },
      ],
    });

    cy.get('input[formControlName=email]').type("louis@test.com");
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);

    // **Uncomment the following assertion to verify successful login redirect**
    cy.url().should('include', '/sessions');
  });

  it('should allow a user to create a session', () => {

    cy.intercept('POST', '/api/session', {
      body: {
        id: 2,
        name: 'Cypress Test Session',
        date: '2024-09-23',
        teacher_id: 1,
        description: 'A relaxing yoga session.',
      },
    }).as('createSession')

    // Click on the Detail button of the session
    cy.get('button[mat-raised-button]').contains('Create').click();

    // Verify session title
    cy.get('mat-card-title h1').should('be.visible');

    cy.get('input[formControlName=name]').type("Cypress Test Session");
    cy.get('input[formControlName=date]').type("2024-09-23"); // Replace with your desired date
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('mat-option').contains('John Doe').click();
    cy.get('textarea[formControlName=description]').type("A relaxing yoga session.");
    cy.get('button[mat-raised-button]').should('be.visible');
    cy.get('button[mat-raised-button]').contains('Save').click();

    cy.get('.mat-snack-bar-container').should('be.visible').and('contain', 'Session created !');
  });

  it('should allow a user to update a session', () => {
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Yoga Session 1',
        date: '2024-09-09',
        description: 'A relaxing yoga session.',
        users: [3],
        teacher_id: 1,
      },
    }).as('getSessionDetail');

    cy.intercept('GET', '/api/teacher/1', {
      body: {
        firstName: 'John',
        lastName: 'Doe',
      },
    }).as('getTeacherDetail');

    cy.intercept('PUT', '/api/session/1', {
      body: {
        id: 1,
        name: 'Cypress Test Session Updated',
        date: '2024-09-23',
        teacher_id: 1,
        description: 'A relaxing yoga session.',
        users: [3],
      },
    }).as('updateSession')

    cy.get('button[mat-raised-button]').contains('Edit').click();

    // Verify session title
    cy.get('mat-card-title h1').should('be.visible');
    cy.get('input[formControlName=name]').clear().type("Cypress Test Session Updated");
    cy.get('input[formControlName=date]').clear().type("2024-09-23"); // Replace with your desired date
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('mat-option').contains('Jane Smith').click();
    cy.get('button[mat-raised-button]').contains('Save').click();

    cy.get('.mat-snack-bar-container').should('be.visible').and('contain', 'Session updated !');
  });

  it('should allow a user to delete a session', () => {
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Yoga Session 1',
        date: '2024-09-09',
        description: 'A relaxing yoga session.',
        users: [3],
        teacher_id: 1,
      },
    }).as('getSessionDetail');

    cy.intercept('GET', '/api/teacher/1', {
      body: {
        firstName: 'John',
        lastName: 'Doe',
      },
    }).as('getTeacherDetail');

    cy.intercept('DELETE', '/api/session/1', {
      body: {},
    }).as('deleteSession')

    cy.get('button[mat-raised-button]').contains('Detail').click();

    cy.get('button[mat-raised-button]').contains('Delete').click();
    // cy.get('button[mat-raised-button]').contains('Yes').click();

    cy.get('.mat-snack-bar-container').should('be.visible').and('contain', 'Session deleted !');
  });
});

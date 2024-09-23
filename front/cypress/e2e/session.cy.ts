describe('Create a session', () => {
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
      []
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

    // Add submit button click if needed
    // cy.get('button').contains('Submit').click();

    // **Uncomment the following assertion to verify successful login redirect**
    cy.url().should('include', '/sessions');
  });

  it('should allow a user to create a session', () => {

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
  });

});

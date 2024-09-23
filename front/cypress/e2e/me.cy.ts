describe('Me component spec', () => {
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

    cy.get('input[formControlName=email]').type("louis@test.com");
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);

    // Add submit button click if needed
    // cy.get('button').contains('Submit').click();

    // **Uncomment the following assertion to verify successful login redirect**
    cy.url().should('include', '/sessions');
  });

  it('displays user information after login', () => {
    cy.get('span').contains('Account').click(); // Click on the "Account" link

    // Verify user information
    cy.get('mat-card-title h1').should('contain.text', 'User information');
    cy.get('p').contains('Name:').should('contain.text', 'Louis TEST'); // Replace LastName with 'lastName.toUpperCase()' if needed
    cy.get('p').contains('Email:').should('contain.text', 'louis@test.com');

    // Verify admin badge (if applicable in your test case)
    // cy.get('p.my2').should('contain.text', 'You are admin'); // Adjust selector if needed

    // Verify creation and update date format
    cy.get('p').contains('Create at:').should('exist');
    cy.get('p').contains('Last update:').should('exist');
  });


  // New test to simulate back button click functionality
  it('navigates back on back button click', () => {
    cy.get('span').contains('Account').click(); // Click on the "Account" link

    cy.get('button[mat-icon-button]').click(); // Click back button

    // Verify URL redirects to a previous page (potentially sessions)
    cy.url().should('not.include', '/me'); // Check URL doesn't contain '/me'
  });

  it('deletes account and verifies logout', () => {
    // Navigate to the "Account" page
    cy.get('span').contains('Account').click();

    // Verify user information is displayed correctly
    cy.get('mat-card-title h1').should('contain.text', 'User information');
    cy.get('p').contains('Name:').should('contain.text', 'Louis TEST');
    cy.get('p').contains('Email:').should('contain.text', 'louis@test.com');

    // Intercept the DELETE request for account deletion
    cy.intercept('DELETE', '/api/user/3', {
      statusCode: 200,
      body: {},
    }).as('deleteAccount');

    // Trigger account deletion
    cy.get('button').contains('Detail').click();

    // Verify the success message
    cy.get('snack-bar-container').should('contain.text', 'Your account has been deleted !');

    // Wait for the DELETE request to be called and check that logout happens
    cy.wait('@deleteAccount').its('response.statusCode').should('eq', 200);

    // Verify that the user is redirected to the home page (or login page)
    cy.url().should('include', '/'); // Adjust if it redirects to the login page
  });

});

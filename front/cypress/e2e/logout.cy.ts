describe('Logout spec', () => {
  beforeEach(() => {
    // Visit the login page
    cy.visit('/login');

    // Mock the login API response
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    // Mock session data retrieval
    cy.intercept('GET', '/api/session', []).as('session');

    // Perform login action
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

    // Ensure the URL contains '/sessions' after login
    cy.url().should('include', '/sessions');
  });

  it('should log out successfully and redirect to root page', () => {
    // Ensure the logout button or span in app.component.html is present and clickable
    cy.get('app-root span').contains('Logout').should('be.visible').click();

    // Check that the app navigates to the root URL
    cy.location('pathname').should('eq', '/'); // This verifies the redirection to the root URL

    // Check that session information is cleared from localStorage
    cy.window().its('localStorage').invoke('getItem', 'session').should('be.null');

    // Verify that the login and register links are visible on the root page
    cy.get('span').contains('Login').should('exist');
    cy.get('span').contains('Register').should('exist');
  });
});

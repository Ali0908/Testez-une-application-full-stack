describe('RegisterComponent', () => {
  beforeEach(() => {
    // Navigate to the register page before each test
    cy.visit('/register');
  });

  it('should successfully register a user', () => {
    // Fill in the form
    cy.get('[data-testid="First name"]').type('John');
    cy.get('[data-testid="Last name"]').type('Doe');
    cy.get('[data-testid="Email"]').type('john.doe@example.com');
    cy.get('[data-testid="Password"]').type('password123');

    // Stub the backend API call for registration
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 201,
      body: {},
    }).as('registerUser');

    // Submit the form
    cy.get('button[type="submit"]').click();

    // Assert that the API was called
    cy.wait('@registerUser').its('request.body').should('include', {
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      password: 'password123',
    });

    // Assert that we are redirected to the login page
    cy.url().should('include', '/login');
  });

  it('should show error message on registration failure', () => {
    // Fill in the form
    cy.get('[data-testid="First name"]').type('Jane');
    cy.get('[data-testid="Last name"]').type('Doe');
    cy.get('[data-testid="Email"]').type('jane.doe@example.com');
    cy.get('[data-testid="Password"]').type('password123');

    // Stub the backend API call to simulate a failure
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: { message: 'Registration failed' },
    }).as('registerUserFail');

    // Submit the form
    cy.get('button[type="submit"]').click();

    // Assert that the error message is displayed
    cy.wait('@registerUserFail');
    cy.get('.error').should('contain', 'An error occurred');
  });

  it('should disable the submit button when the form is invalid', () => {
    // Leave the form fields empty and check that the button is disabled
    cy.get('button[type="submit"]').should('be.disabled');

    // Partially fill the form to make it invalid
    cy.get('[data-testid="First name"]').type('J');
    cy.get('[data-testid="Last name"]').type('D');
    cy.get('[data-testid="Email"]').type('invalid-email');

    // Assert that the button is still disabled
    cy.get('button[type="submit"]').should('be.disabled');
  });
});
